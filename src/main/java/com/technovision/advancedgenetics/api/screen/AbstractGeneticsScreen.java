package com.technovision.advancedgenetics.api.screen;

import com.technovision.advancedgenetics.AdvancedGenetics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

public abstract class AbstractGeneticsScreen<M extends AbstractGeneticsScreenHandler> extends AbstractContainerScreen<M> {
    private static final Identifier DISPLAY_TEXTURE = Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "textures/gui/cell_analyzer_gui.png");

    public AbstractGeneticsScreen(M handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 176, 166);
    }

    public void renderDisplayTooltip(List<DisplayData> displayData, GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY) {
        displayData.stream().filter(data -> mouseX >= data.getX() + x && mouseX <= data.getX() + x + data.getWidth()
                && mouseY >= data.getY() + y && mouseY <= data.getY() + y + data.getHeight())
                .filter(data -> !(data instanceof ProgressDisplayData))
                .forEach(data -> graphics.setComponentTooltipForNextFrame(font, data.toText(), mouseX, mouseY));
    }

    public void renderDisplayData(List<DisplayData> displayData, GuiGraphicsExtractor graphics, int x, int y) {
        displayData.forEach(data -> {
            if (data instanceof ProgressDisplayData progress) {
                directionalArrow(graphics, x + data.getX(), y + data.getY(), data.getValue(), data.getMaxValue(), progress.getDirection());
            }
            if (data instanceof EnergyDisplayData energy) drawEnergyBar(graphics, energy, 178, 4);
        });
    }

    public void drawEnergyBar(GuiGraphicsExtractor graphics, EnergyDisplayData data, int textureX, int textureY) {
        int x = data.getX() + (width - imageWidth) / 2;
        int y = data.getY() + (height - imageHeight) / 2;
        directionalBlit(graphics, x, y + data.getHeight(), textureX, textureY, data.getWidth(), data.getHeight(), data.getValue(), data.getMaxValue(), Direction2D.UP);
    }

    public void directionalArrow(GuiGraphicsExtractor graphics, int x, int y, int progress, int maxProgress, Direction2D direction) {
        switch (direction) {
            case LEFT -> directionalBlit(graphics, x, y, 0, 120, 9, 30, progress, maxProgress, direction);
            case UP -> directionalBlit(graphics, x, y, 0, 138, 9, 30, progress, maxProgress, direction);
            case RIGHT -> directionalBlit(graphics, x, y, 177, 61, 17, 24, progress, maxProgress, direction);
            case DOWN -> directionalBlit(graphics, x, y, 9, 138, 9, 30, progress, maxProgress, direction);
        }
    }

    private void directionalBlit(GuiGraphicsExtractor graphics, int x, int y, int uOffset, int vOffset, int u, int v, int progress, int maxProgress, Direction2D direction) {
        int scaled = getBarScaled(v, progress, maxProgress);
        if (scaled <= 0) return;
        switch (direction) {
            case LEFT -> blit(graphics, x - scaled, y, uOffset + u - scaled, vOffset, scaled, v);
            case UP -> blit(graphics, x, y - scaled, uOffset, vOffset + v - scaled, u, scaled);
            case RIGHT -> blit(graphics, x, y, uOffset, vOffset, scaled, u);
            case DOWN -> blit(graphics, x, y, uOffset, vOffset, u, scaled);
        }
    }

    private void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPLAY_TEXTURE, x, y, u, v, width, height, 256, 256);
    }

    public static int getBarScaled(int pixels, int progress, int maxProgress) {
        return progress > 0 && maxProgress > 0 ? progress * pixels / maxProgress : 0;
    }
}
