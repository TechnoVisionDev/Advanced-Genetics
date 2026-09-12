package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import com.technovision.advancedgenetics.util.ItemData;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class SyringeItem extends Item {
    public SyringeItem(Properties properties) { super(properties.stacksTo(1)); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        if (!isFilled(stack)) return;
        if (!isPurified(stack)) tooltip.accept(Component.literal("Contaminated").withStyle(ChatFormatting.DARK_RED));
        CompoundTag genes = ItemData.read(stack).getCompoundOrEmpty("genes");
        for (String key : genes.keySet()) {
            Genes gene = Genes.valueOf(key);
            if (gene.isEnabled()) {
                String name = (genes.getBooleanOr(key, false) ? "Anti " : "") + gene.getName();
                tooltip.accept(Component.literal(name).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        CompoundTag tag = ItemData.read(user.getItemInHand(hand));
        if (!tag.getBooleanOr("filled", false) || (tag.getBooleanOr("purified", false) && tag.contains("genes"))) {
            user.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!(world instanceof ServerLevel level) || !(user instanceof Player player)) return stack;
        CompoundTag tag = ItemData.read(user.getMainHandItem());
        if (!tag.getBooleanOr("filled", false)) {
            fill(stack, player);
            user.hurtServer(level, user.damageSources().generic(), 1.0F);
        } else if (tag.getBooleanOr("purified", false) && tag.contains("genes")) {
            if (!Config.Common.geneSharing.get() && !tag.read("uuid", UUIDUtil.CODEC).filter(player.getUUID()::equals).isPresent()) {
                player.sendSystemMessage(Component.translatable("message." + AdvancedGenetics.MOD_ID + ".syringe",
                        "§7" + user.getName().getString() + "§f"));
                return stack;
            }
            inject(player, user.getMainHandItem());
            user.hurtServer(level, user.damageSources().generic(), 1.0F);
            user.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 20 * 10));
        }
        return stack;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) { return ItemUseAnimation.BOW; }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) { return 32; }

    public static boolean isFilled(ItemStack stack) { return ItemData.read(stack).getBooleanOr("filled", false); }
    public static boolean isPurified(ItemStack stack) { return ItemData.read(stack).getBooleanOr("purified", false); }

    public static void fill(ItemStack stack, Player player) {
        ItemData.update(stack, tag -> {
            tag.putBoolean("filled", true);
            tag.putBoolean("purified", false);
            tag.store("uuid", UUIDUtil.CODEC, player.getUUID());
        });
        for (Genes gene : ComponentRegistry.PLAYER_GENETICS.get(player).getGenes()) addGene(stack, gene);
    }

    public static void purify(ItemStack stack) {
        ItemData.update(stack, tag -> tag.putBoolean("purified", true));
    }

    public static void addGene(ItemStack syringe, Genes gene) { addGene(syringe, gene.toString(), false); }
    public static void addGene(ItemStack plasmid, ItemStack syringe) {
        addGene(syringe, ItemData.read(plasmid).getStringOr("gene", ""), false);
    }
    public static void addAntiGene(ItemStack plasmid, ItemStack syringe) {
        addGene(syringe, ItemData.read(plasmid).getStringOr("gene", ""), true);
    }

    private static void addGene(ItemStack syringe, String gene, boolean anti) {
        ItemData.update(syringe, tag -> {
            CompoundTag genes = tag.getCompoundOrEmpty("genes");
            genes.putBoolean(gene, anti);
            tag.put("genes", genes);
            tag.putBoolean("purified", false);
        });
    }

    public static void inject(Player player, ItemStack stack) {
        PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(player);
        CompoundTag genes = ItemData.read(stack).getCompoundOrEmpty("genes");
        for (String key : genes.keySet()) {
            Genes gene = Genes.valueOf(key);
            boolean anti = genes.getBooleanOr(key, false);
            if (anti && component.hasGene(gene)) {
                component.removeGene(gene);
                player.sendSystemMessage(Component.translatable("message." + AdvancedGenetics.MOD_ID + ".inject_remove", "§7" + gene.getName() + "§f"));
            } else if (!anti && !component.hasGene(gene)) {
                component.addGene(gene);
                player.sendSystemMessage(Component.translatable("message." + AdvancedGenetics.MOD_ID + ".inject_add", "§7" + gene.getName() + "§f"));
            }
        }
        ItemData.update(stack, tag -> {
            tag.remove("filled");
            tag.remove("purified");
            tag.remove("genes");
            tag.remove("uuid");
        });
    }
}
