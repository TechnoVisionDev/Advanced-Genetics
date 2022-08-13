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

        private static final String categoryCellAnalyzer = "cell_analyzer";

        public static ForgeConfigSpec.IntValue cellAnalyzerEnergyCapacity;
        public static ForgeConfigSpec.IntValue cellAnalyzerEnergyPerTick;
        public static ForgeConfigSpec.IntValue cellAnalyzerTicksPerOperation;

        public Common(ForgeConfigSpec.Builder builder) {

            builder.comment("Cell Analyzer").push(categoryCellAnalyzer);
            cellAnalyzerEnergyCapacity = builder
                    .comment("Maximum energy capacity for the Cell Analyzer.")
                    .comment("Default: 20,000 (20k E)")
                    .defineInRange("energyCapacity", 20000, 0, Integer.MAX_VALUE);
            cellAnalyzerEnergyPerTick = builder
                    .comment("Energy consumed per tick when Cell Analyzer is processing.")
                    .comment("Default: 20 E")
                    .defineInRange("energyPerTick", 20, 0, Integer.MAX_VALUE);
            cellAnalyzerTicksPerOperation = builder
                    .comment("Ticks per operation when using the Cell Analyzer.")
                    .comment("Default: 50 ticks")
                    .defineInRange("ticksPerOperation", 50, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}
