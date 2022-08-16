package com.technovision.advancedgenetics.component;

import com.technovision.advancedgenetics.api.component.EntityGeneticsComponent;
import com.technovision.advancedgenetics.api.genetics.Genes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

        // Potion effect genes
        if (totalSeconds % 4 == 0) {
            checkFoodStatus();
            applyPotionEffects();
        }

        // Lay egg gene (every 5 min)
        if (totalSeconds % 300 == 0) {
            player.dropStack(new ItemStack(Items.EGG));
        }
    }

    /**
     * Apply permanent potion effects based on active genes.
     */
    private void applyPotionEffects() {
        if (hasGene(Genes.RESISTANCE)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.HASTE)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.SPEED)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.REGENERATION)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.STRENGTH)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.FIREPROOF)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.NIGHT_VISION)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.JUMP_BOOST)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.WATER_BREATHING)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.INVISIBILITY)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.LUCK)) {
            player.setStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 20*6, 0, false, false, false), player);
        }
    }

    /**
     * Checks if a user is at half hunger and uses the
     * "no-hunger" gene to prevent further loss.
     */
    private void checkFoodStatus() {
        if (player.getHungerManager().getFoodLevel() > 10) return;
        if (hasGene(Genes.NO_HUNGER)) {
            player.getHungerManager().add(1, 0.0f);
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
    public boolean hasGene(Genes gene) {
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

    @Override
    public void removeAllGenes() {
        genes.clear();
    }
}
