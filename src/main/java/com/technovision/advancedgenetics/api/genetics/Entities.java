package com.technovision.advancedgenetics.api.genetics;

import com.technovision.advancedgenetics.Config;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.technovision.advancedgenetics.api.genetics.Genes.*;

public enum Entities {
    BAT("bat", EntityTypes.BAT, "524020", List.of(NIGHT_VISION)),
    BEE("bee", EntityTypes.BEE, "edc343", List.of(BEELICIOUS)),
    BLAZE("blaze", EntityTypes.BLAZE, "ffd528", List.of(FIREPROOF, SHOOT_FIREBALLS)),
    CAT("cat", EntityTypes.CAT, "747474", List.of(SPEED, SCARE_CREEPERS)),
    COW("cow", EntityTypes.COW, "443626", List.of(MILKY, EAT_GRASS)),
    CHICKEN("chicken", EntityTypes.CHICKEN, "e2e2e2", List.of(LAY_EGG, NO_FALL_DAMAGE)),
    CAVE_SPIDER("cave_spider", EntityTypes.CAVE_SPIDER, "546870", List.of(NIGHT_VISION, VENOM, WEB_WALKING)),
    CREEPER("creeper", EntityTypes.CREEPER, "65d152", List.of(EXPLOSIVE_EXIT)),
    DROWNED("drowned", EntityTypes.DROWNED, "4d9280", List.of(RESISTANCE, WATER_BREATHING)),
    ENDERMAN("enderman", EntityTypes.ENDERMAN, "e079fa", List.of(TELEPORT)),
    ENDER_DRAGON("ender_dragon", EntityTypes.ENDER_DRAGON, "e079fa", List.of(FLIGHT, DRAGONS_BREATH, DRAGONS_HEALTH)),
    FROG("frog", EntityTypes.FROG, "63902e", List.of(POISON_IMMUNITY)),
    GUARDIAN("guardian", EntityTypes.GUARDIAN, "83a59c", List.of(WATER_BREATHING, MOB_SIGHT)),
    GHAST("ghast", EntityTypes.GHAST, "f6f6f6", List.of(SHOOT_FIREBALLS)),
    HORSE("horse", EntityTypes.HORSE, "946734", List.of(JUMP_BOOST)),
    HUSK("husk", EntityTypes.HUSK, "7a6849", List.of(RESISTANCE)),
    IRON_GOLEM("iron_golem", EntityTypes.IRON_GOLEM, "c5cecd", List.of(REGENERATION, RESISTANCE)),
    MAGMA_CUBE("magma_cube", EntityTypes.MAGMA_CUBE, "d3550e", List.of(FIREPROOF)),
    MOOSHROOM("mooshroom", EntityTypes.MOOSHROOM, "a81012", List.of(MILKY, EAT_GRASS)),
    OCELOT("ocelot", EntityTypes.OCELOT, "f2d19a", List.of(SPEED, SCARE_CREEPERS)),
    PANDA("panda", EntityTypes.PANDA, "f6f6f6", List.of(NO_HUNGER, MEATY)),
    PHANTOM("phantom", EntityTypes.PHANTOM, "415189", List.of(INVISIBILITY, NIGHT_VISION, MOB_SIGHT)),
    PIG("pig", EntityTypes.PIG, "f19e98", List.of(MEATY)),
    POLAR_BEAR("polar_bear", EntityTypes.POLAR_BEAR, "f6f6f6", List.of(STRENGTH, MEATY)),
    RABBIT("rabbit", EntityTypes.RABBIT, "ada498", List.of(JUMP_BOOST, SPEED, LUCK)),
    SHEEP("sheep", EntityTypes.SHEEP, "ececec", List.of(EAT_GRASS, WOOLY)),
    SHULKER("shulker", EntityTypes.SHULKER, "976997", List.of(RESISTANCE, REGENERATION)),
    SQUID("squid", EntityTypes.SQUID, "546d80", List.of(WATER_BREATHING)),
    SPIDER("spider", EntityTypes.SPIDER, "4f453c", List.of(NIGHT_VISION, CLIMB_WALLS)),
    SKELETON("skeleton", EntityTypes.SKELETON, "bcbcbc", List.of(INFINITY)),
    STRAY("stray", EntityTypes.STRAY, "9caeac", List.of(INFINITY)),
    SLIME("slime", EntityTypes.SLIME, "7bce6a", List.of(SLIMY)),
    SILVERFISH("silverfish", EntityTypes.SILVERFISH, "9caeac", List.of(HASTE)),
    VILLAGER("villager", EntityTypes.VILLAGER, "be886c", List.of(EMERALD_HEART)),
    WOLF("wolf", EntityTypes.WOLF, "dddadb", List.of(NO_HUNGER, SCARE_SKELETONS)),
    WARDEN("warden", EntityTypes.WARDEN, "006668", List.of(KEEP_INVENTORY, NIGHT_VISION, STRENGTH)),
    WITHER_SKELETON("wither_skeleton", EntityTypes.WITHER_SKELETON, "343434", List.of(WITHER_HIT)),
    WITHER("wither", EntityTypes.WITHER, "343434", List.of(WITHER_RESISTANCE, WITHER_HIT)),
    ZOMBIE("zombie", EntityTypes.ZOMBIE, "3e692d", List.of(RESISTANCE));

    private static final Random rand = new Random();
    private final String name;
    private final EntityType<?> type;
    private final int color;
    private final List<Genes> genes;

    Entities(String name, EntityType<?> type, String color, List<Genes> genes) {
        this.name = name;
        this.type = type;
        this.color = (int) Long.parseLong(color, 16);
        this.genes = genes;
    }

    public String getName() {
        return name;
    }

    public EntityType<?> getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public List<Genes> getGenes() { return genes; }

    public Genes getRandomGene() {
        if (genes.isEmpty() || rand.nextDouble() <= Config.Common.basicGeneChance.get()) return Genes.BASIC;
        List<Genes> enabledGenes = new ArrayList<>();
        for (Genes gene : getGenes()) {
            if (gene.isEnabled()) {
                enabledGenes.add(gene);
            }
        }
        if (enabledGenes.isEmpty()) return Genes.BASIC;
        int index = rand.nextInt(enabledGenes.size());
        return enabledGenes.get(index);
    }

    public static Entities findEntityByType(EntityType<?> type){
        for(Entities entity : values()){
            if( entity.getType() == type){
                return entity;
            }
        }
        return null;
    }
}
