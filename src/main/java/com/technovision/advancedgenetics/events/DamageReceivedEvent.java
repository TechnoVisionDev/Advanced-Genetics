package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Stores data to be passed to DamageReceivedEvent.
 *
 * @author TechnoVision
 */
public class DamageReceivedEvent {

    private final ServerPlayerEntity player;
    private final DamageSource source;
    private final float amount;

    public DamageReceivedEvent(ServerPlayerEntity player, DamageSource source, float amount) {
        this.player = player;
        this.source = source;
        this.amount = amount;
    }

    public ServerPlayerEntity getPlayer() {
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
        // Handles No Fall Damage gene
        ServerPlayerEntity player = event.getPlayer();
        if (event.getSource().isFromFalling() && player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.NO_FALL_DAMAGE)) {
            return true;
        }
        return false;
    }
}
