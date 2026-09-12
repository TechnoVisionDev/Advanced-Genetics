package com.technovision.advancedgenetics.clienttest;

import com.technovision.advancedgenetics.api.blockentity.AbstractProcessingBlockEntity;
import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreen;
import com.technovision.advancedgenetics.events.ClientKeyInputEvents;
import com.technovision.advancedgenetics.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import vazkii.patchouli.api.PatchouliAPI;
import java.util.concurrent.CompletableFuture;

/** Separate development mod: exercises real client menus, packets, and Patchouli pages. */
@Mod(value = "advancedgenetics_clienttest", dist = Dist.CLIENT)
public class NeoForgeClientSmoke {
    private int step, ticks;
    private int inventoryStep;
    private CompletableFuture<?> pending;
    private java.util.List<Identifier> entries;
    private int entry;
    private final long started = System.currentTimeMillis();
    private static final Identifier BOOK = Identifier.fromNamespaceAndPath("advancedgenetics", "guide");
    public NeoForgeClientSmoke() { NeoForge.EVENT_BUS.addListener(this::tick); }
    private void tick(ClientTickEvent.Post event) {
        if (!Boolean.getBoolean("advancedgenetics.smoke") || step < 0) return;
        var mc = Minecraft.getInstance();
        try {
            if (System.currentTimeMillis() - started > 240000) throw new IllegalStateException("Client smoke timeout at step " + step);
            if (pending != null) { if (!pending.isDone()) return; pending.join(); pending = null; }
            if (++ticks < (step == 3 ? 5 : 20)) return;
            ticks = 0;
            if (step == 0) {
                if (!(mc.screen instanceof TitleScreen)) return;
                mc.options.pauseOnLostFocus = false;
                mc.options.guiScale().set(2);
                mc.createWorldOpenFlows().createFreshLevel("Advanced Genetics-Smoke-" + System.currentTimeMillis(),
                        new LevelSettings("Advanced Genetics validation", GameType.CREATIVE,
                                new LevelSettings.DifficultySettings(Difficulty.PEACEFUL, false, false), true, WorldDataConfiguration.DEFAULT),
                        new WorldOptions(12345L, false, false),
                        provider -> provider.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT).value().createWorldDimensions(), mc.screen);
                step++;
                return;
            }
            if (mc.player == null || mc.getSingleplayerServer() == null) return;
            var server = mc.getSingleplayerServer();
            var uuid = mc.player.getUUID();
            if (step == 2 && inventoryStep < 4) {
                if (inventoryStep % 2 == 0) {
                    // Use the inventory key so vanilla builds all tabs and fires NeoForge/Patchouli events.
                    net.minecraft.client.KeyMapping.click(mc.options.keyInventory.getKey());
                } else {
                    check(mc.screen instanceof CreativeModeInventoryScreen, "Creative inventory opens");
                    var guide = PatchouliAPI.get().getBookStack(BOOK);
                    check(AdvancedGenetics.TAB.getDisplayItems().stream()
                            .filter(stack -> ItemStack.isSameItemSameComponents(stack, guide)).count() == 1,
                            "Exactly one Field Guide in the creative tab");
                    check(AdvancedGenetics.TAB.getDisplayItems().size() == ItemRegistry.ALL_ITEMS.size() + 1,
                            "All mod items and the Field Guide in the creative tab");
                    capture("creative-inventory-" + inventoryStep);
                    mc.player.closeContainer();
                }
                inventoryStep++;
                return;
            }
            if (step == 1) {
                pending = server.submit(() -> {
                    var player = server.getPlayerList().getPlayer(uuid);
                    ComponentRegistry.PLAYER_GENETICS.get(player).addGenes(java.util.List.of(Genes.TELEPORT, Genes.DRAGONS_BREATH, Genes.MOB_SIGHT));
                    player.getInventory().add(PatchouliAPI.get().getBookStack(BOOK));
                    return null;
                });
                step++;
            } else if (step == 2) {
                check(ComponentRegistry.PLAYER_GENETICS.get(mc.player).hasGene(Genes.TELEPORT), "Gene synchronization");
                check(ClientKeyInputEvents.teleportKey != null && ClientKeyInputEvents.dragonsBreathKey != null, "Both key bindings registered");
                net.minecraft.client.KeyMapping.click(ClientKeyInputEvents.teleportKey.getKey());
                step++;
            } else if (step == 3) {
                pending = server.submit(() -> {
                    check(ComponentRegistry.PLAYER_GENETICS.get(server.getPlayerList().getPlayer(uuid)).isOnCooldown("teleport"), "Teleport key reached server");
                    return null;
                });
                net.minecraft.client.KeyMapping.click(ClientKeyInputEvents.dragonsBreathKey.getKey());
                step++;
            } else if (step == 4) {
                pending = server.submit(() -> {
                    check(ComponentRegistry.PLAYER_GENETICS.get(server.getPlayerList().getPlayer(uuid)).isOnCooldown("dragons_breath"), "Dragon breath key reached server");
                    return null;
                });
                step++;
            } else if (step >= 5 && step < 17) {
                int machineIndex = (step - 5) / 2;
                if ((step - 5) % 2 == 0) {
                    pending = server.submit(() -> {
                        var player = server.getPlayerList().getPlayer(uuid);
                        var blocks = new net.minecraft.world.level.block.Block[]{BlockRegistry.CELL_ANALYZER, BlockRegistry.DNA_EXTRACTOR,
                                BlockRegistry.DNA_DECRYPTER, BlockRegistry.PLASMID_INFUSER, BlockRegistry.BLOOD_PURIFIER, BlockRegistry.PLASMID_INJECTOR};
                        BlockPos pos = player.blockPosition().offset(1, 0, 1);
                        player.level().setBlockAndUpdate(pos, blocks[machineIndex].defaultBlockState());
                        var machine = (AbstractProcessingBlockEntity) player.level().getBlockEntity(pos);
                        machine.insertEnergy(12345);
                        player.openMenu(machine);
                        return null;
                    });
                } else {
                    check(mc.screen instanceof AbstractGeneticsScreen<?>, "Machine menu " + machineIndex);
                    check(((AbstractGeneticsScreen<?>) mc.screen).getMenu().getPropertyDelegate().get(2) == 12345, "Machine energy display " + machineIndex);
                    capture("machine-" + machineIndex);
                    mc.player.closeContainer();
                }
                step++;
            } else if (step == 17) {
                var book = vazkii.patchouli.common.book.BookRegistry.INSTANCE.books.get(BOOK);
                check(book != null, "Guidebook registration");
                check(!book.getContents().isErrored(), "Guidebook contents: " + book.getContents().getException());
                check(book.getContents().entries.size() == 55, "All 55 guide entries loaded");
                check(book.getContents().categories.size() == 4, "All four guide categories loaded");
                entries = book.getContents().entries.keySet().stream().sorted().toList();
                PatchouliAPI.get().openBookGUI(BOOK);
                step++;
            } else if (step == 18) {
                check(BOOK.equals(PatchouliAPI.get().getOpenBookGui()), "Guidebook opens");
                capture("guide-overview");
                step++;
            } else if (step == 19) {
                if (entry < entries.size()) {
                    PatchouliAPI.get().openBookEntry(BOOK, entries.get(entry++), 0);
                    check(BOOK.equals(PatchouliAPI.get().getOpenBookGui()), "Guide entry opens");
                } else step++;
            } else if (step == 20) {
                PatchouliAPI.get().openBookEntry(BOOK, Identifier.fromNamespaceAndPath("advancedgenetics", "first_gene"), 0);
                step++;
            } else if (step == 21) {
                capture("guide-first-gene");
                org.slf4j.LoggerFactory.getLogger(NeoForgeClientSmoke.class).info("ADVANCED_GENETICS_CLIENT_SMOKE_PASS: creative inventory opens and reopens with one guide, six menus, gene synchronization, both key packets, 55 guide entries");
                step++;
            } else if (step == 22) {
                step = -1;
                mc.stop();
            }
        } catch (Throwable failure) {
            step = -1;
            org.slf4j.LoggerFactory.getLogger(NeoForgeClientSmoke.class).error("ADVANCED_GENETICS_CLIENT_SMOKE_FAIL", failure);
            failure.printStackTrace();
            mc.stop();
        }
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
    private static void capture(String name) {
        var mc = Minecraft.getInstance();
        Screenshot.grab(mc.gameDirectory, name + ".png", mc.getMainRenderTarget(), 1, message -> System.out.println(message.getString()));
    }
}
