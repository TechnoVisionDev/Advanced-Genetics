package com.technovision.advancedgenetics.api.genetics;

import com.technovision.advancedgenetics.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraftforge.common.ForgeConfigSpec;

public enum Genes {
    BASIC("Basic Gene", "9707bf0fd3"),
    CLIMB_WALLS("Climb Walls", "055de4bedf7"),
    DRAGONS_BREATH("Dragon's Breath", "4a3f341dc64aa0b"),
    DRAGONS_HEALTH("Dragon's Health", "35a2097282f5af1"),
    EAT_GRASS("Eat Grass", "73f73be68"),
    EMERALD_HEART("Emerald Heart", "85f608e331505"),
    EXPLOSIVE_EXIT("Explosive Exit", "fdcdf662a4360f"),
    FIREPROOF("Fireproof", "c821b0866"),
    FLIGHT("Flight", "8d39f0"),
    HASTE("Haste", "de3b4"),
    BEELICIOUS("Beelicious", "f2d57d91ad"),
    INFINITY("Infinity", "d0067cad"),
    INVISIBILITY("Invisibility", "d53895c6eb78"),
    JUMP_BOOST("Jump Boost", "25f03ac91f"),
    KEEP_INVENTORY("Keep Inventory", "a2bb0c29020dc3"),
    LAY_EGG("Lay Egg", "cb03a78"),
    LUCK("Luck", "382e"),
    MEATY("Meaty", "f8c6d"),
    MILKY("Milky", "3b107"),
    MOB_SIGHT("Mob Sight", "e10c9a90d"),
    NIGHT_VISION("Night Vision", "4cbe4ae9154d"),
    NO_FALL_DAMAGE("No Fall Damage", "71b2af23b460e1"),
    NO_HUNGER("No Hunger", "301dd96b0"),
    POISON_IMMUNITY("Poison Immunity", "0be142668a3eeca"),
    REGENERATION("Regeneration", "35de2da49504"),
    RESISTANCE("Resistance", "c35070bce1"),
    SCARE_CREEPERS("Scare Creepers", "db3a00b15426a2"),
    SCARE_SKELETONS("Scare Skeletons", "47e8282c9ed2397"),
    SHOOT_FIREBALLS("Shoot Fireballs", "a3d39362dbd6dd0"),
    SLIMY("Slimy", "1bd12"),
    SPEED("Speed", "c372f"),
    STRENGTH("Strength", "63ee3a1b"),
    TELEPORT("Teleport", "e68b3b94"),
    VENOM("Venom", "393b6"),
    WATER_BREATHING("Water Breathing", "4fa023594764255"),
    WEB_WALKING("Web Walking", "c58cb03639f"),
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

    public boolean isEnabled() {
        if (this == Genes.BASIC) return false;
        ForgeConfigSpec.BooleanValue value = Config.Common.genes.get(this.toString().toLowerCase());
        if (value == null) return false;
        Boolean result = value.get();
        if (result == null) return false;
        return result;
    }

    public static Genes getGeneByItem(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        String geneName = tag.getString("gene");
        return Genes.valueOf(geneName);
    }
}
