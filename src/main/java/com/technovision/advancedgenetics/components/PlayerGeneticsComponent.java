package com.technovision.advancedgenetics.components;

import com.technovision.advancedgenetics.api.components.EntityGeneticsComponent;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerGeneticsComponent implements EntityGeneticsComponent {

    private final Map<String, Genes> genes = new HashMap<>();
    private final PlayerEntity player;
    private int tickCounter;
    private long totalSeconds;

    public PlayerGeneticsComponent(PlayerEntity player) {
        this.player = player;
        this.tickCounter = 0;
        this.totalSeconds = 0;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound genesTag = tag.getCompound("genes");
        for (String geneName : genesTag.getKeys()) {
            Genes gene = Genes.valueOf(geneName);
            genes.put(geneName, gene);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound genesTag = new NbtCompound();
        for (Genes gene : genes.values()) {
            genesTag.putString(gene.toString(), gene.getName());
        }
        tag.put("genes", genesTag);
    }

    @Override
    public void serverTick() {
        // Handler timers
        tickCounter++;
        if (tickCounter < 20) return;
        totalSeconds++;
        tickCounter = 0;
        if (totalSeconds == Long.MAX_VALUE) totalSeconds = 0;

        // Lay egg gene (every 5 min)
        if (totalSeconds % 300 == 0) {
            player.dropStack(new ItemStack(Items.EGG));
        }
    }

    @Override
    public int geneCount() {
        return genes.size();
    }

    @Override
    public List<Genes> getGenes() {
        return genes.values().stream().toList();
    }

    @Override
    public Map<String, Genes> getGenesMap() {
        return genes;
    }

    @Override
    public boolean containsGene(Genes gene) {
        return genes.containsKey(gene.toString());
    }

    @Override
    public void addGene(Genes gene) {
        genes.put(gene.toString(), gene);
    }

    @Override
    public void addGenes(List<Genes> genesList) {
        for (Genes gene : genesList) {
            genes.put(gene.toString(), gene);
        }
    }

    @Override
    public void removeGene(Genes gene) {
        genes.remove(gene.toString());
    }
}
