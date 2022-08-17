package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3"),
    EAT_GRASS("Eat Grass", "73f73be68"),
    EMERALD_HEART("Emerald Heart", "85f608e331505"),
    EXPLOSIVE_EXIT("Explosive Exit", "fdcdf662a4360f"),
    FIREPROOF("Fireproof", "c821b0866"),
    HASTE("Haste", "de3b4"),
    INVISIBILITY("Invisibility", "d53895c6eb78"),
    JUMP_BOOST("Jump Boost", "25f03ac91f"),
    KEEP_INVENTORY("Keep Inventory", "a2bb0c29020dc3"),
    LAY_EGG("Lay Egg", "cb03a78"),
    LUCK("Luck", "382e"),
    MEATY("Meaty", "f8c6d"),
    MILKY("Milky", "3b107"),
    NIGHT_VISION("Night Vision", "4cbe4ae9154d"),
    NO_FALL_DAMAGE("No Fall Damage", "71b2af23b460e1"),
    NO_HUNGER("No Hunger", "301dd96b0"),
    POISON_IMMUNITY("Poison Immunity", "0be142668a3eeca"),
    REGENERATION("Regeneration", "35de2da49504"),
    RESISTANCE("Resistance", "c35070bce1"),
    SPEED("Speed", "c372f"),
    SHOOT_FIREBALLS("Shoot Fireballs", "a3d39362dbd6dd0"),
    STRENGTH("Strength", "63ee3a1b"),
    WATER_BREATHING("Water Breathing", "4fa023594764255"),
    WITHER_RESISTANCE("Wither Resistance", "15e26cc9132777ca"),
    WITHER_HIT("Wither Hit", "3d6f11df43"),
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
