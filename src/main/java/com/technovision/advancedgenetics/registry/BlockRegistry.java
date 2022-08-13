package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static final CellAnalyzerBlock CELL_ANALYZER = new CellAnalyzerBlock();

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);
    }
}
