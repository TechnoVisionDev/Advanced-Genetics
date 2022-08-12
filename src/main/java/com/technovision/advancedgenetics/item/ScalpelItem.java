package com.technovision.advancedgenetics.item;

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
        return ActionResult.FAIL;
    }

    private void scrapeEntity(ItemStack stack, PlayerEntity user, LivingEntity entity) {
        if (EntityType.COW == entity.getType()) {
            user.giveItemStack(new ItemStack(ItemRegistry.COW_MATTER));
        }
        entity.damage(DamageSource.player(user), 1.0f);
        stack.damage(1, user, (e) -> user.sendToolBreakStatus(user.getActiveHand()));
    }
}
