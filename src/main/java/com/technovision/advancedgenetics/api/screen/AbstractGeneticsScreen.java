package com.technovision.advancedgenetics.api.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.technovision.advancedgenetics.AdvancedGenetics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class AbstractGeneticsScreen<M extends AbstractGeneticsScreenHandler> extends HandledScreen<M> {

    public AbstractGeneticsScreen(M handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawBackground(matrices, delta, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void renderDisplayTooltip(List<DisplayData> displayData, MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        displayData.stream().filter(data ->
                mouseX >= data.getX() + x &&
                        mouseX <= data.getX() + x + data.getWidth() &&
                        mouseY >= data.getY() + y &&
                        mouseY <= data.getY() + y + data.getHeight()
        ).forEach(data -> {
            if (!(data instanceof ProgressDisplayData)) {
                renderTooltip(matrices, data.toText(), mouseX, mouseY);
            }
        });
    }

    public void renderDisplayData(List<DisplayData> displayData, MatrixStack matrices, int x, int y) {
        displayData.forEach(data -> {
            if (data instanceof ProgressDisplayData) {
                directionalArrow(matrices, x + data.getX(), y + data.getY(), data.getValue(), data.getMaxValue(), ((ProgressDisplayData) data).getDirection());
            }
            if (data instanceof EnergyDisplayData) {
                drawEnergyBar(matrices, (EnergyDisplayData) data, 178, 4);
            }
        });
    }

    public void drawEnergyBar(MatrixStack pPoseStack, EnergyDisplayData data, int textureX, int textureY) {
        int x = data.getX() + (this.width - this.backgroundWidth) / 2;
        int y = data.getY() + (this.height - this.backgroundHeight) / 2;
        this.directionalBlit(pPoseStack, x, y + data.getHeight(), textureX, textureY, data.getWidth(), data.getHeight(), data.getValue(), data.getMaxValue(), Direction2D.UP);
    }

    public void drawTexture(DisplayData pData, Sprite sprite, int pTextureX, int pTextureY) {
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        int renderAmount = Math.max(Math.min(pData.getHeight(), pData.getValue() * pData.getHeight() / pData.getMaxValue()), 1);
        int posY = pTextureY + pData.getHeight() - renderAmount;

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        for (int width = 0; width < pData.getWidth(); width++) {
            for (int height = 0; height < pData.getHeight(); height++) {

                int drawHeight = Math.min(renderAmount - height, 16);
                int drawWidth = Math.min(pData.getWidth() - width, 16);

                int x1 = pTextureX + width;
                float x2 = x1 + drawWidth;
                int y1 = posY + height;
                float y2 = y1 + drawHeight;

                float scaleV = minV + (maxV - minV) * drawHeight / 16f;
                float scaleU = minU + (maxU - minU) * drawWidth / 16f;

                float blitOffset = 0;

                Tessellator tesselator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tesselator.getBuffer();

                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(x1, y2, blitOffset).texture(minU, scaleV).next();
                bufferBuilder.vertex(x2, y2, blitOffset).texture(scaleU, scaleV).next();
                bufferBuilder.vertex(x2, y1, blitOffset).texture(scaleU, minV).next();
                bufferBuilder.vertex(x1, y1, blitOffset).texture(minU, minV).next();
                tesselator.draw();

                height += 15;
            }
            width += 16;
        }
    }

    public static void setShaderColor(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public void directionalArrow(MatrixStack matrices, int x, int y, int progress, int maxProgress, Direction2D direction2D) {
        switch (direction2D) {
            case LEFT -> directionalBlit(matrices, x, y, 0, 120, 9, 30, progress, maxProgress, Direction2D.LEFT);
            case UP -> directionalBlit(matrices, x, y, 0, 138, 9, 30, progress, maxProgress, Direction2D.UP);
            case RIGHT -> directionalBlit(matrices, x, y, 177, 61, 17, 24, progress, maxProgress, Direction2D.RIGHT);
            case DOWN -> directionalBlit(matrices, x, y, 9, 138, 9, 30, progress, maxProgress, Direction2D.DOWN);
        }
    }

    private void directionalBlit(MatrixStack matrices, int x, int y, int uOffset, int vOffset, int u, int v, int progress, int maxProgress, Direction2D direction2D) {
        RenderSystem.setShaderTexture(0, new Identifier(AdvancedGenetics.MOD_ID, "textures/gui/cell_analyzer_gui.png"));

        switch (direction2D) {
            case LEFT -> {
                int scaled = getBarScaled(v, progress, maxProgress);
                drawTexture(matrices, x - scaled, y, uOffset + u - scaled, vOffset, scaled, v);
            }
            case UP -> {
                int scaled = getBarScaled(v, progress, maxProgress);
                drawTexture(matrices, x, y - scaled, uOffset, vOffset + v - scaled, u, scaled);
            }
            case RIGHT -> {
                int scaled = getBarScaled(v, progress, maxProgress);
                drawTexture(matrices, x, y, uOffset, vOffset, scaled, u);
            }
            case DOWN -> {
                int scaled = getBarScaled(v, progress, maxProgress);
                drawTexture(matrices, x, y, uOffset, vOffset, u, scaled);
            }
        }
    }

    public static int getBarScaled(int pixels, int progress, int maxProgress) {
        if (progress > 0 && maxProgress > 0) {
            return progress * pixels / maxProgress;
        } else {
            return 0;
        }
    }
}
