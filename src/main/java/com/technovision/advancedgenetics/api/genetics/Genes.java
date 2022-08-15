package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.util.Formatting;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3", Formatting.GRAY),
    MILKY("Milky", "3b107", "d1d1cf"),
    MEATY("Meaty", "f8c6d", "f7adba");

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
}
