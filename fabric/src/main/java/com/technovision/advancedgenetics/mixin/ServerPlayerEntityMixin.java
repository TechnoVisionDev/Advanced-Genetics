package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.events.DamageReceivedEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for LivingEntity methods
 *
 * @author TechnoVisions.
 */
@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {

    /**
     * Fires when player takes damage and forwards event info to event handler.
     */
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void damage(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer player = (ServerPlayer) ((Object)this);
        boolean cancelEvent = DamageReceivedEvent.onDamageReceivedEvent(new DamageReceivedEvent(player, source, amount));
        if (cancelEvent) cir.setReturnValue(false);
    }
}
