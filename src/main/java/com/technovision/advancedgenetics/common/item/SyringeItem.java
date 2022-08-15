package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SyringeItem extends Item {

    public SyringeItem() {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB).maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!stack.hasNbt() || !isFilled(stack)) return;
        if (!isPurified(stack)) {
            tooltip.add(Text.literal("Contaminated").formatted(Formatting.GRAY));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getStackInHand(hand).getOrCreateNbt().getBoolean("filled")) {
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()) {
            fill(stack);
            user.damage(DamageSource.GENERIC, 1.0f);
        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public static boolean isFilled(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        return tag.getBoolean("filled");
    }

    public static boolean isPurified(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        return tag.getBoolean("purified");
    }

    public static void fill(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("filled", true);
        tag.putBoolean("purified", false);
    }

    public static void purify(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("purified", true);
    }
}
