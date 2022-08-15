package com.technovision.advancedgenetics;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {

        private static final String categorySettings = "settings";
        private static final String categoryCellAnalyzer = "cell_analyzer";
        private static final String categoryDnaExtractor = "dna_extractor";
        private static final String categoryDnaDecrypter = "dna_decrypter";

        public static ForgeConfigSpec.DoubleValue basicGeneChance;

        public static ForgeConfigSpec.IntValue cellAnalyzerEnergyCapacity;
        public static ForgeConfigSpec.IntValue cellAnalyzerEnergyPerTick;
        public static ForgeConfigSpec.IntValue cellAnalyzerTicksPerOperation;
        public static ForgeConfigSpec.DoubleValue cellAnalyzerSuccessRate;

        public static ForgeConfigSpec.IntValue dnaExtractorEnergyCapacity;
        public static ForgeConfigSpec.IntValue dnaExtractorEnergyPerTick;
        public static ForgeConfigSpec.IntValue dnaExtractorTicksPerOperation;
        public static ForgeConfigSpec.DoubleValue dnaExtractorSuccessRate;

        public static ForgeConfigSpec.IntValue dnaDecrypterEnergyCapacity;
        public static ForgeConfigSpec.IntValue dnaDecrypterEnergyPerTick;
        public static ForgeConfigSpec.IntValue dnaDecrypterTicksPerOperation;
        public static ForgeConfigSpec.DoubleValue dnaDecrypterSuccessRate;

        public Common(ForgeConfigSpec.Builder builder) {

            // General Settings
            builder.comment("Settings").push(categorySettings);
            basicGeneChance = builder
                    .comment("Percent chance to receive a basic gene upon DNA decryption.",
                            "Default: 50% chance")
                    .defineInRange("basicGeneChance", 0.50, 0.00, 1.00);
            builder.pop();

            // Cell Analyzer
            builder.comment("Cell Analyzer").push(categoryCellAnalyzer);
            cellAnalyzerEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Cell Analyzer.",
                            "Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            cellAnalyzerEnergyPerTick = builder
                    .comment("Energy consumed per tick when Cell Analyzer is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            cellAnalyzerTicksPerOperation = builder
                    .comment("Ticks per operation when using the Cell Analyzer.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 1, Integer.MAX_VALUE);
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
                    .comment("Energy consumed per tick when DNA Extractor is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            dnaExtractorTicksPerOperation = builder
                    .comment("Ticks per operation when using the DNA Extractor.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 1, Integer.MAX_VALUE);
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
                    .comment("Energy consumed per tick when DNA Decrypter is processing.",
                            "Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            dnaDecrypterTicksPerOperation = builder
                    .comment("Ticks per operation when using the DNA Decrypter.",
                            "Default: 200 ticks")
                    .defineInRange("ticksPerOperation", 200, 1, Integer.MAX_VALUE);
            dnaDecrypterSuccessRate = builder
                    .comment("Percent chance for the DNA Decrypter to successfully decode a DNA helix.",
                            "Default: 100% success rate")
                    .defineInRange("successRate", 1.00, 0.00, 1.00);
            builder.pop();
        }
    }
}
