package com.technovision.advancedgenetics.common.block.dnadecrypter;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.screen.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.List;

public class DnaDecrypterScreen extends AbstractGeneticsScreen<DnaDecrypterScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "textures/gui/dna_decrypter_gui.png");
    protected final List<DisplayData> displayData = new ArrayList<>();

    public DnaDecrypterScreen(DnaDecrypterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        displayData.add(new ProgressDisplayData(handler.getPropertyDelegate(), 0, 1, 83, 37, 60, 9, Direction2D.RIGHT));
        displayData.add(new EnergyDisplayData(handler.getPropertyDelegate(), 2, 3, 10, 23, 12, 40));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractContents(graphics, mouseX, mouseY, delta);
        renderDisplayData(displayData, graphics, leftPos, topPos);
        renderDisplayTooltip(displayData, graphics, leftPos, topPos, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        int overclock = menu.getPropertyDelegate().get(4);
        if (overclock > 0) {
            String text = "x" + overclock;
            graphics.text(font, text, imageWidth - font.width(text) - 8, 6, 0xff3f3f3f, false);
        }
    }
}
