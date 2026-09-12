package com.technovision.advancedgenetics.api.component;

import com.technovision.advancedgenetics.api.genetics.Genes;

import java.util.List;
import java.util.Map;

public interface EntityGeneticsComponent extends net.neoforged.neoforge.common.util.ValueIOSerializable {

    void readData(net.minecraft.world.level.storage.ValueInput input);
    void writeData(net.minecraft.world.level.storage.ValueOutput output);
    void serverTick();
    default void serialize(net.minecraft.world.level.storage.ValueOutput output) { writeData(output); }
    default void deserialize(net.minecraft.world.level.storage.ValueInput input) { readData(input); }

    int geneCount();

    List<Genes> getGenes();

    Map<String, Genes> getGenesMap();

    boolean hasGene(Genes gene);

    void addGene(Genes gene);

    void addGenes(List<Genes> genes);

    void removeGene(Genes gene);

    void removeAllGenes();

    void addCooldown(String key, long seconds);

    boolean isOnCooldown(String key);
}
