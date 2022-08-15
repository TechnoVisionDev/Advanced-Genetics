package com.technovision.advancedgenetics.common.block.plasmidinjector;

import com.mojang.blaze3d.systems.RenderSystem;
import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.screen.*;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PlasmidInjectorScreen extends AbstractGeneticsScreen<PlasmidInjectorScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(AdvancedGenetics.MOD_ID, "textures/gui/plasmid_injector_gui.png");

    protected final List<DisplayData> displayData = new ArrayList<>();

    public PlasmidInjectorScreen(PlasmidInjectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        displayData.add(new ProgressDisplayData(handler.getPropertyDelegate(), 0, 1, 83, 37, 60, 9, Direction2D.RIGHT));
        displayData.add(new EnergyDisplayData(handler.getPropertyDelegate(), 2, 3, 10, 23, 12, 40));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderDisplayData(displayData, matrices, this.x, this.y);
        renderDisplayTooltip(displayData, matrices, this.x, this.y, mouseX, mouseY);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        int overclock = handler.getPropertyDelegate().get(4);
        if (overclock > 0) {
            String text = "x"+overclock;
            textRenderer.draw(matrices, text, backgroundWidth - textRenderer.getWidth(text) - 8, 6, 0x3f3f3f);
        }
    }

}
