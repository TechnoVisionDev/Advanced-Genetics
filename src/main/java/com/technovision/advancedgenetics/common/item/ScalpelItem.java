package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
        if (!user.getWorld().isClient() && hand == Hand.MAIN_HAND) {
            scrapeEntity(stack, user, entity);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void scrapeEntity(ItemStack stack, PlayerEntity user, LivingEntity entity) {
        Item item = null;
        if (EntityType.COW == entity.getType()) {
            item = ItemRegistry.COW_MATTER;
        } else if (EntityType.PIG == entity.getType()) {
            item = ItemRegistry.PIG_MATTER;
        }

        if (item != null) {
            user.giveItemStack(new ItemStack(item));
            entity.damage(DamageSource.player(user), 1.0f);
            stack.damage(1, user, (e) -> user.sendToolBreakStatus(user.getActiveHand()));
        }
    }
}
