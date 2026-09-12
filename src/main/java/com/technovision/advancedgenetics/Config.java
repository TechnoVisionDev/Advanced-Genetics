package com.technovision.advancedgenetics;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class Config {

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {

        private static final String categoryGeneralSettings = "general_settings";
        private static final String categoryGenes = "genes";
        private static final String categoryCellAnalyzer = "cell_analyzer";
        private static final String categoryDnaExtractor = "dna_extractor";
        private static final String categoryDnaDecrypter = "dna_decrypter";
        private static final String categoryPlasmidInfuser = "plasmid_infuser";
        private static final String categoryBloodPurifier = "blood_purifier";
        private static final String categoryPlasmidInjector = "plasmid_injector";
        private static final String categoryCloningMachine = "cloning_machine";

        public static ModConfigSpec.BooleanValue hardMode;
        public static ModConfigSpec.BooleanValue geneSharing;
        public static ModConfigSpec.DoubleValue basicGeneChance;
        public static ModConfigSpec.IntValue plasmidRequirement;
        public static ModConfigSpec.IntValue overclockSpeed;
        public static ModConfigSpec.IntValue overclockEnergy;

        public static ModConfigSpec.IntValue cellAnalyzerEnergyCapacity;
        public static ModConfigSpec.IntValue cellAnalyzerEnergyPerTick;
        public static ModConfigSpec.IntValue cellAnalyzerTicksPerOperation;
        public static ModConfigSpec.IntValue cellAnalyzerMaxOverclock;
        public static ModConfigSpec.DoubleValue cellAnalyzerSuccessRate;

        public static ModConfigSpec.IntValue dnaExtractorEnergyCapacity;
        public static ModConfigSpec.IntValue dnaExtractorEnergyPerTick;
        public static ModConfigSpec.IntValue dnaExtractorTicksPerOperation;
        public static ModConfigSpec.IntValue dnaExtractorMaxOverclock;
        public static ModConfigSpec.DoubleValue dnaExtractorSuccessRate;

        public static ModConfigSpec.IntValue dnaDecrypterEnergyCapacity;
        public static ModConfigSpec.IntValue dnaDecrypterEnergyPerTick;
        public static ModConfigSpec.IntValue dnaDecrypterTicksPerOperation;
        public static ModConfigSpec.IntValue dnaDecrypterMaxOverclock;
        public static ModConfigSpec.DoubleValue dnaDecrypterSuccessRate;

        public static ModConfigSpec.IntValue plasmidInfuserEnergyCapacity;
        public static ModConfigSpec.IntValue plasmidInfuserEnergyPerTick;
        public static ModConfigSpec.IntValue plasmidInfuserTicksPerOperation;
        public static ModConfigSpec.IntValue plasmidInfuserMaxOverclock;
        public static ModConfigSpec.DoubleValue plasmidInfuserSuccessRate;

        public static ModConfigSpec.IntValue bloodPurifierEnergyCapacity;
        public static ModConfigSpec.IntValue bloodPurifierEnergyPerTick;
        public static ModConfigSpec.IntValue bloodPurifierTicksPerOperation;
        public static ModConfigSpec.IntValue bloodPurifierMaxOverclock;
        public static ModConfigSpec.DoubleValue bloodPurifierSuccessRate;

        public static ModConfigSpec.IntValue plasmidInjectorEnergyCapacity;
        public static ModConfigSpec.IntValue plasmidInjectorEnergyPerTick;
        public static ModConfigSpec.IntValue plasmidInjectorTicksPerOperation;
        public static ModConfigSpec.IntValue plasmidInjectorMaxOverclock;
        public static ModConfigSpec.DoubleValue plasmidInjectorSuccessRate;

        public static ModConfigSpec.IntValue cloningMachineEnergyCapacity;
        public static ModConfigSpec.IntValue cloningMachineEnergyPerTick;
        public static ModConfigSpec.IntValue cloningMachineTicksPerOperation;
        public static ModConfigSpec.IntValue cloningMachineMaxOverclock;
        public static ModConfigSpec.DoubleValue cloningMachineSuccessRate;

        public static HashMap<String, ModConfigSpec.BooleanValue> genes = new HashMap<>();

        public Common(ModConfigSpec.Builder builder) {

            // General Settings
            builder.comment("General Settings").push(categoryGeneralSettings);
            hardMode = builder
                    .comment("Increase difficulty by making plasmids no longer accept basic genes.",
                            "Default: false")
                    .define("hardMode", false);
            geneSharing = builder
                    .comment("Allows players to take the blood of other players and get all of their genes from it.",
                            "Default: false")
                    .define("geneSharing", false);
            basicGeneChance = builder
                    .comment("Percent chance to receive a basic gene upon DNA decryption.",
                            "Default: 50% chance")
                    .defineInRange("basicGeneChance", 0.50, 0.00, 1.00);
            plasmidRequirement = builder
                    .comment("The number of decrypted DNA helices needed to fill a plasmid.",
                            "Default: 24 DNA helices")
                    .defineInRange("plasmidRequirement", 24, 2, 100);
            overclockSpeed = builder
                    .comment("The number of seconds that an overclock item speeds up a machine.",
                            "Default: 2 seconds faster")
                    .defineInRange("overclockSpeed", 2, 0, Integer.MAX_VALUE);
            overclockEnergy = builder
                    .comment("The amount of extra energy per tick that an overclock item uses.",
                            "Default: 5 E/t")
                    .defineInRange("overclockEnergy", 5, 0, Integer.MAX_VALUE);
            builder.pop();

            // Genes
            builder.comment("Genes", "Set any gene to 'false' to disable it").push(categoryGenes);
            genes.put("beelicious", builder.define("beelicious", true));
            genes.put("climb_walls", builder.define("climb_walls", true));
            genes.put("dragons_breath", builder.define("dragons_breath", true));
            genes.put("dragons_health", builder.define("dragons_health", true));
            genes.put("eat_grass", builder.define("eat_grass", true));
            genes.put("emerald_heart", builder.define("emerald_heart", true));
            genes.put("explosive_exit", builder.define("explosive_exit", true));
            genes.put("fireproof", builder.define("fireproof", true));
            genes.put("flight", builder.define("flight", true));
            genes.put("haste", builder.define("haste", true));
            genes.put("infinity", builder.define("infinity", true));
            genes.put("invisibility", builder.define("invisibility", true));
            genes.put("jump_boost", builder.define("jump_boost", true));
            genes.put("keep_inventory", builder.define("keep_inventory", true));
            genes.put("lay_egg", builder.define("lay_egg", true));
            genes.put("luck", builder.define("luck", true));
            genes.put("meaty", builder.define("meaty", true));
            genes.put("milky", builder.define("milky", true));
            genes.put("mob_sight", builder.define("mob_sight", true));
            genes.put("night_vision", builder.define("night_vision", true));
            genes.put("no_fall_damage", builder.define("no_fall_damage", true));
            genes.put("no_hunger", builder.define("no_hunger", true));
            genes.put("poison_immunity", builder.define("poison_immunity", true));
            genes.put("regeneration", builder.define("regeneration", true));
            genes.put("resistance", builder.define("resistance", true));
            genes.put("scare_creepers", builder.define("scare_creepers", true));
            genes.put("scare_skeletons", builder.define("scare_skeletons", true));
            genes.put("shoot_fireballs", builder.define("shoot_fireballs", true));
            genes.put("slimy", builder.define("slimy", true));
            genes.put("speed", builder.define("speed", true));
            genes.put("strength", builder.define("strength", true));
            genes.put("teleport", builder.define("teleport", true));
            genes.put("venom", builder.define("venom", true));
            genes.put("water_breathing", builder.define("water_breathing", true));
            genes.put("web_walking", builder.define("web_walking", true));
            genes.put("wither_resistance", builder.define("wither_resistance", true));
            genes.put("wither_hit", builder.define("wither_hit", true));
            genes.put("wooly", builder.define("wooly", true));
            builder.pop();

            // Cell Analyzer
            builder.comment("Cell Analyzer").push(categoryCellAnalyzer);
            cellAnalyzerEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Cell Analyzer.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            cellAnalyzerEnergyPerTick = builder
                    .comment("Energy consumed per tick when the Cell Analyzer is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            cellAnalyzerTicksPerOperation = builder
                    .comment("Ticks per operation when using the Cell Analyzer.",
                            "Default: 400 ticks")
                    .defineInRange("ticksPerOperation", 400, 20, Integer.MAX_VALUE);
            cellAnalyzerMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the Cell Analyzer.",
                            "Default: 10x overclock")
                    .defineInRange("maxOverclock", 10, 0, Integer.MAX_VALUE);
            cellAnalyzerSuccessRate = builder
                    .comment("Percent chance for the Cell Analyzer to successfully create a cell.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // DNA Extractor
            builder.comment("DNA Extractor").push(categoryDnaExtractor);
            dnaExtractorEnergyCapacity = builder
                    .comment("Maximum energy capacity for the DNA Extractor.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            dnaExtractorEnergyPerTick = builder
                    .comment("Energy consumed per tick when the DNA Extractor is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            dnaExtractorTicksPerOperation = builder
                    .comment("Ticks per operation when using the DNA Extractor.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 20, Integer.MAX_VALUE);
            dnaExtractorMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the DNA Extractor.",
                            "Default: 5x overclock")
                    .defineInRange("maxOverclock", 5, 0, Integer.MAX_VALUE);
            dnaExtractorSuccessRate = builder
                    .comment("Percent chance for the DNA Extractor to successfully extract a DNA helix.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // DNA Decrypter
            builder.comment("DNA Decrypter").push(categoryDnaDecrypter);
            dnaDecrypterEnergyCapacity = builder
                    .comment("Maximum energy capacity for the DNA Decrypter.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            dnaDecrypterEnergyPerTick = builder
                    .comment("Energy consumed per tick when the DNA Decrypter is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            dnaDecrypterTicksPerOperation = builder
                    .comment("Ticks per operation when using the DNA Decrypter.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 20, Integer.MAX_VALUE);
            dnaDecrypterMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the DNA Decrypter.",
                            "Default: 5x overclock")
                    .defineInRange("maxOverclock", 5, 0, Integer.MAX_VALUE);
            dnaDecrypterSuccessRate = builder
                    .comment("Percent chance for the DNA Decrypter to successfully decode a DNA helix.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // Plasmid Infuser
            builder.comment("Plasmid Infuser").push(categoryPlasmidInfuser);
            plasmidInfuserEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Plasmid Infuser.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            plasmidInfuserEnergyPerTick = builder
                    .comment("Energy consumed per tick when the Plasmid Infuser is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            plasmidInfuserTicksPerOperation = builder
                    .comment("Ticks per operation when using the Plasmid Infuser.",
                            "Default: 400 ticks")
                    .defineInRange("ticksPerOperation", 400, 20, Integer.MAX_VALUE);
            plasmidInfuserMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the Plasmid Infuser.",
                            "Default: 10x overclock")
                    .defineInRange("maxOverclock", 10, 0, Integer.MAX_VALUE);
            plasmidInfuserSuccessRate = builder
                    .comment("Percent chance for the Plasmid Infuser to successfully add DNA helix to plasmid.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // Blood Purifier
            builder.comment("Blood Purifier").push(categoryBloodPurifier);
            bloodPurifierEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Blood Purifier.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            bloodPurifierEnergyPerTick = builder
                    .comment("Energy consumed per tick when the Blood Purifier is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            bloodPurifierTicksPerOperation = builder
                    .comment("Ticks per operation when using the Blood Purifier.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 20, Integer.MAX_VALUE);
            bloodPurifierMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the Blood Purifier.",
                            "Default: 5x overclock")
                    .defineInRange("maxOverclock", 5, 0, Integer.MAX_VALUE);
            bloodPurifierSuccessRate = builder
                    .comment("Percent chance for the Blood Purifier to successfully purify blood.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // Plasmid Injector
            builder.comment("Plasmid Injector").push(categoryPlasmidInjector);
            plasmidInjectorEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Plasmid Injector.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            plasmidInjectorEnergyPerTick = builder
                    .comment("Energy consumed per tick when the Plasmid Injector is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            plasmidInjectorTicksPerOperation = builder
                    .comment("Ticks per operation when using the Plasmid Injector.",
                            "Default: 400 ticks")
                    .defineInRange("ticksPerOperation", 400, 20, Integer.MAX_VALUE);
            plasmidInjectorMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the Plasmid Injector.",
                            "Default: 10x overclock")
                    .defineInRange("maxOverclock", 10, 0, Integer.MAX_VALUE);
            plasmidInjectorSuccessRate = builder
                    .comment("Percent chance for the Plasmid Injector to successfully inject a tplasmid into the syringe.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();

            // Cloning Machine
            builder.comment("Cloning Machine").push(categoryCloningMachine);
            cloningMachineEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Cloning Machine.",
                            "Default: 500,000 (500k E)")
                    .defineInRange("energyCapacity", 500000, 0, Integer.MAX_VALUE);
            cloningMachineEnergyPerTick = builder
                    .comment("Energy consumed per tick when the Cloning Machine is processing.",
                            "Default: 1000 E")
                    .defineInRange("energyPerTick", 1000, 0, Integer.MAX_VALUE);
            cloningMachineTicksPerOperation = builder
                    .comment("Ticks per operation when using the Cloning Machine.",
                            "Default: 600 ticks")
                    .defineInRange("ticksPerOperation", 600, 20, Integer.MAX_VALUE);
            cloningMachineMaxOverclock = builder
                    .comment("The max amount of overclock items that can be used on the Cloning Machine.",
                            "Default: 5x overclock")
                    .defineInRange("maxOverclock", 5, 0, Integer.MAX_VALUE);
            cloningMachineSuccessRate = builder
                    .comment("Percent chance for the cloning machine to successfully spawn an entity.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();
        }
    }
}
