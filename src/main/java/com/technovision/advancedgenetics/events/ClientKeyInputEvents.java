package com.technovision.advancedgenetics.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class ClientKeyInputEvents {
    public static final String KEY_TELEPORT = "key." + AdvancedGenetics.MOD_ID + ".teleport";
    public static final String KEY_DRAGONS_BREATH = "key." + AdvancedGenetics.MOD_ID + ".dragons_breath";
    public static KeyMapping teleportKey;
    public static KeyMapping dragonsBreathKey;

    public static void registerClientSide(net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent event) {
        var category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "genetics"));
        teleportKey = new KeyMapping(KEY_TELEPORT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, category);
        dragonsBreathKey = new KeyMapping(KEY_DRAGONS_BREATH, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, category);
        event.register(teleportKey);
        event.register(dragonsBreathKey);
    }

    public static void tick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
            var client = net.minecraft.client.Minecraft.getInstance();
            if (client.player == null) return;
            PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(client.player);
            if (teleportKey.consumeClick() && component.hasGene(Genes.TELEPORT)) {
                net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new KeyInputEvents.KeyPressedPayload("teleport"));
            } else if (dragonsBreathKey.consumeClick() && component.hasGene(Genes.DRAGONS_BREATH)) {
                net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new KeyInputEvents.KeyPressedPayload("dragons_breath"));
            }
    }
}
