package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreen;
import com.technovision.advancedgenetics.registry.BlockRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;

public class GeneticsClientTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            world.getServer().runOnServer(server -> server.getPlayerList().getPlayers().getFirst().setGameMode(GameType.CREATIVE));
            world.getConnection().waitForClientboundPackets();
            context.runOnClient(client -> client.gui.setScreen(new CreativeModeInventoryScreen(
                    client.player, client.level.enabledFeatures(), true)));
            context.waitForScreen(CreativeModeInventoryScreen.class);
            context.runOnClient(client -> {
                var screen = (FabricCreativeModeInventoryScreen) client.gui.screen();
                if (!screen.setSelectedTab(AdvancedGenetics.TAB)) throw new AssertionError("Genetics tab must be selectable");
                if (!AdvancedGenetics.TAB.getDisplayName().getString().equals("Advanced Genetics"))
                    throw new AssertionError("Creative tab translation");
                for (var item : ItemRegistry.ALL_ITEMS) {
                    if (!AdvancedGenetics.TAB.contains(new ItemStack(item))) throw new AssertionError("Missing creative item: " + item);
                }
            });
            context.waitTicks(5);
            context.takeScreenshot("advancedgenetics-creative-tab");
            context.runOnClient(client -> client.player.closeContainer());
            for (Block block : new Block[]{BlockRegistry.CELL_ANALYZER, BlockRegistry.DNA_EXTRACTOR,
                    BlockRegistry.DNA_DECRYPTER, BlockRegistry.PLASMID_INFUSER, BlockRegistry.BLOOD_PURIFIER,
                    BlockRegistry.PLASMID_INJECTOR}) {
                world.getServer().runOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    var pos = player.blockPosition().offset(2, 0, 0);
                    player.level().setBlockAndUpdate(pos, block.defaultBlockState());
                    var machine = (AbstractInventoryBlockEntity) player.level().getBlockEntity(pos);
                    machine.insertEnergy(12345);
                });
                context.waitFor(client -> client.level.getBlockState(client.player.blockPosition().offset(2, 0, 0)).is(block));
                world.getServer().runOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    var machine = (AbstractInventoryBlockEntity) player.level().getBlockEntity(player.blockPosition().offset(2, 0, 0));
                    player.openMenu(machine);
                });
                world.getConnection().waitForClientboundPackets();
                context.waitFor(client -> client.gui.screen() instanceof AbstractGeneticsScreen<?> screen
                        && screen.getMenu().getPropertyDelegate().get(2) == 12345
                        && screen.getMenu().getPropertyDelegate().get(3) == 20000);
                context.waitTicks(5);
                context.runOnClient(client -> {
                    var screen = (AbstractGeneticsScreen<?>) client.gui.screen();
                    if (screen.getMenu().slots.size() != 38) throw new AssertionError("Two machine slots and 36 player slots");
                    if (screen.getTitle().getString().contains(".container.")) throw new AssertionError("Machine title translation");
                });
                context.takeScreenshot("advancedgenetics-" + BuiltInRegistries.BLOCK.getKey(block).getPath());
                context.runOnClient(client -> client.player.closeContainer());
            }
        }
    }
}
