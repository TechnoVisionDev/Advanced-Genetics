package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.glfw.GLFW;

/** Drive real mouse clicks so a callback that blocks the item cannot evade this test. */
public class ScalpelClientTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            for (Item scalpel : new Item[]{ItemRegistry.METAL_SCALPEL, ItemRegistry.DIAMOND_SCALPEL, ItemRegistry.NETHERITE_SCALPEL}) {
                int mobId = world.getServer().computeOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    player.setGameMode(GameType.SURVIVAL);
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(scalpel));
                    var cow = EntityTypes.COW.create(player.level(), EntitySpawnReason.COMMAND);
                    cow.setNoAi(true);
                    cow.setPos(player.position().add(0, 0, 2));
                    player.level().addFreshEntity(cow);
                    return cow.getId();
                });
                world.getConnection().waitForClientboundPackets();
                context.waitFor(client -> client.level.getEntity(mobId) != null && client.player.getMainHandItem().is(scalpel));
                context.getInput().lookAt(context.computeOnClient(client -> client.level.getEntity(mobId).blockPosition()));
                context.waitFor(client -> client.hitResult instanceof EntityHitResult hit && hit.getEntity().getId() == mobId);
                context.getInput().pressMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
                world.getConnection().waitForServerboundPackets();
                world.getServer().runOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    var cow = (net.minecraft.world.entity.LivingEntity) player.level().getEntity(mobId);
                    var matter = ItemRegistry.MATTER.get(EntityTypes.COW);
                    int count = player.level().getEntitiesOfClass(ItemEntity.class, cow.getBoundingBox().inflate(3)).stream()
                            .filter(drop -> drop.getItem().is(matter)).mapToInt(drop -> drop.getItem().getCount()).sum();
                    if (count != 1 || player.getMainHandItem().getDamageValue() != 1 || cow.getHealth() != cow.getMaxHealth() - 1)
                        throw new AssertionError("Mouse right-click must yield one cow slice and cost one durability/health with " + scalpel);
                    player.level().getEntitiesOfClass(ItemEntity.class, cow.getBoundingBox().inflate(3)).forEach(ItemEntity::discard);
                    cow.discard();
                });
                world.getConnection().waitForClientboundPackets();
                context.waitFor(client -> client.player.getMainHandItem().getDamageValue() == 1);
            }
        }
    }
}
