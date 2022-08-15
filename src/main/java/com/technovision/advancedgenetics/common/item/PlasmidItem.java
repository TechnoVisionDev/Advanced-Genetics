package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlasmidItem extends Item {

    public static final int MAX_GENES = 24;

    public PlasmidItem() {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB).maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("gene")) {
            Genes gene = Genes.getGeneByItem(stack);
            String text = String.format("%s %d/%d", gene.getName(), tag.getInt("count"), MAX_GENES);
            tooltip.add(Text.literal(text).formatted(Formatting.GRAY));
        }
    }

    public static boolean canCombine(ItemStack dnaItem, ItemStack plasmidItem) {
        if (!dnaItem.hasNbt()) return false;
        String gene = dnaItem.getNbt().getString("gene");
        if (!plasmidItem.hasNbt()) {
            return !gene.equals(Genes.BASIC.toString());
        } else if (gene.equals(Genes.BASIC.toString())) {
            return true;
        }
        return gene.equals(plasmidItem.getNbt().getString("gene"))
                && plasmidItem.getNbt().getInt("count") < MAX_GENES;
    }

    public static void combine(ItemStack dnaItem, ItemStack plasmidItem) {
        String gene = dnaItem.getNbt().getString("gene");
        if (!plasmidItem.hasNbt()) {
            NbtCompound tag = plasmidItem.getOrCreateNbt();
            tag.putString("gene", gene);
            tag.putInt("count", 2);
            return;
        }
        NbtCompound tag = plasmidItem.getOrCreateNbt();
        if (gene.equals(Genes.BASIC.toString())) {
            tag.putInt("count", tag.getInt("count") + 1);
        } else {
            int newTotal = tag.getInt("count") + 2;
            if (newTotal > MAX_GENES) newTotal = MAX_GENES;
            tag.putInt("count", newTotal);
        }
    }
}
