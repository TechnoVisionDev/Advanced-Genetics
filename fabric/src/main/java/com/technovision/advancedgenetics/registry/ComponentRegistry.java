package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.Identifier;

public class ComponentRegistry implements EntityComponentInitializer {

    public static final ComponentKey<PlayerGeneticsComponent> PLAYER_GENETICS =
            org.ladysnake.cca.api.v3.component.ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "player_genetics"), PlayerGeneticsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Add the component to every Player instance, and copy it on respawn
        registry.registerForPlayers(PLAYER_GENETICS, PlayerGeneticsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
