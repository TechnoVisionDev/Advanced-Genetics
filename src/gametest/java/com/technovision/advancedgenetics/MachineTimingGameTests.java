package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.api.block.AbstractGeneticsBlock;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreenHandler;
import com.technovision.advancedgenetics.common.item.PlasmidItem;
import com.technovision.advancedgenetics.registry.BlockRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import com.technovision.advancedgenetics.util.ItemData;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class MachineTimingGameTests {
    private static final BlockPos POS = new BlockPos(1, 2, 1);
    private static final AbstractGeneticsBlock[] MACHINES = {
            BlockRegistry.CELL_ANALYZER, BlockRegistry.DNA_EXTRACTOR, BlockRegistry.DNA_DECRYPTER,
            BlockRegistry.PLASMID_INFUSER, BlockRegistry.BLOOD_PURIFIER, BlockRegistry.PLASMID_INJECTOR
    };

    @GameTest
    public void processingDurationAndEnergyRemainExact(GameTestHelper helper) {
        for (AbstractGeneticsBlock block : MACHINES) {
            for (int overclock = 0; overclock <= 1; overclock++) {
                var machine = machine(helper, block);
                prepareInputs(machine, block);
                machine.setOverclock(overclock);
                machine.insertEnergy(machine.getEnergyStorage().getCapacity());
                int duration = machine.getMaxProgress();
                int cost = block.getEnergyRequirement() + Config.Common.overclockEnergy.get() * overclock;
                long originalEnergy = machine.getEnergyStorage().getAmount();
                for (int tick = 0; tick < duration; tick++) machine.tick();
                helper.assertTrue(machine.getProgress() == duration, "Original progress threshold for " + block);
                helper.assertTrue(machine.getStackInSlot(0).getCount() == 1, "Input is retained until completion tick");
                helper.assertTrue(machine.getEnergyStorage().getAmount() == originalEnergy - (long) duration * cost,
                        "Each working tick consumes the original energy cost");
                machine.tick();
                helper.assertTrue(machine.getProgress() == 0 && machine.getStackInSlot(0).isEmpty(),
                        "Operation finishes on threshold plus one tick, matching the original mod");
                helper.assertTrue(machine.getEnergyStorage().getAmount() == originalEnergy - (long) (duration + 1) * cost,
                        "Completion tick also consumes energy");
            }
        }
        helper.succeed();
    }

    @GameTest
    public void blockedRecipesConsumeNothingAndResetProgress(GameTestHelper helper) {
        var machine = machine(helper, BlockRegistry.CELL_ANALYZER);
        prepareInputs(machine, BlockRegistry.CELL_ANALYZER);
        machine.insertEnergy(1000);
        machine.tick();
        helper.assertTrue(machine.getProgress() == 1, "Valid recipe starts processing");
        long remaining = machine.getEnergyStorage().getAmount();
        machine.setStackInSlot(1, new ItemStack(ItemRegistry.CELLS.get(Entities.BAT.getType()), 64));
        machine.tick();
        helper.assertTrue(machine.getProgress() == 0 && machine.getEnergyStorage().getAmount() == remaining,
                "Full output resets progress without consuming energy");
        machine.setStackInSlot(1, new ItemStack(Items.DIRT));
        machine.tick();
        helper.assertTrue(machine.getEnergyStorage().getAmount() == remaining, "Incompatible output blocks processing");
        machine.setStackInSlot(1, ItemStack.EMPTY);
        machine.setStackInSlot(0, new ItemStack(Items.DIRT));
        machine.tick();
        helper.assertTrue(machine.getEnergyStorage().getAmount() == remaining, "Invalid input cannot process");
        prepareInputs(machine, BlockRegistry.CELL_ANALYZER);
        machine.extractEnergy(machine.getEnergyStorage().getAmount());
        machine.setProgress(7);
        machine.tick();
        helper.assertTrue(machine.getProgress() == 0 && machine.getStackInSlot(0).getCount() == 1,
                "Loss of power resets progress and preserves input");
        helper.succeed();
    }

    @GameTest
    public void overclockersCrowbarsAndBlockFacingRemainUsable(GameTestHelper helper) {
        var machine = machine(helper, BlockRegistry.CELL_ANALYZER);
        var player = helper.makeMockServerPlayer(GameType.SURVIVAL);
        ItemStack overclockers = new ItemStack(ItemRegistry.OVERCLOCKER, 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, overclockers);
        var hit = new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(POS)), Direction.NORTH, helper.absolutePos(POS), false);
        var state = helper.getBlockState(POS);
        state.useItemOn(overclockers, helper.getLevel(), player, InteractionHand.MAIN_HAND, hit);
        helper.assertTrue(machine.getOverclock() == 1 && overclockers.getCount() == 1,
                "Survival installation consumes exactly one overclocker");
        int originalDuration = Config.Common.cellAnalyzerTicksPerOperation.get();
        helper.assertTrue(machine.getMaxProgress() == originalDuration - Config.Common.overclockSpeed.get() * 20,
                "Installed overclocker applies configured speed");
        ItemStack crowbar = new ItemStack(ItemRegistry.CROWBAR);
        player.setItemInHand(InteractionHand.MAIN_HAND, crowbar);
        state.useItemOn(crowbar, helper.getLevel(), player, InteractionHand.MAIN_HAND, hit);
        helper.assertTrue(machine.getOverclock() == 0 && crowbar.getDamageValue() == 1,
                "Crowbar removes an upgrade and consumes one durability");
        helper.assertItemEntityCountIs(ItemRegistry.OVERCLOCKER, POS, 2.0, 1);
        helper.assertTrue(machine.getMaxProgress() == originalDuration, "Removing upgrade restores original duration");
        helper.assertTrue(state.setValue(AbstractGeneticsBlock.FACING, Direction.NORTH).rotate(Rotation.CLOCKWISE_90)
                .getValue(AbstractGeneticsBlock.FACING) == Direction.EAST, "Machine facing remains rotatable");
        helper.succeed();
    }

    @GameTest
    public void hopperTransferAndMenuShiftClickKeepOriginalSlots(GameTestHelper helper) {
        var machine = machine(helper, BlockRegistry.CELL_ANALYZER);
        ItemStack matter = new ItemStack(ItemRegistry.MATTER.get(Entities.BAT.getType()), 3);
        var remainder = HopperBlockEntity.addItem(new SimpleContainer(1), machine, matter, Direction.UP);
        helper.assertTrue(remainder.isEmpty() && machine.getStackInSlot(0).getCount() == 3,
                "Hopper insertion reaches the input slot");
        machine.setStackInSlot(1, new ItemStack(ItemRegistry.CELLS.get(Entities.BAT.getType()), 2));
        helper.setBlock(POS.below(), Blocks.HOPPER);
        var hopper = helper.getBlockEntity(POS.below(), HopperBlockEntity.class);
        helper.assertTrue(HopperBlockEntity.suckInItems(helper.getLevel(), hopper), "Hopper extracts machine output");
        helper.assertTrue(hopper.countItem(ItemRegistry.CELLS.get(Entities.BAT.getType())) == 1
                && machine.getStackInSlot(0).getCount() == 3, "Hopper extracts output and leaves input untouched");
        helper.setBlock(POS.below(), Blocks.AIR);
        machine.clearContent();
        var player = helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(9, new ItemStack(ItemRegistry.MATTER.get(Entities.BAT.getType()), 4));
        var menu = (AbstractGeneticsScreenHandler) machine.createMenu(1, player.getInventory(), player);
        helper.assertTrue(menu.slots.size() == 38, "Menu retains 36 player slots followed by input and output");
        menu.quickMoveStack(player, 0);
        helper.assertTrue(machine.getStackInSlot(0).getCount() == 4 && player.getInventory().getItem(9).isEmpty(),
                "Shift-click transfers valid player stacks into machine input");
        machine.setStackInSlot(1, new ItemStack(ItemRegistry.CELLS.get(Entities.BAT.getType())));
        menu.quickMoveStack(player, 37);
        helper.assertTrue(machine.getStackInSlot(1).isEmpty()
                && player.getInventory().countItem(ItemRegistry.CELLS.get(Entities.BAT.getType())) == 1,
                "Shift-click takes finished output into the player inventory");
        for (AbstractGeneticsBlock block : MACHINES) {
            var current = machine(helper, block);
            var currentMenu = (AbstractGeneticsScreenHandler) current.createMenu(2, player.getInventory(), player);
            helper.assertTrue(currentMenu.slots.size() == 38 && currentMenu.getPropertyDelegate().getCount() == 5,
                    "Every machine retains its slots and progress/energy/overclock data");
        }
        helper.succeed();
    }

    @GameTest
    public void breakingMachineDropsBothInventorySlots(GameTestHelper helper) {
        var machine = machine(helper, BlockRegistry.DNA_EXTRACTOR);
        var cell = ItemRegistry.CELLS.get(Entities.BAT.getType());
        machine.setStackInSlot(0, new ItemStack(cell, 3));
        machine.setStackInSlot(1, new ItemStack(ItemRegistry.DNA_HELIX, 2));
        helper.setBlock(POS, Blocks.AIR);
        helper.assertItemEntityCountIs(cell, POS, 2.0, 3);
        helper.assertItemEntityCountIs(ItemRegistry.DNA_HELIX, POS, 2.0, 2);
        helper.succeed();
    }

    private static AbstractInventoryBlockEntity machine(GameTestHelper helper, AbstractGeneticsBlock block) {
        if (helper.getLevel().getBlockEntity(helper.absolutePos(POS)) instanceof AbstractInventoryBlockEntity previous) previous.clearContent();
        helper.setBlock(POS, Blocks.AIR);
        helper.setBlock(POS, block);
        return helper.getBlockEntity(POS, AbstractInventoryBlockEntity.class);
    }

    private static void prepareInputs(AbstractInventoryBlockEntity machine, AbstractGeneticsBlock block) {
        machine.clearContent();
        if (block == BlockRegistry.CELL_ANALYZER) {
            machine.setStackInSlot(0, new ItemStack(ItemRegistry.MATTER.get(Entities.BAT.getType())));
        } else if (block == BlockRegistry.DNA_EXTRACTOR) {
            machine.setStackInSlot(0, new ItemStack(ItemRegistry.CELLS.get(Entities.BAT.getType())));
        } else if (block == BlockRegistry.DNA_DECRYPTER || block == BlockRegistry.PLASMID_INFUSER) {
            ItemStack dna = new ItemStack(ItemRegistry.DNA_HELIX);
            ItemData.update(dna, tag -> {
                tag.putString("gene", Genes.NIGHT_VISION.toString());
                tag.putBoolean("decoded", block == BlockRegistry.PLASMID_INFUSER);
            });
            machine.setStackInSlot(0, dna);
            if (block == BlockRegistry.PLASMID_INFUSER) machine.setStackInSlot(1, new ItemStack(ItemRegistry.PLASMID));
        } else {
            ItemStack blood = new ItemStack(ItemRegistry.SYRINGE);
            ItemData.update(blood, tag -> { tag.putBoolean("filled", true); tag.putBoolean("purified", block == BlockRegistry.PLASMID_INJECTOR); });
            if (block == BlockRegistry.BLOOD_PURIFIER) machine.setStackInSlot(0, blood);
            else {
                ItemStack plasmid = new ItemStack(ItemRegistry.PLASMID);
                ItemData.update(plasmid, tag -> { tag.putString("gene", Genes.NIGHT_VISION.toString()); tag.putInt("count", PlasmidItem.MAX_GENES); });
                machine.setStackInSlot(0, plasmid);
                machine.setStackInSlot(1, blood);
            }
        }
    }
}
