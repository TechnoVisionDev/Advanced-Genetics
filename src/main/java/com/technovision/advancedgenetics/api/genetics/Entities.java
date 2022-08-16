package com.technovision.advancedgenetics.api.genetics;

import com.technovision.advancedgenetics.Config;
import net.minecraft.entity.EntityType;

import java.util.List;
import java.util.Random;

import static com.technovision.advancedgenetics.api.genetics.Genes.*;

public enum Entities {
    COW("cow", EntityType.COW, "443626", List.of(MILKY, EAT_GRASS)),
    PIG("pig", EntityType.PIG, "f19e98", List.of(MEATY)),
    CHICKEN("chicken", EntityType.CHICKEN, "e2e2e2", List.of(LAY_EGG, NO_FALL_DAMAGE)),
    SHEEP("sheep", EntityType.SHEEP, "dedede", List.of(EAT_GRASS, WOOLY)),
    SQUID("squid", EntityType.SQUID, "546d80", List.of(WATER_BREATHING)),
    HORSE("horse", EntityType.HORSE, "b98968", List.of(JUMP_BOOST)),
    MOOSHROOM("mooshroom", EntityType.MOOSHROOM, "a81012", List.of(MILKY, EAT_GRASS)),
    SPIDER("spider", EntityType.SPIDER, "4f453c", List.of(NIGHT_VISION)),
    CAVE_SPIDER("cave_spider", EntityType.CAVE_SPIDER, "546870", List.of(NIGHT_VISION, POISON_IMMUNITY)),
    CREEPER("creeper", EntityType.CREEPER, "65d152", List.of(EXPLOSIVE_EXIT)),
    ZOMBIE("zombie", EntityType.ZOMBIE, "3e692d", List.of(RESISTANCE)),
    DROWNED("drowned", EntityType.DROWNED, "4d9280", List.of(RESISTANCE, WATER_BREATHING)),
    HUSK("husk", EntityType.HUSK, "7a6849", List.of(RESISTANCE)),
    SKELETON("skeleton", EntityType.SKELETON, "bcbcbc", List.of()),
    WITHER_SKELETON("wither_skeleton", EntityType.WITHER_SKELETON, "343434", List.of(WITHER_HIT)),
    STRAY("stray", EntityType.STRAY, "9caeac", List.of());

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
        int index = rand.nextInt(genes.size());
        return genes.get(index);
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
