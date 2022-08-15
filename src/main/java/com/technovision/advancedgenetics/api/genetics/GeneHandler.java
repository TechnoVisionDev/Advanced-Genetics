package com.technovision.advancedgenetics.api.genetics;

import com.technovision.advancedgenetics.common.item.CellItem;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class GeneHandler {

    public static Genes getGene(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        String geneName = tag.getString("gene");
        return Genes.valueOf(geneName);
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
