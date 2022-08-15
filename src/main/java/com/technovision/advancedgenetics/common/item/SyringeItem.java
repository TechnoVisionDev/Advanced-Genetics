package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.components.AdvancedGeneticsComponents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("genes")) {
            NbtCompound genesTag = tag.getCompound("genes");
            for (String key : genesTag.getKeys()) {
                Genes gene = Genes.valueOf(genesTag.getString(key));
                tooltip.add(Text.literal(gene.getName()).setStyle(Style.EMPTY.withColor(gene.getColor())));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand != Hand.MAIN_HAND) return TypedActionResult.pass(user.getStackInHand(hand));
        NbtCompound tag = user.getStackInHand(hand).getOrCreateNbt();
        if (!tag.getBoolean("filled") || (tag.getBoolean("purified") && tag.contains("genes"))) {
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        NbtCompound tag = user.getMainHandStack().getOrCreateNbt();
        if (!tag.getBoolean("filled")) {
            // Draw blood
            if (!world.isClient()) {
                fill(stack);
                user.damage(DamageSource.GENERIC, 1.0f);
            }
        } else if (tag.getBoolean("purified") && tag.contains("genes")) {
            // Inject genes into bloodstream
            if (!user.getWorld().isClient()) {
                inject((PlayerEntity) user, user.getMainHandStack());
                user.damage(DamageSource.GENERIC, 1.0f);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*10));
            }
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

    private static List<Genes> getGenes(ItemStack stack) {
        List<Genes> genes = new ArrayList<>();
        NbtCompound genesTag = stack.getOrCreateNbt().getCompound("genes");
        for (String geneName : genesTag.getKeys()) {
            Genes gene = Genes.valueOf(geneName);
            genes.add(gene);
        }
        return genes;
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

    public static void addGene(ItemStack plasmid, ItemStack syringe) {
        String gene = plasmid.getNbt().getString("gene");
        final NbtCompound syringeTag = syringe.getOrCreateNbt();
        NbtCompound genes = syringeTag.getCompound("genes");
        genes.putString(gene, gene);
        syringeTag.put("genes", genes);
        syringeTag.putBoolean("purified", false);
    }

    public static void inject(PlayerEntity user, ItemStack stack) {
        // Add genes to user
        List<Genes> genes = getGenes(stack);
        user.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).addGenes(genes);

        // Reset syringe data
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.remove("filled");
        tag.remove("purified");
        tag.remove("genes");
    }
}
