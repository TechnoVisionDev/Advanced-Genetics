package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ScalpelGameTests {
    private static final BlockPos POS = new BlockPos(2, 2, 2);

    @GameTest
    public void allScalpelsCollectThroughRealServerRightClick(GameTestHelper helper) {
        var player = TestPlayers.survival(helper);
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(POS.offset(0, 0, -1))));
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        Item matter = ItemRegistry.MATTER.get(EntityTypes.COW);
        int expectedDrops = 0;
        for (Item scalpel : new Item[]{ItemRegistry.METAL_SCALPEL, ItemRegistry.DIAMOND_SCALPEL, ItemRegistry.NETHERITE_SCALPEL}) {
            var cow = helper.spawn(EntityTypes.COW, POS);
            cow.setNoAi(true);
            var stack = new ItemStack(scalpel);
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            float health = cow.getHealth();
            var packet = new ServerboundInteractPacket(cow.getId(), InteractionHand.MAIN_HAND, new Vec3(0, 0.5, 0), false);
            player.connection.handleInteract(packet);
            helper.assertTrue(drops(helper, matter) == ++expectedDrops, "Right-click produces one cow slice with " + scalpel);
            helper.assertTrue(stack.getDamageValue() == 1 && cow.getHealth() == health - 1,
                    "Scraping costs one durability and one mob health point");
            helper.assertTrue(player.getCooldowns().isOnCooldown(stack), "Scraping starts its cooldown");
            player.connection.handleInteract(packet);
            helper.assertTrue(drops(helper, matter) == expectedDrops && stack.getDamageValue() == 1,
                    "Repeated click during cooldown cannot produce extra matter or use durability");
            for (int i = 0; i < 9; i++) player.getCooldowns().tick();
            helper.assertTrue(player.getCooldowns().isOnCooldown(stack), "Cooldown lasts the original ten ticks");
            player.getCooldowns().tick();
            player.connection.handleInteract(packet);
            helper.assertTrue(drops(helper, matter) == ++expectedDrops && stack.getDamageValue() == 2,
                    "Scalpel collects again after ten ticks");
            cow.discard();
        }
        helper.succeed();
    }

    @GameTest
    public void unhandledGeneInteractionsAllowNormalMobAndItemUse(GameTestHelper helper) {
        var player = TestPlayers.survival(helper);
        var cow = helper.spawn(EntityTypes.COW, POS);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.METAL_SCALPEL));
        helper.assertTrue(UseEntityCallback.EVENT.invoker().interact(player, helper.getLevel(),
                InteractionHand.MAIN_HAND, cow, null) == InteractionResult.PASS,
                "Gene handler must pass ordinary mobs to their normal interaction path");
        var otherPlayer = TestPlayers.survival(helper);
        helper.assertTrue(UseEntityCallback.EVENT.invoker().interact(player, helper.getLevel(),
                InteractionHand.MAIN_HAND, otherPlayer, null) == InteractionResult.PASS,
                "A player without a matching gene interaction must also pass through");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
        player.interactOn(cow, InteractionHand.MAIN_HAND, new Vec3(0, 0.5, 0));
        helper.assertTrue(player.getMainHandItem().is(Items.MILK_BUCKET), "Normal cow milking remains available");
        helper.succeed();
    }

    private static int drops(GameTestHelper helper, Item item) {
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                new net.minecraft.world.phys.AABB(helper.absolutePos(POS)).inflate(3)).stream()
                .filter(entity -> entity.getItem().is(item)).mapToInt(entity -> entity.getItem().getCount()).sum();
    }
}
