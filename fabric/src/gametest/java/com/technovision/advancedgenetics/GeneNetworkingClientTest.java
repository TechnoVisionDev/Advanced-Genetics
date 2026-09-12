package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.events.ClientKeyInputEvents;
import com.technovision.advancedgenetics.events.KeyInputEvents;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

/** Exercises the real connection, CCA synchronization, client mixin and both default hotkeys. */
public class GeneNetworkingClientTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            int pigId = world.getServer().computeOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                player.setGameMode(GameType.SURVIVAL);
                BlockPos floor = player.blockPosition().above(6);
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 20; z++) {
                        player.level().setBlockAndUpdate(floor.offset(x, 0, z), Blocks.STONE.defaultBlockState());
                        for (int y = 1; y <= 4; y++) {
                            player.level().setBlockAndUpdate(floor.offset(x, y, z), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
                Vec3 origin = Vec3.atBottomCenterOf(floor.above());
                player.teleportTo(origin.x, origin.y, origin.z);
                var pig = EntityTypes.PIG.create(player.level(), EntitySpawnReason.COMMAND);
                pig.setNoAi(true);
                pig.setPos(origin.add(2, 0, 0));
                player.level().addFreshEntity(pig);
                ComponentRegistry.PLAYER_GENETICS.get(player).addGenes(java.util.List.of(
                        Genes.TELEPORT, Genes.DRAGONS_BREATH, Genes.MOB_SIGHT));
                return pig.getId();
            });
            world.getConnection().waitForClientboundPackets();
            context.waitFor(client -> {
                var genes = ComponentRegistry.PLAYER_GENETICS.get(client.player);
                return genes.hasGene(Genes.TELEPORT) && genes.hasGene(Genes.DRAGONS_BREATH)
                        && genes.hasGene(Genes.MOB_SIGHT) && client.level.getEntity(pigId) != null;
            });
            context.runOnClient(client -> require(client.level.getEntity(pigId).isCurrentlyGlowing(),
                    "Mob Sight must make a tracked mob glow on the real client"));
            context.getInput().lookAt(0, 0);
            context.waitTick();
            world.getConnection().waitForServerboundPackets();
            Vec3 origin = world.getServer().computeOnServer(server -> server.getPlayerList().getPlayers().getFirst().position());

            long teleportSent = System.currentTimeMillis();
            context.getInput().pressKey(ClientKeyInputEvents.teleportKey);
            world.getConnection().waitForServerboundPackets();
            long teleportReceived = System.currentTimeMillis();
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                require(Math.abs(player.getZ() - origin.z - 6) < 0.01 && Math.abs(player.getX() - origin.x) < 0.01,
                        "The G hotkey must teleport six blocks along the horizontal view direction");
                assertCooldown(ComponentRegistry.PLAYER_GENETICS.get(player), "teleport", 1000,
                        teleportSent, teleportReceived);
            });
            world.getConnection().waitForClientboundPackets();
            context.waitFor(client -> Math.abs(client.player.getZ() - origin.z - 6) < 0.01 && client.player.onGround());
            world.getServer().waitFor(server -> server.getPlayerList().getPlayers().getFirst().onGround());

            long breathSent = System.currentTimeMillis();
            context.getInput().pressKey(ClientKeyInputEvents.dragonsBreathKey);
            world.getConnection().waitForServerboundPackets();
            long breathReceived = System.currentTimeMillis();
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                require(fireballCount(player.level()) == 1, "The H hotkey must create one dragon fireball on the server");
                assertCooldown(ComponentRegistry.PLAYER_GENETICS.get(player), "dragons_breath", 15000,
                        breathSent, breathReceived);
            });
            context.getInput().pressKey(ClientKeyInputEvents.dragonsBreathKey);
            world.getConnection().waitForServerboundPackets();
            world.getServer().runOnServer(server -> require(fireballCount(server.getPlayerList().getPlayers().getFirst().level()) == 1,
                    "A second H press during the cooldown must not spawn another fireball"));

            Vec3 beforeDeniedRequest = world.getServer().computeOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                var component = ComponentRegistry.PLAYER_GENETICS.get(player);
                component.removeAllGenes();
                // Expire both timers so the next checks specifically exercise gene authorization.
                component.addCooldown("teleport", -1);
                component.addCooldown("dragons_breath", -1);
                var fireballs = new java.util.ArrayList<DragonFireball>();
                for (var entity : player.level().getAllEntities()) {
                    if (entity instanceof DragonFireball fireball) fireballs.add(fireball);
                }
                fireballs.forEach(DragonFireball::discard);
                return player.position();
            });
            world.getConnection().waitForClientboundPackets();
            context.waitFor(client -> ComponentRegistry.PLAYER_GENETICS.get(client.player).geneCount() == 0);
            context.runOnClient(client -> {
                require(!client.level.getEntity(pigId).isCurrentlyGlowing(), "Removing Mob Sight must stop the client outline");
                ClientPlayNetworking.send(new KeyInputEvents.KeyPressedPayload("teleport"));
                ClientPlayNetworking.send(new KeyInputEvents.KeyPressedPayload("dragons_breath"));
            });
            world.getConnection().waitForServerboundPackets();
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                require(player.position().distanceToSqr(beforeDeniedRequest) < 0.001,
                        "The server must reject a teleport packet from a player without the gene");
                require(fireballCount(player.level()) == 0,
                        "The server must reject a dragon-breath packet from a player without the gene");
            });
        }
    }

    private static int fireballCount(ServerLevel level) {
        int count = 0;
        for (var entity : level.getAllEntities()) if (entity instanceof DragonFireball) count++;
        return count;
    }

    @SuppressWarnings("unchecked")
    private static void assertCooldown(PlayerGeneticsComponent component, String key, long duration,
                                       long requestSent, long requestReceived) {
        try {
            var field = PlayerGeneticsComponent.class.getDeclaredField("cooldowns");
            field.setAccessible(true);
            Long deadline = ((Map<String, Long>) field.get(component)).get(key);
            require(deadline != null && deadline >= requestSent + duration && deadline <= requestReceived + duration,
                    key + " must retain its original " + duration + " millisecond cooldown");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Could not inspect ability cooldown", e);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
