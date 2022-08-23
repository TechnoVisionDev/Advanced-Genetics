package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyInputEvents {

    public static final String KEY_CATEGORY_GENETICS = "key."+ AdvancedGenetics.MOD_ID+".category";
    public static final String KEY_TELEPORT = "key."+ AdvancedGenetics.MOD_ID+".teleport";

    public static KeyBinding teleportKey;

    @Environment(EnvType.CLIENT)
    public static void registerClientSide() {
        teleportKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_TELEPORT, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_GENETICS));
        registerKeyInputs();
    }

    public static void registerServerSide() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(AdvancedGenetics.MOD_ID, "key_pressed"), (server, player, handler, buf, responseSender) -> {
            String geneName = buf.readString();
            PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
            if (geneName.equals("teleport") && component.hasGene(Genes.TELEPORT)) {
                // TODO: Fix vector
                Vec3d v3 = player.getPos();
                player.teleport(v3.getX()+6, v3.getY(), v3.getZ(), true);
            }
        });
    }

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (teleportKey.wasPressed()) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeString("teleport");
                ClientPlayNetworking.send(new Identifier(AdvancedGenetics.MOD_ID, "key_pressed"), buf);
                client.player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        });
    }
}
