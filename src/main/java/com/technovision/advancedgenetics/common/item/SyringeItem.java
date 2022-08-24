package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
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
import net.minecraft.text.Text;
import net.minecraft.util.*;
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
                Genes gene = Genes.valueOf(key);
                String geneName = gene.getName();
                if (genesTag.getBoolean(key)) geneName = "Anti " + geneName;
                tooltip.add(Text.literal(geneName).formatted(Formatting.GRAY));
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
                fill(stack, (PlayerEntity)user);
                user.damage(DamageSource.GENERIC, 1.0f);
            }
        } else if (tag.getBoolean("purified") && tag.contains("genes")) {
            // Inject genes into bloodstream
            if (!user.getWorld().isClient()) {
                if (!Config.Common.geneSharing.get() && !tag.getUuid("uuid").equals(user.getUuid())) {
                    user.sendMessage(Text.translatable("message."+AdvancedGenetics.MOD_ID+".syringe", "§7"+user.getName().getString()+"§f"));
                    return stack;
                }
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

    private static List<Pair<Genes, Boolean>> getGenes(ItemStack stack) {
        List<Pair<Genes, Boolean>> genes = new ArrayList<>();
        NbtCompound genesTag = stack.getOrCreateNbt().getCompound("genes");
        for (String geneName : genesTag.getKeys()) {
            Genes gene = Genes.valueOf(geneName);
            genes.add(new Pair<>(gene, genesTag.getBoolean(geneName)));
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

    public static void fill(ItemStack stack, PlayerEntity player) {
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("filled", true);
        tag.putBoolean("purified", false);
        tag.putUuid("uuid", player.getUuid());

        // Add any existing genes
        for (Genes gene : player.getComponent(ComponentRegistry.PLAYER_GENETICS).getGenes()) {
            addGene(stack, gene);
        }
    }

    public static void purify(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.putBoolean("purified", true);
    }

    public static void addGene(ItemStack syringe, Genes gene) {
        final NbtCompound syringeTag = syringe.getOrCreateNbt();
        NbtCompound genes = syringeTag.getCompound("genes");
        genes.putBoolean(gene.toString(), false);
        syringeTag.put("genes", genes);
        syringeTag.putBoolean("purified", false);
    }

    public static void addGene(ItemStack plasmid, ItemStack syringe) {
        String gene = plasmid.getNbt().getString("gene");
        final NbtCompound syringeTag = syringe.getOrCreateNbt();
        NbtCompound genes = syringeTag.getCompound("genes");
        genes.putBoolean(gene, false);
        syringeTag.put("genes", genes);
        syringeTag.putBoolean("purified", false);
    }

    public static void addAntiGene(ItemStack plasmid, ItemStack syringe) {
        String gene = plasmid.getNbt().getString("gene");
        final NbtCompound syringeTag = syringe.getOrCreateNbt();
        NbtCompound genes = syringeTag.getCompound("genes");
        genes.putBoolean(gene, true);
        syringeTag.put("genes", genes);
        syringeTag.putBoolean("purified", false);
    }

    public static void inject(PlayerEntity player, ItemStack stack) {
        // Add genes to user
        PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
        for (Pair<Genes, Boolean> gene : getGenes(stack)) {
            if (gene.getRight()) {
                component.removeGene(gene.getLeft());
            } else {
                component.addGene(gene.getLeft());
            }
        }

        // Reset syringe data
        final NbtCompound tag = stack.getOrCreateNbt();
        tag.remove("filled");
        tag.remove("purified");
        tag.remove("genes");
    }
}
