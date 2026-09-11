package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.util.ItemData;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class PlasmidItem extends Item {
    public static final int MAX_GENES = Config.Common.plasmidRequirement.get();

    public PlasmidItem(Properties properties) { super(properties.stacksTo(1)); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = ItemData.read(stack);
        if (tag.contains("gene")) {
            Genes gene = Genes.getGeneByItem(stack);
            int count = tag.getIntOr("count", 0);
            String text = gene.getName();
            if (count < MAX_GENES) text += " " + count + "/" + MAX_GENES;
            tooltip.accept(Component.literal(text).withStyle(ChatFormatting.GRAY));
        }
    }

    public static boolean canCombine(ItemStack dnaItem, ItemStack plasmidItem) {
        CompoundTag dna = ItemData.read(dnaItem);
        if (!dna.contains("gene")) return false;
        String gene = dna.getStringOr("gene", "");
        CompoundTag plasmid = ItemData.read(plasmidItem);
        if (!plasmid.contains("gene")) return !gene.equals(Genes.BASIC.toString());
        return (gene.equals(Genes.BASIC.toString()) || gene.equals(plasmid.getStringOr("gene", "")))
                && plasmid.getIntOr("count", 0) < MAX_GENES;
    }

    public static void combine(ItemStack dnaItem, ItemStack plasmidItem) {
        String gene = ItemData.read(dnaItem).getStringOr("gene", "");
        ItemData.update(plasmidItem, tag -> {
            if (!tag.contains("gene")) {
                tag.putString("gene", gene);
                tag.putInt("count", 2);
            } else {
                int increment = gene.equals(Genes.BASIC.toString()) ? 1 : 2;
                tag.putInt("count", Math.min(MAX_GENES, tag.getIntOr("count", 0) + increment));
            }
        });
    }
}
