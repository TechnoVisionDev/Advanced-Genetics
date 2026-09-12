package com.technovision.advancedgenetics.gametest;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.AdvancedGenetics;

import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.item.*;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipe;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.*;
import com.technovision.advancedgenetics.util.ItemData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;

public class GeneticsGameTests {
    private static final BlockPos POS = new BlockPos(1, 1, 1);

    @GameTest
    public void neoforgeTransactionsAndPatchouliRecipeWork(GameTestHelper helper) {
        var analyzer = machine(helper, BlockRegistry.CELL_ANALYZER);
        var energy = helper.getLevel().getCapability(Capabilities.Energy.BLOCK, helper.absolutePos(POS), Direction.UP);
        helper.assertTrue(energy != null, "NeoForge energy capability exists");
        try (var transaction = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
            helper.assertTrue(energy.insert(1000, transaction) == 1000, "Energy can be inserted in a transaction");
        }
        helper.assertTrue(energy.getAmountAsLong() == 0, "Aborted energy transfer rolls back");
        try (var transaction = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
            energy.insert(1000, transaction);
            transaction.commit();
        }
        helper.assertTrue(energy.getAmountAsLong() == 1000, "Committed energy transfer is retained");
        var items = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(POS), Direction.UP);
        helper.assertTrue(items != null, "NeoForge item automation capability exists");
        var matter = net.neoforged.neoforge.transfer.item.ItemResource.of(ItemRegistry.MATTER.get(Entities.BAT.getType()));
        try (var transaction = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
            helper.assertTrue(items.insert(0, matter, 1, transaction) == 1, "Automation accepts valid matter");
            transaction.commit();
        }
        helper.assertTrue(analyzer.getStackInSlot(0).getCount() == 1, "Automation updates machine inventory");
        var input = net.minecraft.world.item.crafting.CraftingInput.of(2, 1, java.util.List.of(
                new ItemStack(net.minecraft.world.item.Items.BOOK), new ItemStack(net.minecraft.world.item.Items.IRON_INGOT)));
        var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,
                input, helper.getLevel()).orElseThrow();
        var book = recipe.value().assemble(input);
        helper.assertTrue(ItemStack.isSameItemSameComponents(book, vazkii.patchouli.api.PatchouliAPI.get().getBookStack(id("guide"))),
                "Book and iron craft the correct Patchouli guide");
        helper.succeed();
    }

    @GameTest
    public void allOriginalContentAndCellRecipesLoad(GameTestHelper helper) {
        helper.assertTrue(Genes.values().length == 39, "All 38 abilities and the basic gene are retained");
        helper.assertTrue(Entities.values().length == 39, "All 39 original mob sources are retained");
        helper.assertTrue(ItemRegistry.ALL_ITEMS.size() == 94, "All 94 original items are registered");
        helper.assertTrue(Config.Common.genes.size() == 38, "Every ability still has a config toggle");
        for (Entities entity : Entities.values()) {
            var matter = ItemRegistry.MATTER.get(entity.getType());
            var cell = ItemRegistry.CELLS.get(entity.getType());
            helper.assertTrue(BuiltInRegistries.ITEM.getKey(matter).equals(id(entity.getName() + "_matter")), "Original matter ID");
            helper.assertTrue(BuiltInRegistries.ITEM.getKey(cell).equals(id(entity.getName() + "_cell")), "Original cell ID");
            var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(CellAnalyzerRecipe.Type.INSTANCE,
                    new SingleRecipeInput(new ItemStack(matter, 64)), helper.getLevel()).orElseThrow().value();
            helper.assertTrue(recipe.getOutput().is(cell), "Every mob's matter produces its own cell");
            helper.assertTrue(recipe.getInput().getCount() == 1 && recipe.getOutput().getCount() == 1, "Original analyzer quantities");
        }
        helper.succeed();
    }

    @GameTest
    public void machinesCompleteTheOriginalGeneticsWorkflow(GameTestHelper helper) {
        var analyzer = machine(helper, BlockRegistry.CELL_ANALYZER);
        analyzer.setStackInSlot(0, new ItemStack(ItemRegistry.MATTER.get(Entities.BAT.getType())));
        complete(helper, analyzer);
        ItemStack cell = analyzer.getStackInSlot(1).copy();
        helper.assertTrue(cell.is(ItemRegistry.CELLS.get(Entities.BAT.getType())), "Matter becomes a cell");

        var extractor = machine(helper, BlockRegistry.DNA_EXTRACTOR);
        extractor.setStackInSlot(0, cell);
        complete(helper, extractor);
        ItemStack dna = extractor.getStackInSlot(1).copy();
        helper.assertTrue(dna.is(ItemRegistry.DNA_HELIX) && !DnaItem.isDecoded(dna), "Cell becomes encrypted DNA");
        helper.assertTrue(Genes.getGeneByItem(dna) == Genes.BASIC || Genes.getGeneByItem(dna) == Genes.NIGHT_VISION,
                "Bat DNA keeps its original possible genes");

        var decrypter = machine(helper, BlockRegistry.DNA_DECRYPTER);
        decrypter.setStackInSlot(0, dna);
        complete(helper, decrypter);
        dna = decrypter.getStackInSlot(1).copy();
        helper.assertTrue(DnaItem.isDecoded(dna), "Decrypter reveals the same DNA gene");
        ItemData.update(dna, tag -> tag.putString("gene", Genes.NIGHT_VISION.toString()));

        var infuser = machine(helper, BlockRegistry.PLASMID_INFUSER);
        infuser.setStackInSlot(0, dna.copyWithCount(12));
        infuser.setStackInSlot(1, new ItemStack(ItemRegistry.PLASMID));
        for (int i = 0; i < 12; i++) complete(helper, infuser);
        ItemStack plasmid = infuser.getStackInSlot(1).copy();
        helper.assertTrue(ItemData.read(plasmid).getIntOr("count", 0) == 24, "12 matching helices fill the 24-gene plasmid");

        var player = helper.makeMockServerPlayerInLevel();
        player.setGameMode(GameType.SURVIVAL);
        ItemStack blood = new ItemStack(ItemRegistry.SYRINGE);
        SyringeItem.fill(blood, player);
        var purifier = machine(helper, BlockRegistry.BLOOD_PURIFIER);
        purifier.setStackInSlot(0, blood);
        complete(helper, purifier);
        blood = purifier.getStackInSlot(1).copy();
        helper.assertTrue(SyringeItem.isFilled(blood) && SyringeItem.isPurified(blood), "Blood purifier cleans filled syringe");

        var injector = machine(helper, BlockRegistry.PLASMID_INJECTOR);
        injector.setStackInSlot(0, plasmid);
        injector.setStackInSlot(1, blood);
        complete(helper, injector);
        blood = injector.getStackInSlot(1);
        helper.assertFalse(SyringeItem.isPurified(blood), "Adding a gene contaminates blood as before");
        helper.assertTrue(ItemData.read(blood).getCompoundOrEmpty("genes").contains("NIGHT_VISION"), "Injector transfers plasmid gene");
        SyringeItem.purify(blood);
        SyringeItem.inject(player, blood);
        helper.assertTrue(ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.NIGHT_VISION), "Syringe grants gene");
        helper.assertFalse(SyringeItem.isFilled(blood), "Injection empties syringe");
        helper.succeed();
    }

    @GameTest
    public void energyOverclockInventoryAndSaveDataSurviveReload(GameTestHelper helper) {
        for (Block block : new Block[]{BlockRegistry.CELL_ANALYZER, BlockRegistry.DNA_EXTRACTOR,
                BlockRegistry.DNA_DECRYPTER, BlockRegistry.PLASMID_INFUSER, BlockRegistry.BLOOD_PURIFIER,
                BlockRegistry.PLASMID_INJECTOR}) {
            var machine = machine(helper, block);
            helper.assertTrue(machine.getEnergyStorage().getCapacityAsLong() == 20000, "Original 20,000 E capacity");
            helper.assertTrue(helper.getLevel().getCapability(Capabilities.Energy.BLOCK, helper.absolutePos(POS), Direction.NORTH)
                    == machine.getEnergyStorage(), "External energy providers can connect");
            int originalTicks = machine.getMaxProgress();
            machine.setOverclock(1);
            helper.assertTrue(machine.getMaxProgress() == Math.max(20, originalTicks - 40), "Overclock preserves two-second speedup");
            machine.insertEnergy(12345);
            machine.setProgress(17);
            ItemStack dna = geneDna(Genes.FLIGHT);
            machine.setStackInSlot(0, dna);
            var saved = machine.saveWithFullMetadata(helper.getLevel().registryAccess());
            var loaded = (AbstractInventoryBlockEntity) BlockEntity.loadStatic(helper.absolutePos(POS),
                    machine.getBlockState(), saved, helper.getLevel().registryAccess());
            helper.assertTrue(loaded != null && loaded.getProgress() == 17 && loaded.getOverclock() == 1,
                    "Progress and overclock survive save/load");
            helper.assertTrue(loaded.getEnergyStorage().getAmountAsLong() == 12345, "Stored energy survives save/load");
            helper.assertTrue(ItemStack.isSameItemSameComponents(loaded.getStackInSlot(0), dna), "Inventory retains gene data");
            helper.assertTrue(machine.canPlaceItemThroughFace(0, dna, Direction.UP)
                    && !machine.canTakeItemThroughFace(0, dna, Direction.DOWN)
                    && machine.canTakeItemThroughFace(1, dna, Direction.DOWN), "Automation keeps original input/output sides");
        }
        helper.succeed();
    }

    @GameTest
    public void plasmidRulesAndAntiGenesMatchOriginalBehavior(GameTestHelper helper) {
        ItemStack basic = geneDna(Genes.BASIC);
        ItemStack flight = geneDna(Genes.FLIGHT);
        ItemStack plasmid = new ItemStack(ItemRegistry.PLASMID);
        helper.assertFalse(PlasmidItem.canCombine(basic, plasmid), "Basic DNA cannot start a plasmid");
        PlasmidItem.combine(flight, plasmid);
        helper.assertTrue(ItemData.read(plasmid).getIntOr("count", 0) == 2, "Specific DNA contributes two genes");
        PlasmidItem.combine(basic, plasmid);
        helper.assertTrue(ItemData.read(plasmid).getIntOr("count", 0) == 3, "Basic DNA contributes one gene");
        helper.assertFalse(PlasmidItem.canCombine(geneDna(Genes.SPEED), plasmid), "Mismatched specific DNA is rejected");
        var player = helper.makeMockServerPlayerInLevel();
        player.setGameMode(GameType.SURVIVAL);
        var component = ComponentRegistry.PLAYER_GENETICS.get(player);
        component.addGene(Genes.FLIGHT);
        ItemStack anti = new ItemStack(ItemRegistry.ANTIPLASMID);
        AntiPlasmidItem.combine(flight, anti);
        ItemStack syringe = new ItemStack(ItemRegistry.SYRINGE);
        SyringeItem.fill(syringe, player);
        SyringeItem.addAntiGene(anti, syringe);
        SyringeItem.purify(syringe);
        SyringeItem.inject(player, syringe);
        helper.assertFalse(component.hasGene(Genes.FLIGHT), "Antiplasmid removes its gene");
        helper.succeed();
    }

    @GameTest
    public void playerGenesPersistAndApplyOriginalPassiveEffects(GameTestHelper helper) {
        var player = TestPlayers.survival(helper);
        var component = ComponentRegistry.PLAYER_GENETICS.get(player);
        for (Genes gene : Genes.values()) if (gene != Genes.BASIC) component.addGene(gene);
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
        component.writeData(output);
        var restored = new PlayerGeneticsComponent(player);
        restored.readData(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        helper.assertTrue(restored.geneCount() == 38, "Every ability survives persistence");
        player.getFoodData().setFoodLevel(8);
        for (int i = 0; i < 80; i++) component.serverTick();
        helper.assertTrue(player.getAbilities().mayfly, "Flight gene grants survival flight");
        helper.assertTrue(player.getFoodData().getFoodLevel() == 9, "No-hunger restores one food point below half");
        for (var effect : java.util.List.of(MobEffects.RESISTANCE, MobEffects.HASTE, MobEffects.SPEED,
                MobEffects.REGENERATION, MobEffects.STRENGTH, MobEffects.FIRE_RESISTANCE, MobEffects.NIGHT_VISION,
                MobEffects.JUMP_BOOST, MobEffects.WATER_BREATHING, MobEffects.INVISIBILITY, MobEffects.LUCK)) {
            helper.assertTrue(player.hasEffect(effect) && player.getEffect(effect).getAmplifier() == 0,
                    "Original level-one passive effect is applied");
        }
        component.addCooldown("teleport", 1);
        helper.assertTrue(component.isOnCooldown("teleport") && !component.isOnCooldown("dragons_breath"),
                "Ability cooldowns remain independent");
        component.removeAllGenes();
        for (int i = 0; i < 80; i++) component.serverTick();
        helper.assertFalse(player.getAbilities().mayfly, "Removing flight revokes survival flight");
        helper.succeed();
    }

    private static Identifier id(String path) { return Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, path); }
    private static ItemStack geneDna(Genes gene) {
        var stack = new ItemStack(ItemRegistry.DNA_HELIX);
        ItemData.update(stack, tag -> { tag.putString("gene", gene.toString()); tag.putBoolean("decoded", true); });
        return stack;
    }
    private static AbstractInventoryBlockEntity machine(GameTestHelper helper, Block block) {
        helper.setBlock(POS, net.minecraft.world.level.block.Blocks.AIR);
        helper.setBlock(POS, block);
        return helper.getBlockEntity(POS, AbstractInventoryBlockEntity.class);
    }
    private static void complete(GameTestHelper helper, AbstractInventoryBlockEntity machine) {
        machine.insertEnergy(machine.getEnergyStorage().getCapacityAsLong() - machine.getEnergyStorage().getAmountAsLong());
        machine.updateRecipe();
        helper.assertTrue(machine.canProcessRecipe(), "Machine accepts valid input and energy: " + machine.getName().getString());
        machine.setProgress(machine.getMaxProgress());
        machine.tick();
        helper.assertTrue(machine.getProgress() == 0, "Machine completes and resets progress");
    }
}
