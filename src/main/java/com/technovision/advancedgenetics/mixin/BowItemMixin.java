package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyVariable(method = "onStoppedUsing", at = @At("STORE"), ordinal = 0)
    private boolean onStoppedUsing(boolean bl, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        // Allow using bow without any arrows if player has "infinity" gene
        PlayerEntity player = (PlayerEntity) user;
        bl = player.getAbilities().creativeMode
                || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0
                || player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.INFINITY);
        return bl;
    }
}
