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
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    public static boolean isDecoded(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        return tag.getBoolean("decoded");
    }

    public static void setGene(ItemStack cellStack, ItemStack dnaStack) {
        EntityType entityType = ((CellItem)(cellStack.getItem())).getEntityType();
        Genes gene = Entities.findEntityByType(entityType).getRandomGene();
        final NbtCompound tag = dnaStack.getOrCreateNbt();
        tag.putString("gene", gene.toString());
        tag.putBoolean("decoded", false);
    }

    public static void decode(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("decoded", true);
    }
}
