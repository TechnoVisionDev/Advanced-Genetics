package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.technovision.advancedgenetics.api.genetics.DnaHandler.*;

public class DnaItem extends Item {

    public DnaItem() {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!stack.hasNbt()) return;
        Genes gene = Genes.getGeneByItem(stack);
        MutableText geneName;
        int color;
        if (isDecoded(stack)) {
            geneName = Text.literal(gene.getName());
            color = gene.getColor();
        } else {
            geneName = Text.literal(gene.getEncryptedName());
            color = Formatting.GRAY.getColorValue();
        }
        tooltip.add(geneName.setStyle(Style.EMPTY.withColor(color)));
    }
}
