package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import net.minecraft.world.phys.Vec3;

/** Common packet registration; client key mappings live in ClientKeyInputEvents. */
public class KeyInputEvents {
    public record KeyPressedPayload(String geneName) implements CustomPacketPayload {
        public static final Type<KeyPressedPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "key_pressed"));
        public static final StreamCodec<RegistryFriendlyByteBuf, KeyPressedPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, KeyPressedPayload::geneName, KeyPressedPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void registerServerSide() {
        PayloadTypeRegistry.serverboundPlay().register(KeyPressedPayload.TYPE, KeyPressedPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KeyPressedPayload.TYPE, (payload, context) -> {
            var player = context.player();
            String geneName = payload.geneName();
            PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(player);
            if (geneName.equals("teleport") && component.hasGene(Genes.TELEPORT)) {
                if (!component.isOnCooldown("teleport")) {
                    Vec3 target = player.getLookAngle().scale(6).add(player.getEyePosition());
                    player.randomTeleport(target.x, target.y, target.z, true);
                    component.addCooldown("teleport", 1);
                } else {
                    player.sendSystemMessage(Component.translatable("message." + AdvancedGenetics.MOD_ID + ".cooldown", "§7" + Genes.TELEPORT.getName() + "§f"));
                }
            } else if (geneName.equals("dragons_breath") && component.hasGene(Genes.DRAGONS_BREATH)) {
                if (!component.isOnCooldown("dragons_breath")) {
                    DragonFireball fireball = new DragonFireball(player.level(), player, player.getViewVector(1));
                    fireball.setPos(player.getX(), player.getY() + 1.5, player.getZ());
                    player.level().addFreshEntity(fireball);
                    player.playSound(SoundEvents.ENDER_DRAGON_SHOOT, 1.0f, 1.0f);
                    component.addCooldown("dragons_breath", 15);
                } else {
                    player.sendSystemMessage(Component.translatable("message." + AdvancedGenetics.MOD_ID + ".cooldown", "§7" + Genes.DRAGONS_BREATH.getName() + "§f"));
                }
            }
        });
    }
}
