package wtf.moneymod.client.impl.ui.click.theme;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.impl.ui.click.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.*;
import java.awt.*;
import wtf.moneymod.client.impl.module.global.*;
import wtf.moneymod.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public abstract class AbstractTheme extends Renderer2D implements Globals
{
    public abstract void drawHeader(final Panel p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawPanelOutline(final Panel p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawModuleButton(final ModuleButton p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawBooleanButton(final Option<Boolean> p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawSliderButton(final Option<Number> p0, final int p1, final int p2, final int p3, final int p4, final double p5, final boolean p6);
    
    public abstract void drawKeyButton(final KeyButton p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawModeButton(final Option<Enum> p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawSubModeButton(final SubMode p0, final String p1, final int p2, final int p3, final int p4, final int p5, final boolean p6);
    
    public abstract void drawColorButton(final Option<JColor> p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    public abstract void drawPickerButton(final ColorButton p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    protected ScaledResolution getResolution() {
        return new ScaledResolution(AbstractTheme.mc);
    }
    
    protected Color getAbsoluteColor() {
        return ((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).color.getColor();
    }
    
    protected void drawPickerBase(final int pickerX, final int pickerY, final int pickerWidth, final int pickerHeight, final float red, final float green, final float blue, final float alpha) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glBegin(9);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glDisable(3008);
        GL11.glBegin(9);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    protected void drawHueSlider(final int x, final int y, final int width, final int height, final float hue) {
        int step = 0;
        for (int colorIndex = 0; colorIndex < 5; ++colorIndex) {
            final int previousStep = Color.HSBtoRGB(step / 5.0f, 1.0f, 1.0f);
            final int nextStep = Color.HSBtoRGB((step + 1) / 5.0f, 1.0f, 1.0f);
            Renderer2D.drawHGradientRect(x + step * (width / 5.0f), (float)y, x + (step + 1) * (width / 5.0f), (float)(y + height), previousStep, nextStep);
            ++step;
        }
        final int sliderMinX = (int)(x + width * hue);
        Renderer2D.drawRect((float)(sliderMinX - 1), (float)y, (float)(sliderMinX + 1), (float)(y + height), -1);
        Renderer2D.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1.0f, Color.BLACK.getRGB());
    }
    
    protected void drawAlphaSlider(final int x, final int y, final int width, final int height, final float red, final float green, final float blue, final float alpha) {
        boolean left = true;
        for (int checkerBoardSquareSize = height / 2, squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                Gui.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, -1);
                Gui.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, -7303024);
                if (squareIndex < width - checkerBoardSquareSize) {
                    final int minX = x + squareIndex + checkerBoardSquareSize;
                    final int maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    Gui.drawRect(minX, y, maxX, y + height, -7303024);
                    Gui.drawRect(minX, y + checkerBoardSquareSize, maxX, y + height, -1);
                }
            }
            left = !left;
        }
        this.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1.0f).getRGB(), 0);
        final int sliderMinX = (int)(x + width - width * alpha);
        Gui.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        Renderer2D.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1.0f, Color.BLACK.getRGB());
    }
    
    protected void drawLeftGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(f5, f6, f7, f5).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(f5, f6, f7, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
