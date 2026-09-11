package com.technovision.advancedgenetics.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class ClientKeyInputEvents {
    public static final String KEY_TELEPORT = "key." + AdvancedGenetics.MOD_ID + ".teleport";
    public static final String KEY_DRAGONS_BREATH = "key." + AdvancedGenetics.MOD_ID + ".dragons_breath";
    public static KeyMapping teleportKey;
    public static KeyMapping dragonsBreathKey;

    public static void registerClientSide() {
        var category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "genetics"));
        teleportKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(KEY_TELEPORT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, category));
        dragonsBreathKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(KEY_DRAGONS_BREATH, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(client.player);
            if (teleportKey.consumeClick() && component.hasGene(Genes.TELEPORT)) {
                ClientPlayNetworking.send(new KeyInputEvents.KeyPressedPayload("teleport"));
            } else if (dragonsBreathKey.consumeClick() && component.hasGene(Genes.DRAGONS_BREATH)) {
                ClientPlayNetworking.send(new KeyInputEvents.KeyPressedPayload("dragons_breath"));
            }
        });
    }
}
