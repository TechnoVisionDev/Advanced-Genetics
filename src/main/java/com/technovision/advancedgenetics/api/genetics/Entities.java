package com.technovision.advancedgenetics.api.genetics;

import com.technovision.advancedgenetics.Config;
import net.minecraft.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.technovision.advancedgenetics.api.genetics.Genes.*;

public enum Entities {
    BAT("bat", EntityType.BAT, "524020", List.of(NIGHT_VISION)),
    BEE("bee", EntityType.BEE, "edc343", List.of()),
    BLAZE("blaze", EntityType.BLAZE, "ffd528", List.of(FIREPROOF, SHOOT_FIREBALLS)),
    CAT("cat", EntityType.CAT, "747474", List.of(SPEED, SCARE_CREEPERS)),
    COW("cow", EntityType.COW, "443626", List.of(MILKY, EAT_GRASS)),
    CHICKEN("chicken", EntityType.CHICKEN, "e2e2e2", List.of(LAY_EGG, NO_FALL_DAMAGE)),
    CAVE_SPIDER("cave_spider", EntityType.CAVE_SPIDER, "546870", List.of(NIGHT_VISION, VENOM, WEB_WALKING)),
    CREEPER("creeper", EntityType.CREEPER, "65d152", List.of(EXPLOSIVE_EXIT)),
    DROWNED("drowned", EntityType.DROWNED, "4d9280", List.of(RESISTANCE, WATER_BREATHING)),
    ENDERMAN("enderman", EntityType.ENDERMAN, "e079fa", List.of(TELEPORT)),
    ENDER_DRAGON("ender_dragon", EntityType.ENDER_DRAGON, "e079fa", List.of(FLIGHT, DRAGONS_BREATH, DRAGONS_HEALTH)),
    FROG("frog", EntityType.FROG, "63902e", List.of(POISON_IMMUNITY)),
    GUARDIAN("guardian", EntityType.GUARDIAN, "83a59c", List.of(WATER_BREATHING, MOB_SIGHT)),
    GHAST("ghast", EntityType.GHAST, "f6f6f6", List.of(SHOOT_FIREBALLS)),
    HORSE("horse", EntityType.HORSE, "946734", List.of(JUMP_BOOST)),
    HUSK("husk", EntityType.HUSK, "7a6849", List.of(RESISTANCE)),
    IRON_GOLEM("iron_golem", EntityType.IRON_GOLEM, "e2dbd6", List.of(REGENERATION, RESISTANCE)),
    MAGMA_CUBE("magma_cube", EntityType.MAGMA_CUBE, "d3550e", List.of(FIREPROOF)),
    MOOSHROOM("mooshroom", EntityType.MOOSHROOM, "a81012", List.of(MILKY, EAT_GRASS)),
    OCELOT("ocelot", EntityType.OCELOT, "f2d19a", List.of(SPEED, SCARE_CREEPERS)),
    PANDA("panda", EntityType.PANDA, "f6f6f6", List.of(NO_HUNGER, MEATY)),
    PHANTOM("phantom", EntityType.PHANTOM, "415189", List.of(INVISIBILITY, NIGHT_VISION, MOB_SIGHT)),
    PIG("pig", EntityType.PIG, "f19e98", List.of(MEATY)),
    POLAR_BEAR("polar_bear", EntityType.POLAR_BEAR, "f6f6f6", List.of(STRENGTH, MEATY)),
    RABBIT("rabbit", EntityType.RABBIT, "ada498", List.of(JUMP_BOOST, SPEED, LUCK)),
    SHEEP("sheep", EntityType.SHEEP, "dedede", List.of(EAT_GRASS, WOOLY)),
    SHULKER("shulker", EntityType.SHULKER, "976997", List.of(RESISTANCE)),
    SQUID("squid", EntityType.SQUID, "546d80", List.of(WATER_BREATHING)),
    SPIDER("spider", EntityType.SPIDER, "4f453c", List.of(NIGHT_VISION, CLIMB_WALLS)),
    SKELETON("skeleton", EntityType.SKELETON, "bcbcbc", List.of(INFINITY)),
    STRAY("stray", EntityType.STRAY, "9caeac", List.of(INFINITY)),
    SLIME("slime", EntityType.SLIME, "7bce6a", List.of(SLIMY)),
    SILVERFISH("silverfish", EntityType.SILVERFISH, "9caeac", List.of(HASTE)),
    VILLAGER("villager", EntityType.VILLAGER, "be886c", List.of(EMERALD_HEART)),
    WOLF("wolf", EntityType.WOLF, "dddadb", List.of(NO_HUNGER, SCARE_SKELETONS)),
    WARDEN("warden", EntityType.WARDEN, "006668", List.of(KEEP_INVENTORY, NIGHT_VISION, STRENGTH)),
    WITHER_SKELETON("wither_skeleton", EntityType.WITHER_SKELETON, "343434", List.of(WITHER_HIT)),
    WITHER("wither", EntityType.WITHER, "343434", List.of(WITHER_RESISTANCE, WITHER_HIT)),
    ZOMBIE("zombie", EntityType.ZOMBIE, "3e692d", List.of(RESISTANCE));

    private static final Random rand = new Random();
    private final String name;
    private final EntityType type;
    private final int color;
    private final List<Genes> genes;

    Entities(String name, EntityType type, String color, List<Genes> genes) {
        this.name = name;
        this.type = type;
        this.color = (int) Long.parseLong(color, 16);
        this.genes = genes;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
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

    public static Entities findEntityByType(EntityType type){
        for(Entities entity : values()){
            if( entity.getType() == type){
                return entity;
            }
        }
        return null;
    }
}
