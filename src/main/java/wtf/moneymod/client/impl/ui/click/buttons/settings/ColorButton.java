package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import java.awt.*;
import wtf.moneymod.client.impl.ui.click.*;
import net.minecraft.client.gui.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class ColorButton extends Component
{
    public final Option<JColor> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private boolean open;
    private boolean dragging;
    private Color color;
    
    public ColorButton(final Option<JColor> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
        this.open = false;
        this.dragging = false;
    }
    
    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        this.isHovered = this.isHovered(mouseX, mouseY);
        this.y = this.button.panel.getY() + this.offset;
        this.x = this.button.panel.getX();
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawColorButton(this.setting, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 12, this.isHovered);
        if (this.open) {
            final float[] hsb = { Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[0], Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[1], Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[2] };
            int alphas = ((JColor)this.setting.getValue()).getColor().getAlpha();
            final boolean picker = mouseX > this.x + 2 && mouseX < this.x + 108 && mouseY > this.y + 18 && mouseY < this.y + 126;
            final boolean hue = mouseX > this.x + 2 && mouseX < this.x + 109 && mouseY > this.y + 126 && mouseY < this.y + 134;
            final boolean alpha = mouseX > this.x + 2 && mouseX < this.x + 109 && mouseY > this.y + 134 && mouseY < this.y + 141;
            if (this.dragging) {
                if (picker) {
                    final float restrictedX = (float)Math.min(Math.max(this.x + 2, mouseX), this.x + 2 + 106);
                    final float restrictedY = (float)Math.min(Math.max(this.y + 18, mouseY), this.y + 18 + 106);
                    hsb[1] = Math.max(Math.min((restrictedX - this.x + 2.0f) / 106.0f, 1.0f), 0.0f);
                    hsb[2] = Math.max(Math.min(1.0f - (restrictedY - (this.y + 18)) / 108.0f, 1.0f), 0.0f);
                }
                else if (hue && !((JColor)this.setting.getValue()).isRainbow()) {
                    final float restrictedX = (float)Math.min(Math.max(this.x - 2, mouseX), this.x + 104);
                    hsb[0] = Math.min((restrictedX - this.x - 2.0f) / 104.0f, 1.0f);
                }
                else if (alpha) {
                    final float restrictedX = (float)Math.min(Math.max(this.x - 2, mouseX), this.x + 104);
                    alphas = (int)(Math.min(1.0f - (restrictedX - this.x - 2.0f) / 104.0f, 1.0f) * 255.0f);
                }
            }
            Screen.abstractTheme.drawPickerButton(this, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 142, this.isHovered);
            this.color = ColorUtil.injectAlpha(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])), alphas);
            this.setting.setValue((Object)new JColor(this.color, ((JColor)this.setting.getValue()).isRainbow()));
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.button.panel.update();
        }
        if (this.isHovered(mouseX, mouseY) && button == 0 && this.open) {
            this.dragging = true;
            if (mouseX > this.x && mouseX < this.x + 110 && mouseY > this.y + 140 && mouseY < this.y + this.getHeight()) {
                ((JColor)this.setting.getValue()).setRainbow(!((JColor)this.setting.getValue()).isRainbow());
            }
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 150;
        }
        return 12;
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 136 && y > this.y && y < this.y + this.getHeight();
    }
    
    private void drawPicker(final int mouseX, final int mouseY) {
        final float[] hsb = { Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[0], Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[1], Color.RGBtoHSB(((JColor)this.setting.getValue()).getColor().getRed(), ((JColor)this.setting.getValue()).getColor().getGreen(), ((JColor)this.setting.getValue()).getColor().getBlue(), null)[2] };
        int alphas = ((JColor)this.setting.getValue()).getColor().getAlpha();
        final boolean picker = mouseX > this.button.panel.getX() + 2 && mouseX < this.button.panel.getX() + 108 && mouseY > this.button.panel.getY() + this.offset + 18 && mouseY < this.button.panel.getY() + this.offset + 126;
        final boolean hue = mouseX > this.button.panel.getX() + 2 && mouseX < this.button.panel.getX() + 109 && mouseY > this.button.panel.getY() + this.offset + 126 && mouseY < this.button.panel.getY() + this.offset + 134;
        final boolean alpha = mouseX > this.button.panel.getX() + 2 && mouseX < this.button.panel.getX() + 109 && mouseY > this.button.panel.getY() + this.offset + 134 && mouseY < this.button.panel.getY() + this.offset + 141;
        if (this.dragging) {
            if (picker) {
                final float restrictedX = (float)Math.min(Math.max(this.button.panel.getX() + 2, mouseX), this.button.panel.getX() + 2 + 106);
                final float restrictedY = (float)Math.min(Math.max(this.button.panel.getY() + this.offset + 18, mouseY), this.button.panel.getY() + this.offset + 18 + 106);
                hsb[1] = Math.max(Math.min((restrictedX - this.button.panel.getX() + 2.0f) / 106.0f, 1.0f), 0.0f);
                hsb[2] = Math.max(Math.min(1.0f - (restrictedY - (this.y + 18)) / 108.0f, 1.0f), 0.0f);
            }
            else if (hue && !((JColor)this.setting.getValue()).isRainbow()) {
                final float restrictedX = (float)Math.min(Math.max(this.button.panel.getX() - 2, mouseX), this.button.panel.getX() + 104);
                hsb[0] = Math.min((restrictedX - this.button.panel.getX() - 2.0f) / 104.0f, 1.0f);
            }
            else if (alpha) {
                final float restrictedX = (float)Math.min(Math.max(this.button.panel.getX() - 2, mouseX), this.button.panel.getX() + 104);
                alphas = (int)(Math.min(1.0f - (restrictedX - this.button.panel.getX() - 2.0f) / 104.0f, 1.0f) * 255.0f);
            }
        }
        final int selectedColor = Color.HSBtoRGB(hsb[0], 1.0f, 1.0f);
        final float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        final float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        final float selectedBlue = (selectedColor & 0xFF) / 255.0f;
        Gui.drawRect(this.button.panel.getX(), this.button.panel.getY() + this.offset + 12, this.button.panel.getX() + 110, this.button.panel.getY() + this.offset + 150, new Color(0, 0, 0, 140).getRGB());
        this.drawPickerBase(this.button.panel.getX() + 2, this.button.panel.getY() + this.offset + 18, 106, 108, selectedRed, selectedGreen, selectedBlue, 1.0f);
        this.drawHueSlider(this.button.panel.getX() + 2, this.button.panel.getY() + this.offset + 127, 109, 6, hsb[0]);
        this.drawAlphaSlider(this.button.panel.getX() + 2, this.button.panel.getY() + this.offset + 134, 105, 6, selectedRed, selectedGreen, selectedBlue, alphas / 255.0f);
        final float xPos = this.button.panel.getX() + 2 + hsb[1] * 106.0f;
        final float yPos = this.button.panel.getY() + this.offset + 18 + 108 - hsb[2] * 108.0f;
        GlStateManager.pushMatrix();
        Renderer2D.drawPolygonOutline(xPos - 2.0f, yPos + 2.0f, 2.0, 3, 360, Color.black.getRGB());
        GlStateManager.popMatrix();
        ColorButton.mc.fontRenderer.drawStringWithShadow("Rainbow", (float)(int)(this.button.panel.getX() + this.button.panel.getWidth() / 2.0f - ColorButton.mc.fontRenderer.getStringWidth("Rainbow") / 2.0f), (float)(this.button.panel.getY() + this.offset + 142), ((JColor)this.setting.getValue()).isRainbow() ? ((JColor)this.setting.getValue()).getColor().getRGB() : -1);
        this.color = this.injectAlpha(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])), alphas);
    }
    
    private Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    private void drawPickerBase(final int pickerX, final int pickerY, final int pickerWidth, final int pickerHeight, final float red, final float green, final float blue, final float alpha) {
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
    
    private void drawHueSlider(final int x, final int y, final int width, final int height, final float hue) {
        int step = 0;
        for (int colorIndex = 0; colorIndex < 5; ++colorIndex) {
            final int previousStep = Color.HSBtoRGB(step / 5.0f, 1.0f, 1.0f);
            final int nextStep = Color.HSBtoRGB((step + 1) / 5.0f, 1.0f, 1.0f);
            Renderer2D.drawHGradientRect((float)(x + step * (width / 5)), (float)y, (float)(x + (step + 1) * (width / 5)), (float)(y + height), previousStep, nextStep);
            ++step;
        }
        final int sliderMinX = (int)(x + width * hue);
        Renderer2D.drawRect((float)(sliderMinX - 1), (float)y, (float)(sliderMinX + 1), (float)(y + height), -1);
        Renderer2D.drawOutline(sliderMinX - 1, y, sliderMinX + 1, y + height, 1.0f, Color.BLACK.getRGB());
    }
    
    private void drawAlphaSlider(final int x, final int y, final int width, final int height, final float red, final float green, final float blue, final float alpha) {
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
    
    private void drawLeftGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
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
