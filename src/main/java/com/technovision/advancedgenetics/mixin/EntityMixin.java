package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    /**
     * Makes all entities glow for a player if they have the "Mob Sight" gene.
     */
    @Environment(EnvType.CLIENT)
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void makeEntitiesGlow(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Entity thisEntity = (Entity)(Object)this;
        if (player != null && player != thisEntity && thisEntity instanceof LivingEntity) {
            if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.MOB_SIGHT)) {
                cir.setReturnValue(true);
            }
        }
    }
}
