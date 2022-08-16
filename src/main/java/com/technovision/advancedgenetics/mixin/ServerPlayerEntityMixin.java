package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.events.DamageReceivedEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for LivingEntity methods
 *
 * @author TechnoVisions.
 */
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * Fires when player takes damage and forwards event info to event handler.
     */
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) ((Object)this);
        boolean cancelEvent = DamageReceivedEvent.onDamageReceivedEvent(new DamageReceivedEvent(player, source, amount));
        if (cancelEvent) cir.setReturnValue(false);
    }
}
