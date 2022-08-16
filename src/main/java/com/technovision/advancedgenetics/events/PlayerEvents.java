package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.components.AdvancedGeneticsComponents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.item.Items;
import net.minecraft.world.explosion.Explosion;

/**
 * Handles genes that trigger on player attack, damage, or death.
 *
 * @author TechnoVision
 */
public class PlayerEvents {

    public static void registerEvents() {
        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
            // Handles Explosive Exit gene
            if (player.getInventory().count(Items.GUNPOWDER) >= 5) {
                if (player.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).containsGene(Genes.EXPLOSIVE_EXIT)) {
                    player.getWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), 3.0f, Explosion.DestructionType.BREAK);
                }
            }
            return true;
        });
    }
}
