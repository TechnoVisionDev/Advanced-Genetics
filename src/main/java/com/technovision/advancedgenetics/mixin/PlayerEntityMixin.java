package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /**
     * Keep inventory if player has "Keep Inventory" gene
     */
    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void dropInventory(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.KEEP_INVENTORY)) {
            ci.cancel();
        }
    }

    /**
     * Allow walking through cobwebs if player has "Web Walking" gene
     */
    @Inject(at = @At("HEAD"), method = "slowMovement", cancellable = true)
    public void slowMovement(BlockState state, Vec3d multiplier, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient()) {
            System.out.println("CLIENT: "+player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.WEB_WALKING));
        }
        if (!player.getWorld().isClient()) {
            System.out.println("SERVER: "+player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.WEB_WALKING));
        }
        if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.WEB_WALKING)) {
            info.cancel();
        }
    }
}
