package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3", Formatting.GRAY),
    EAT_GRASS("Eat Grass", "73f73be68", "59ba56"),
    EXPLOSIVE_EXIT("Explosive Exit", "fdcdf662a4360f", Formatting.RED),
    LAY_EGG("Lay Egg", "cb03a78", Formatting.YELLOW),
    MEATY("Meaty", "f8c6d", "f7adba"),
    MILKY("Milky", "3b107", Formatting.GRAY),
    NO_FALL_DAMAGE("No Fall Damage", "71b2af23b460e1", Formatting.BLUE),
    RESISTANCE("Resistance", "c35070bce1", Formatting.LIGHT_PURPLE),
    WOOLY("Wooly", "a7ae3", Formatting.GRAY);

    private final String name;
    private final String encryptedName;
    private final int color;

    Genes(String name, String encryptedName, String color) {
        this.name = name;
        this.encryptedName = encryptedName;
        this.color = (int) Long.parseLong(color, 16);
    }

    Genes(String name, String encryptedName, Formatting color) {
        this.name = name;
        this.encryptedName = encryptedName;
        this.color = color.getColorValue();
    }

    public String getName() {
        return name;
    }

    public String getEncryptedName() {
        return encryptedName;
    }

    public int getColor() {
        return color;
    }

    public static Genes getGeneByItem(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        String geneName = tag.getString("gene");
        return Genes.valueOf(geneName);
    }
}
