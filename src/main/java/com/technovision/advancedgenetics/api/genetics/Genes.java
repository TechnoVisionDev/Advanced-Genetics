package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.util.Formatting;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3", Formatting.GRAY),
    MILKY("Milky", "3b107", Formatting.WHITE),
    MEATY("Meaty", "f8c6d", Formatting.RED);

    private final String name;
    private final String encryptedName;
    private final Formatting color;

    Genes(String name, String encryptedName, Formatting color) {
        this.name = name;
        this.encryptedName = encryptedName;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedName() {
        return encryptedName;
    }

    public Formatting getColor() {
        return color;
    }
}
