package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3"),
    EAT_GRASS("Eat Grass", "73f73be68"),
    EXPLOSIVE_EXIT("Explosive Exit", "fdcdf662a4360f"),
    LAY_EGG("Lay Egg", "cb03a78"),
    MEATY("Meaty", "f8c6d"),
    MILKY("Milky", "3b107"),
    NO_FALL_DAMAGE("No Fall Damage", "71b2af23b460e1"),
    RESISTANCE("Resistance", "c35070bce1"),
    WOOLY("Wooly", "a7ae3");

    private final String name;
    private final String encryptedName;

    Genes(String name, String encryptedName) {
        this.name = name;
        this.encryptedName = encryptedName;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedName() {
        return encryptedName;
    }

    public static Genes getGeneByItem(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        String geneName = tag.getString("gene");
        return Genes.valueOf(geneName);
    }
}
