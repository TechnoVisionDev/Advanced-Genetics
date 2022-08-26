package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.entity.FireballEntity;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputEvents {

    public static final String KEY_CATEGORY_GENETICS = "key."+ AdvancedGenetics.MOD_ID+".category";
    public static final String KEY_TELEPORT = "key."+ AdvancedGenetics.MOD_ID+".teleport";
    public static final String KEY_DRAGONS_BREATH = "key."+ AdvancedGenetics.MOD_ID+".dragons_breath";

    public static KeyBinding teleportKey;
    public static KeyBinding dragonsBreathKey;

    @Environment(EnvType.CLIENT)
    public static void registerClientSide() {
        teleportKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_TELEPORT, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_GENETICS));
        dragonsBreathKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_DRAGONS_BREATH, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY_GENETICS));
        registerKeyInputs();
    }

    public static void registerServerSide() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(AdvancedGenetics.MOD_ID, "key_pressed"), (server, player, handler, buf, responseSender) -> {
            String geneName = buf.readString();
            PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
            if (geneName.equals("teleport") && component.hasGene(Genes.TELEPORT)) {
                Vec3d v3 = player.getRotationVector().multiply(6).add(player.getEyePos());
                player.teleport(v3.getX(), v3.getY(), v3.getZ(), true);
            }
            else if (geneName.equals("dragons_breath") && component.hasGene(Genes.DRAGONS_BREATH)) {
                Vec3d v3 = player.getRotationVec(1);
                DragonFireballEntity fireballEntity = new DragonFireballEntity(player.getWorld(), player, v3.getX(), v3.getY(), v3.getZ());
                fireballEntity.setPosition(player.getX(), player.getY() + 1.5, player.getZ());
                player.getWorld().spawnEntity(fireballEntity);
            }
        });
    }

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            PlayerGeneticsComponent component = client.player.getComponent(ComponentRegistry.PLAYER_GENETICS);
            if (teleportKey.wasPressed() && component.hasGene(Genes.TELEPORT)) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeString("teleport");
                ClientPlayNetworking.send(new Identifier(AdvancedGenetics.MOD_ID, "key_pressed"), buf);
                client.player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
            else if (dragonsBreathKey.wasPressed() && component.hasGene(Genes.DRAGONS_BREATH)) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeString("dragons_breath");
                ClientPlayNetworking.send(new Identifier(AdvancedGenetics.MOD_ID, "key_pressed"), buf);
                client.player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.0f);
            }
        });
    }
}
