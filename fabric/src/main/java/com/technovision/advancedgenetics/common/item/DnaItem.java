package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.util.ItemData;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class DnaItem extends Item {
    public DnaItem(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        if (!ItemData.read(stack).contains("gene")) return;
        Genes gene = Genes.getGeneByItem(stack);
        tooltip.accept(Component.literal(isDecoded(stack) ? gene.getName() : gene.getEncryptedName())
                .withStyle(ChatFormatting.GRAY));
    }

    public static boolean isDecoded(ItemStack stack) {
        return ItemData.read(stack).getBooleanOr("decoded", false);
    }

    public static void setGene(ItemStack cellStack, ItemStack dnaStack) {
        Genes gene = Entities.findEntityByType(((CellItem) cellStack.getItem()).getEntityType()).getRandomGene();
        ItemData.update(dnaStack, tag -> {
            tag.putString("gene", gene.toString());
            tag.putBoolean("decoded", false);
        });
    }

    public static void decode(ItemStack stack) {
        ItemData.update(stack, tag -> tag.putBoolean("decoded", true));
    }
}
