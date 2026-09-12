package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @Inject(method = "useAmmo", at = @At("HEAD"), cancellable = true)
    private static void preserveInfinityArrows(ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangible,
                                               CallbackInfoReturnable<ItemStack> cir) {
        if (weapon.getItem() instanceof BowItem && ammo.is(Items.ARROW) && shooter instanceof Player player
                && ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.INFINITY)) {
            ItemStack projectile = ammo.copyWithCount(1);
            projectile.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            cir.setReturnValue(projectile);
        }
    }
}
