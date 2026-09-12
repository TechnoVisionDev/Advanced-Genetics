package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    /**
     * Makes all entities glow for a player if they have the "Mob Sight" gene.
     */
        @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void makeEntitiesGlow(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        Entity thisEntity = (Entity)(Object)this;
        if (player != null && player != thisEntity && thisEntity instanceof LivingEntity) {
            if (ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.MOB_SIGHT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
