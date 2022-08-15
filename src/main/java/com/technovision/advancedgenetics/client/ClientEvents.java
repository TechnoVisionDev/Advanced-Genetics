package com.technovision.advancedgenetics.client;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.item.GlassSyringeItem;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ClientEvents {

    /**
     * Updates predicate for syringe items on client startup,
     */
    public static void propertyOverrideRegistry() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            ModelPredicateProviderRegistry.register(
                    ItemRegistry.GLASS_SYRINGE,
                    new Identifier(AdvancedGenetics.MOD_ID, "filled"),
                    (itemStack, clientWorld, livingEntity, num) -> GlassSyringeItem.isFilled(itemStack) ? 1 : 0
            );
        });
    }
}
