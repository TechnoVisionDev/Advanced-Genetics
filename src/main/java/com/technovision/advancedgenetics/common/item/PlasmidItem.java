package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlasmidItem extends Item {

    public static final int MAX_GENES = Config.Common.plasmidRequiredGenes.get();

    public PlasmidItem() {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB).maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("gene")) {
            Genes gene = Genes.getGeneByItem(stack);
            int count = tag.getInt("count");
            String text = gene.getName();
            if (count < MAX_GENES) {
                text += " " + count + "/" + MAX_GENES;
            }
            tooltip.add(Text.literal(text).formatted(Formatting.GRAY));
        }
    }

    public static boolean canCombine(ItemStack dnaItem, ItemStack plasmidItem) {
        if (!dnaItem.hasNbt()) return false;
        String gene = dnaItem.getNbt().getString("gene");
        if (!plasmidItem.hasNbt()) {
            return !gene.equals(Genes.BASIC.toString());
        }
        return (gene.equals(Genes.BASIC.toString()) || gene.equals(plasmidItem.getNbt().getString("gene")))
                && plasmidItem.getNbt().getInt("count") < MAX_GENES;
    }

    public static void combine(ItemStack dnaItem, ItemStack plasmidItem) {
        String gene = dnaItem.getNbt().getString("gene");
        if (!plasmidItem.hasNbt()) {
            NbtCompound tag = plasmidItem.getOrCreateNbt();
            tag.putString("gene", gene);
            tag.putInt("count", 2);
        } else {
            NbtCompound tag = plasmidItem.getOrCreateNbt();
            int increment = gene.equals(Genes.BASIC.toString()) ? 1 : 2;
            int newTotal = tag.getInt("count") + increment;
            if (newTotal > MAX_GENES) newTotal = MAX_GENES;
            tag.putInt("count", newTotal);
        }
    }
}
