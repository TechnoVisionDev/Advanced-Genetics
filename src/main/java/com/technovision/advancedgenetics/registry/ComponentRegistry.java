package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Persistent player genetics, copied on respawn and synchronized to tracking clients. */
public final class ComponentRegistry {
    public static final AttachmentType<PlayerGeneticsComponent> TYPE = AttachmentType
            .serializable(holder -> new PlayerGeneticsComponent((Player) holder)).copyOnDeath()
            .sync(new AttachmentSyncHandler<PlayerGeneticsComponent>() {
                public void write(RegistryFriendlyByteBuf buffer, PlayerGeneticsComponent genetics, boolean initialSync) {
                    buffer.writeVarInt(genetics.geneCount());
                    for (Genes gene : genetics.getGenes()) buffer.writeEnum(gene);
                }
                public PlayerGeneticsComponent read(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer, PlayerGeneticsComponent previous) {
                    var genetics = new PlayerGeneticsComponent((Player) holder);
                    int count = buffer.readVarInt();
                    if (count < 0 || count > Genes.values().length) throw new IllegalArgumentException("Invalid gene count");
                    for (int i = 0; i < count; i++) {
                        Genes gene = buffer.readEnum(Genes.class);
                        genetics.getGenesMap().put(gene.name(), gene);
                    }
                    return genetics;
                }
            }).build();
    public static final GeneticsAccess PLAYER_GENETICS = new GeneticsAccess();
    public static void register() {
        Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "player_genetics"), TYPE);
    }
    public static final class GeneticsAccess {
        public PlayerGeneticsComponent get(Player player) { return player.getData(TYPE); }
        public void sync(Player player) { if (!player.level().isClientSide()) player.syncData(TYPE); }
    }
}
