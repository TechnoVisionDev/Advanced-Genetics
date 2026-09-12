package com.technovision.advancedgenetics.gametest;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.AdvancedGenetics;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.goal.FleePlayerGoal;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GeneAbilityGameTests {
    private static final BlockPos POS = new BlockPos(2, 2, 2);

    @GameTest
    public void damageImmunitiesAndDragonCrystalRetainOriginalRules(GameTestHelper helper) {
        ServerPlayer player = player(helper);
        var component = ComponentRegistry.PLAYER_GENETICS.get(player);
        float health = player.getHealth();
        component.addGene(Genes.NO_FALL_DAMAGE);
        helper.assertFalse(player.hurtServer(helper.getLevel(), player.damageSources().fall(), 8), "Fall damage is canceled by the mixin");
        helper.assertTrue(player.getHealth() == health, "No-fall gene preserves health");
        component.removeAllGenes();
        component.addGene(Genes.POISON_IMMUNITY);
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
        helper.assertFalse(player.hurtServer(helper.getLevel(), player.damageSources().magic(), 3), "Poison immunity cancels incoming damage while poisoned");
        helper.assertFalse(player.hasEffect(MobEffects.POISON), "Poison is removed");
        component.removeAllGenes();
        component.addGene(Genes.WITHER_RESISTANCE);
        player.addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
        helper.assertFalse(player.hurtServer(helper.getLevel(), player.damageSources().wither(), 3), "Wither resistance cancels incoming damage while withered");
        helper.assertFalse(player.hasEffect(MobEffects.WITHER), "Wither is removed");
        component.removeAllGenes();
        component.addGene(Genes.DRAGONS_HEALTH);
        var crystal = new ItemStack(ItemRegistry.DRAGON_HEALTH_CRYSTAL);
        player.getInventory().setItem(8, crystal);
        helper.assertFalse(player.hurtServer(helper.getLevel(), player.damageSources().generic(), 4.75F), "Crystal absorbs damage from any inventory slot");
        helper.assertTrue(crystal.getDamageValue() == 4 && player.getHealth() == health, "Crystal preserves original integer durability cost");
        helper.succeed();
    }

    @GameTest
    public void grassMilkHoneyMeatAndWoolKeepOriginalInteractions(GameTestHelper helper) {
        ServerPlayer actor = player(helper);
        ServerPlayer donor = player(helper);
        var actorGenes = ComponentRegistry.PLAYER_GENETICS.get(actor);
        var donorGenes = ComponentRegistry.PLAYER_GENETICS.get(donor);
        actorGenes.addGene(Genes.EAT_GRASS);
        actor.getFoodData().setFoodLevel(10);
        helper.setBlock(POS, Blocks.GRASS_BLOCK);
        var hit = new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(POS)), Direction.UP, helper.absolutePos(POS), false);
        com.technovision.advancedgenetics.events.GeneticsEvents.useBlock(actor, helper.getLevel(), InteractionHand.MAIN_HAND, hit);
        helper.assertBlockPresent(Blocks.DIRT, POS);
        helper.assertTrue(actor.getFoodData().getFoodLevel() == 11, "Eating grass restores exactly one food point");

        donorGenes.addGene(Genes.MILKY);
        actor.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
        interact(helper, actor, donor);
        helper.assertTrue(actor.getMainHandItem().is(Items.MILK_BUCKET) && donorGenes.isOnCooldown("milky"), "Milky fills the bucket and starts its cooldown");
        actor.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
        interact(helper, actor, donor);
        helper.assertTrue(actor.getMainHandItem().is(Items.BUCKET), "Milking again during cooldown does not refill");

        donorGenes.addGene(Genes.BEELICIOUS);
        actor.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GLASS_BOTTLE, 2));
        interact(helper, actor, donor);
        helper.assertTrue(actor.getMainHandItem().getCount() == 1 && actor.getInventory().countItem(Items.HONEY_BOTTLE) == 1
                && donorGenes.isOnCooldown("beelicious"), "Honey consumes one bottle and yields one honey bottle");

        donorGenes.addGene(Genes.MEATY);
        donorGenes.addGene(Genes.WOOLY);
        actor.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.SHEARS));
        interact(helper, actor, donor);
        helper.assertItemEntityPresent(Items.PORKCHOP);
        helper.assertItemEntityPresent(Items.WHITE_WOOL);
        helper.assertTrue(actor.getMainHandItem().getDamageValue() == 2 && donorGenes.isOnCooldown("meaty")
                && donorGenes.isOnCooldown("wooly"), "Both shearing genes activate independently and each costs one durability");
        interact(helper, actor, donor);
        helper.assertTrue(actor.getMainHandItem().getDamageValue() == 2, "Cooldown blocks additional shearing");
        helper.succeed();
    }

    @GameTest
    public void witherHitAndInfinityRetainCombatBehavior(GameTestHelper helper) {
        ServerPlayer player = player(helper);
        var component = ComponentRegistry.PLAYER_GENETICS.get(player);
        var target = helper.spawn(EntityType.PIG, new BlockPos(3, 2, 3));
        component.addGene(Genes.WITHER_HIT);
        var result = com.technovision.advancedgenetics.events.GeneticsEvents.attack(player, helper.getLevel(), target);
        var effect = target.getEffect(MobEffects.WITHER);
        helper.assertTrue(result == InteractionResult.SUCCESS && effect != null && effect.getDuration() >= 20
                && effect.getDuration() <= 100 && effect.getAmplifier() == 0, "Wither hit preserves its 1–5 second level-one effect");
        helper.assertTrue(com.technovision.advancedgenetics.events.GeneticsEvents.attack(player, helper.getLevel(), target)
                == InteractionResult.PASS, "Existing wither is not refreshed");
        component.removeAllGenes();
        component.addGene(Genes.INFINITY);
        var bow = new ItemStack(Items.BOW);
        player.setItemInHand(InteractionHand.MAIN_HAND, bow);
        player.getInventory().setItem(8, new ItemStack(Items.ARROW, 3));
        player.startUsingItem(InteractionHand.MAIN_HAND);
        helper.assertTrue(((BowItem) Items.BOW).releaseUsing(bow, helper.getLevel(), player, 71980), "Infinity bow fires at full draw");
        helper.assertTrue(player.getInventory().countItem(Items.ARROW) == 3 && bow.getDamageValue() == 1,
                "Infinity preserves ammunition while retaining normal bow durability cost");
        var arrows = helper.getLevel().getEntitiesOfClass(AbstractArrow.class, player.getBoundingBox().inflate(3));
        helper.assertTrue(!arrows.isEmpty() && arrows.stream().allMatch(arrow -> arrow.pickup == AbstractArrow.Pickup.CREATIVE_ONLY),
                "Infinity arrows cannot be collected in survival");
        helper.succeed();
    }

    @GameTest
    public void deathGenesAndKeepInventoryCopyRemainAvailable(GameTestHelper helper) {
        ServerPlayer original = player(helper);
        var component = ComponentRegistry.PLAYER_GENETICS.get(original);
        component.addGenes(java.util.List.of(Genes.EMERALD_HEART, Genes.SLIMY, Genes.KEEP_INVENTORY));
        original.getInventory().setItem(8, new ItemStack(Items.DIAMOND, 7));
        com.technovision.advancedgenetics.events.GeneticsEvents.onDeath(original);
        helper.assertItemEntityPresent(Items.EMERALD);
        helper.assertEntityPresent(EntityType.SLIME);
        helper.assertTrue(component.isOnCooldown("emerald_heart"), "Emerald heart applies its cooldown");
        original.die(original.damageSources().generic());
        helper.assertTrue(original.getInventory().countItem(Items.DIAMOND) == 7, "Keep inventory prevents death drops");
        ServerPlayer restored = player(helper);
        restored.restoreFrom(original, false);
        helper.assertTrue(ComponentRegistry.PLAYER_GENETICS.get(restored).hasGene(Genes.KEEP_INVENTORY), "NeoForge copies genes to the respawned player");
        helper.assertTrue(restored.getInventory().countItem(Items.DIAMOND) == 7, "Respawn receives the retained inventory");
        helper.succeed();
    }

    @GameTest
    public void wallClimbingWebWalkingAndFearGoalsRemainInstalled(GameTestHelper helper) throws ReflectiveOperationException {
        ServerPlayer player = player(helper);
        var component = ComponentRegistry.PLAYER_GENETICS.get(player);
        helper.setBlock(POS.below(), Blocks.STONE);
        helper.setBlock(POS.north(), Blocks.STONE);
        helper.setBlock(POS.north().above(), Blocks.STONE);
        helper.assertFalse(player.onClimbable(), "Ordinary player cannot climb a stone wall");
        component.addGene(Genes.CLIMB_WALLS);
        helper.assertTrue(player.onClimbable(), "Wall-climbing gene marks an adjacent wall climbable");
        component.addGene(Genes.WEB_WALKING);
        double before = player.getX();
        player.makeStuckInBlock(Blocks.COBWEB.defaultBlockState(), new Vec3(0.25, 0.05, 0.25));
        player.move(MoverType.SELF, new Vec3(0.5, 0, 0));
        helper.assertTrue(Math.abs(player.getX() - before - 0.5) < 0.0001, "Web-walking gene removes the cobweb movement multiplier");
        var selectorField = Mob.class.getDeclaredField("goalSelector");
        selectorField.setAccessible(true);
        for (Mob mob : new Mob[]{helper.spawn(EntityType.CREEPER, POS), helper.spawn(EntityType.SKELETON, POS)}) {
            GoalSelector selector = (GoalSelector) selectorField.get(mob);
            helper.assertTrue(selector.getAvailableGoals().stream().anyMatch(goal -> goal.getPriority() == 3
                    && goal.getGoal() instanceof FleePlayerGoal), "Fear gene avoidance is installed at the original priority");
        }
        helper.succeed();
    }

    @GameTest
    public void geneCommandsRetainExecutionAndPermissionRequirements(GameTestHelper helper) throws Exception {
        ServerPlayer player = player(helper);
        var dispatcher = helper.getLevel().getServer().getCommands().getDispatcher();
        var source = player.createCommandSourceStack().withPermission(PermissionSet.ALL_PERMISSIONS);
        helper.assertTrue(dispatcher.execute("gene add @s flight", source) == 1, "Authorized add command succeeds");
        helper.assertTrue(ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.FLIGHT), "Add command installs selected gene");
        helper.assertTrue(dispatcher.execute("gene remove @s flight", source) == 1, "Authorized remove command succeeds");
        helper.assertFalse(ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.FLIGHT), "Remove command removes selected gene");
        dispatcher.execute("gene add @s haste", source);
        helper.assertTrue(dispatcher.execute("gene clear @s", source) == 1 && ComponentRegistry.PLAYER_GENETICS.get(player).geneCount() == 0,
                "Clear command removes all genes");
        boolean denied = false;
        try {
            dispatcher.execute("gene add @s flight", source.withPermission(PermissionSet.NO_PERMISSIONS));
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException expected) {
            denied = true;
        }
        helper.assertTrue(denied && ComponentRegistry.PLAYER_GENETICS.get(player).geneCount() == 0, "Unprivileged players cannot grant genes");
        helper.succeed();
    }

    private static ServerPlayer player(GameTestHelper helper) {
        ServerPlayer player = TestPlayers.survival(helper);
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(POS)));
        return player;
    }

    private static void interact(GameTestHelper helper, ServerPlayer actor, ServerPlayer donor) {
        com.technovision.advancedgenetics.events.GeneticsEvents.useEntity(actor, helper.getLevel(), InteractionHand.MAIN_HAND, donor);
    }
}
