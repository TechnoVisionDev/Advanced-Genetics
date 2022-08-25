package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class DragonHealthCrystalItem extends Item {

    public DragonHealthCrystalItem() {
        super(new FabricItemSettings().maxCount(1).maxDamage(1000).group(AdvancedGenetics.TAB));
    }
}
