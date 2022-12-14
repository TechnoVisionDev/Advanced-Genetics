package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ComponentRegistry implements EntityComponentInitializer {

    public static final ComponentKey<PlayerGeneticsComponent> PLAYER_GENETICS =
            dev.onyxstudios.cca.api.v3.component.ComponentRegistry.getOrCreate(new Identifier(AdvancedGenetics.MOD_ID, "player_genetics"), PlayerGeneticsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Add the component to every PlayerEntity instance, and copy it on respawn
        registry.registerForPlayers(PLAYER_GENETICS, PlayerGeneticsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
