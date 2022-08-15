package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
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
        super(new FabricItemSettings().group(AdvancedGenetics.TAB));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("gene")) {
            String text = String.format("%s (%d/%d)", tag.getString("gene"), tag.getInt("count"), MAX_GENES);
            tooltip.add(Text.literal(text.formatted(Formatting.GRAY)));
        }
    }

    public static void setGene(ItemStack cellStack, ItemStack dnaStack) {
        // TODO: Implement
        EntityType entityType = ((CellItem)(cellStack.getItem())).getEntityType();
        Genes gene = Entities.findEntityByType(entityType).getRandomGene();
        final NbtCompound tag = dnaStack.getOrCreateNbt();
        tag.putString("gene", gene.toString());
        tag.putInt("count", 0);
    }

    public static void addCount(ItemStack plasmid, Genes gene) {
        NbtCompound tag = plasmid.getOrCreateNbt();
        if (gene == Genes.BASIC) {
            tag.putInt("count", tag.getInt("count") + 1);
        } else {
            int newTotal = tag.getInt("count") + 2;
            if (newTotal > MAX_GENES) newTotal = MAX_GENES;
            tag.putInt("count", tag.getInt("count") + 2);
        }
    }
}
