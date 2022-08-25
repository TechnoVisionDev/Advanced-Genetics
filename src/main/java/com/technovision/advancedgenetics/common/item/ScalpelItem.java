package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ScalpelItem extends Item {

    public ScalpelItem(int durability) {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB).maxCount(1).maxDamage(durability));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        ItemCooldownManager cooldownManager = user.getItemCooldownManager();
        if (!user.getWorld().isClient() && hand == Hand.MAIN_HAND && !cooldownManager.isCoolingDown(stack.getItem())) {
            scrapeEntity(stack, user, entity);
            cooldownManager.set(stack.getItem(), 10);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void scrapeEntity(ItemStack stack, PlayerEntity user, LivingEntity entity) {
        EntityType type = entity.getType();
        OrganicMatterItem item = ItemRegistry.MATTER.get(type);

        if (item == null) {
            if (type == EntityType.SKELETON_HORSE) {
                item = ItemRegistry.MATTER.get(EntityType.SKELETON);
            }
            else if (type == EntityType.ZOMBIE_HORSE || type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.ZOMBIE_VILLAGER) {
                item = ItemRegistry.MATTER.get(EntityType.ZOMBIE);
            }
            else if (type == EntityType.MULE) {
                item = ItemRegistry.MATTER.get(EntityType.HORSE);
            }
            else if (type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE) {
                item = ItemRegistry.MATTER.get(EntityType.PIG);
            }
            else if (type == EntityType.WITCH) {
                item = ItemRegistry.MATTER.get(EntityType.VILLAGER);
            }
        }
        if (item != null) {
            entity.dropItem(item);
            entity.damage(DamageSource.player(user), 1.0f);
            stack.damage(1, user, (e) -> user.sendToolBreakStatus(user.getActiveHand()));
        }
    }
}
