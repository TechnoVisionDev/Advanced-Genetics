package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void dropInventory(CallbackInfo ci) {
        // Keep inventory if player has gene
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.KEEP_INVENTORY)) {
            System.out.println("1");
            ci.cancel();
        }
    }
}
