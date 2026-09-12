package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Stores data to be passed to DamageReceivedEvent.
 *
 * @author TechnoVision
 */
public class DamageReceivedEvent {

    private final ServerPlayer player;
    private final DamageSource source;
    private final float amount;

    public DamageReceivedEvent(ServerPlayer player, DamageSource source, float amount) {
        this.player = player;
        this.source = source;
        this.amount = amount;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    /**
     * Event that fires every time a player receives damage on the server side.
     *
     * @param event the event details.
     * @return true if event is canceled and player takes no damage, otherwise false.
     */
    public static boolean onDamageReceivedEvent(DamageReceivedEvent event) {
        ServerPlayer player = event.getPlayer();
        DamageSource source = event.getSource();
        PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(player);

        // Handles "No Fall Damage" gene
        if (source.is(DamageTypeTags.IS_FALL) && component.hasGene(Genes.NO_FALL_DAMAGE)) {
            return true;
        }
        // Handles "Poison Immunity" gene
        if (player.hasEffect(MobEffects.POISON) && component.hasGene(Genes.POISON_IMMUNITY)) {
            player.removeEffect(MobEffects.POISON);
            return true;
        }
        // Handles "Wither Resistance" gene
        if (player.hasEffect(MobEffects.WITHER) && component.hasGene(Genes.WITHER_RESISTANCE)) {
            player.removeEffect(MobEffects.WITHER);
            return true;
        }
        // Handles "Dragons Health" gene
        if (component.hasGene(Genes.DRAGONS_HEALTH)) {
            for(int j = 0; j < player.getInventory().getContainerSize(); ++j) {
                ItemStack itemStack = player.getInventory().getItem(j);
                if (itemStack.getItem().equals(ItemRegistry.DRAGON_HEALTH_CRYSTAL)) {
                    itemStack.hurtAndBreak((int)event.getAmount(), player.level(), player, brokenItem -> { });
                    return true;
                }
            }
        }
        return false;
    }
}
