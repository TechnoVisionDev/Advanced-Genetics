package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.api.block.AbstractGeneticsBlock;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class GeneticsBlockItem extends BlockItem {
    public GeneticsBlockItem(AbstractGeneticsBlock block, Properties properties) { super(block, properties.useBlockDescriptionPrefix()); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.advancedgenetics.energy_requirement",
                ((AbstractGeneticsBlock) getBlock()).getEnergyRequirement()).withStyle(ChatFormatting.GRAY));
    }
}
