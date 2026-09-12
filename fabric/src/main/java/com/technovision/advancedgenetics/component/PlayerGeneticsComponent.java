package com.technovision.advancedgenetics.component;

import com.technovision.advancedgenetics.api.component.EntityGeneticsComponent;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.sounds.SoundEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerGeneticsComponent implements EntityGeneticsComponent {

    private final Map<String, Genes> genes = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final Player player;
    private int tickCounter;
    private long totalSeconds;

    public PlayerGeneticsComponent(Player player) {
        this.player = player;
        this.tickCounter = 0;
        this.totalSeconds = 0;
    }

    @Override
    public void readData(ValueInput input) {
        genes.clear();
        CompoundTag genesTag = input.read("genes", CompoundTag.CODEC).orElseGet(CompoundTag::new);
        for (String geneName : genesTag.keySet()) {
            Genes gene = Genes.valueOf(geneName);
            genes.put(geneName, gene);
        }
    }

    @Override
    public void writeData(ValueOutput output) {
        CompoundTag genesTag = new CompoundTag();
        for (Genes gene : genes.values()) {
            genesTag.putString(gene.toString(), gene.getName());
        }
        output.store("genes", CompoundTag.CODEC, genesTag);
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
            checkFlightStatus();
            applyPotionEffects();
        }

        // Lay egg gene (every 5 min)
        if (totalSeconds % 300 == 0) {
            if (hasGene(Genes.LAY_EGG)) {
                player.spawnAtLocation((ServerLevel) player.level(), new ItemStack(Items.EGG));
                player.playSound(SoundEvents.CHICKEN_EGG, 1.0f, 1.0f);
            }
        }
    }

    /**
     * Apply permanent potion effects based on active genes.
     */
    private void applyPotionEffects() {
        if (hasGene(Genes.RESISTANCE)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.HASTE)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.HASTE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.SPEED)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.SPEED, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.REGENERATION)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.REGENERATION, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.STRENGTH)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.STRENGTH, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.FIREPROOF)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.NIGHT_VISION)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.JUMP_BOOST)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.WATER_BREATHING)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.INVISIBILITY)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20*6, 0, false, false, false), player);
        }
        if (hasGene(Genes.LUCK)) {
            player.forceAddEffect(new MobEffectInstance(MobEffects.LUCK, 20*6, 0, false, false, false), player);
        }
    }

    /**
     * Checks if a user is at half hunger and uses the
     * "no-hunger" gene to prevent further loss.
     */
    private void checkFoodStatus() {
        if (player.getFoodData().getFoodLevel() > 10) return;
        if (hasGene(Genes.NO_HUNGER)) {
            player.getFoodData().eat(1, 0.0f);
        }
    }

    /**
     * Checks if a user has the flight gene and enables/disables flight.
     */
    private void checkFlightStatus() {
        if (player.isCreative()) return;
        if (hasGene(Genes.FLIGHT)) {
            player.getAbilities().mayfly = true;
        } else {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
        }
        player.onUpdateAbilities();
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
        if (!gene.isEnabled()) return false;
        return genes.containsKey(gene.toString());
    }

    @Override
    public void addGene(Genes gene) {
        genes.put(gene.toString(), gene);
        ComponentRegistry.PLAYER_GENETICS.sync(player);
    }

    @Override
    public void addGenes(List<Genes> genesList) {
        for (Genes gene : genesList) {
            genes.put(gene.toString(), gene);
        }
        ComponentRegistry.PLAYER_GENETICS.sync(player);
    }

    @Override
    public void removeGene(Genes gene) {
        genes.remove(gene.toString());
        ComponentRegistry.PLAYER_GENETICS.sync(player);
    }

    @Override
    public void removeAllGenes() {
        genes.clear();
        ComponentRegistry.PLAYER_GENETICS.sync(player);
    }

    @Override
    public void addCooldown(String key, long seconds) {
        cooldowns.put(key, System.currentTimeMillis() + (1000 * seconds));
    }

    @Override
    public boolean isOnCooldown(String key) {
        Long cooldown = cooldowns.get(key);
        if (cooldown == null) return false;
        return (System.currentTimeMillis() < cooldown);
    }
}
