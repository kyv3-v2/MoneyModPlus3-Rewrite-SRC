package wtf.moneymod.client.impl.ui.click.theme.impl;

import wtf.moneymod.client.impl.ui.click.theme.*;
import wtf.moneymod.client.impl.ui.click.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import wtf.moneymod.client.impl.module.global.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.api.setting.*;
import com.mojang.realmsclient.gui.*;
import org.lwjgl.input.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.*;

public class WurstTheme extends AbstractTheme
{
    private static final WurstTheme INSTANCE;
    
    public static WurstTheme getInstance() {
        return WurstTheme.INSTANCE;
    }
    
    public void drawHeader(final Panel panel, final int x, final int y, final int w, final int h, final boolean hovered) {
        drawRect((float)(x - 2), (float)(y - 2), (float)(x + w + 2), (float)y, new Color(255, 111, 0, 199).getRGB());
        drawRect((float)x, (float)y, (float)(x + w), (float)(y + h - 2), new Color(255, 111, 0, 199).getRGB());
        if (panel.isOpen()) {
            Gui.drawRect(x, y + h - 2, x + w, y + h, new Color(255, 111, 0, 199).getRGB());
        }
        WurstTheme.mc.fontRenderer.drawStringWithShadow(panel.category.name(), (float)(x + 3), (float)(y + 4), -1);
        WurstTheme.mc.fontRenderer.drawStringWithShadow(panel.isOpen() ? "-" : "+", (float)(x + w - 10), (float)(y + 4), -1);
    }
    
    public void drawPanelOutline(final Panel panel, final int x, final int y, final int w, final int h, final boolean hovered) {
        final int localHeight = panel.isOpen() ? h : 16;
        drawRect((float)(x - 2), (float)y, (float)x, (float)(y + localHeight), new Color(255, 111, 0, 199).getRGB());
        drawRect((float)(x + w), (float)y, (float)(x + w + 2), (float)(y + localHeight), new Color(255, 111, 0, 199).getRGB());
        drawRect((float)(x - 2), (float)(y + localHeight), (float)(x + w + 2), (float)(y + localHeight + 2), new Color(255, 111, 0, 199).getRGB());
    }
    
    public void drawModuleButton(final ModuleButton module, final int x, final int y, final int w, final int h, final boolean hovered) {
        drawRect((float)x, (float)y, (float)(x + w), (float)(y + h), new Color(255, 111, 0, 199).getRGB());
        if (module.module.isToggled()) {
            drawRect((float)x, (float)(y + 1), (float)(x + w), (float)(y + h - 1), new Color(255, 255, 255, 180).getRGB());
        }
        WurstTheme.mc.fontRenderer.drawStringWithShadow(module.module.getLabel(), (float)(x + 3), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
    }
    
    public void drawBooleanButton(final Option<Boolean> container, final int x, final int y, final int w, final int h, final boolean hovered) {
        drawRect((float)x, (float)y, (float)(x + w), (float)(y + 12), new Color(255, 111, 0, 199).getRGB());
        if (container.getValue()) {
            drawRect((float)x, (float)(y + 1), (float)(x + w), (float)(y + h - 1), new Color(255, 255, 255, 183).getRGB());
        }
        WurstTheme.mc.fontRenderer.drawStringWithShadow(container.getName(), (float)(x + 3), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
    }
    
    public void drawSliderButton(final Option<Number> container, final int x, final int y, final int w, final int h, final double sliderWidth, final boolean hovered) {
        Gui.drawRect(x, y, x + w, y + h, new Color(255, 111, 0, 199).getRGB());
        Gui.drawRect(x, y + h - 1, (int)(x + 6 + sliderWidth), y + 1, new Color(255, 255, 255, 183).getRGB());
        WurstTheme.mc.fontRenderer.drawStringWithShadow(container.getName() + ": " + ChatFormatting.GRAY + container.getValue(), (float)(x + 3), y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 0.0f : 1.5f) : 1.5f), -1);
    }
    
    public void drawKeyButton(final KeyButton button, final int x, final int y, final int w, final int h, final boolean hovered) {
        drawRect((float)x, (float)y, (float)(x + w), (float)(y + h), new Color(255, 111, 0, 199).getRGB());
        WurstTheme.mc.fontRenderer.drawStringWithShadow("Key", (float)(x + 5), (float)(y + 2), -1);
        if (button.isBinding()) {
            WurstTheme.mc.fontRenderer.drawStringWithShadow("...", (float)(x + w - 5 - WurstTheme.mc.fontRenderer.getStringWidth("...")), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
        }
        else {
            String key = null;
            switch (button.button.module.getKey()) {
                case 345: {
                    key = "RCtrl";
                    break;
                }
                case 341: {
                    key = "Ctrl";
                    break;
                }
                case 346: {
                    key = "RAlt";
                    break;
                }
                case -1: {
                    key = "NONE";
                    break;
                }
                default: {
                    key = Keyboard.getKeyName(button.button.module.getKey());
                    break;
                }
            }
            WurstTheme.mc.fontRenderer.drawStringWithShadow(key, (float)(x + w - 3 - WurstTheme.mc.fontRenderer.getStringWidth(key)), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
        }
    }
    
    public void drawModeButton(final Option<Enum> container, final int x, final int y, final int w, final int h, final boolean hovered) {
        Gui.drawRect(x, y, x + w, y + h, new Color(255, 111, 0, 199).getRGB());
        WurstTheme.mc.fontRenderer.drawStringWithShadow(container.getName(), (float)(x + 3), (float)(y + 2), -1);
        WurstTheme.mc.fontRenderer.drawStringWithShadow(SettingUtils.INSTANCE.getProperName((Enum)container.getValue()), (float)(x + w - 5 - WurstTheme.mc.fontRenderer.getStringWidth(SettingUtils.INSTANCE.getProperName((Enum)container.getValue()))), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
    }
    
    public void drawSubModeButton(final SubMode container, final String current, final int x, final int y, final int w, final int h, final boolean hovered) {
        Gui.drawRect(x, y, x + w, y + h, new Color(255, 111, 0, 199).getRGB());
        WurstTheme.mc.fontRenderer.drawStringWithShadow(current, x + w / 2.0f - WurstTheme.mc.fontRenderer.getStringWidth(current) / 2.0f, (float)(y + 2), -1);
    }
    
    public void drawColorButton(final Option<JColor> container, final int x, final int y, final int w, final int h, final boolean hovered) {
        Gui.drawRect(x, y, x + w, y + h, new Color(255, 111, 0, 199).getRGB());
        drawVGradientRect((float)(x + w - h), (float)(y + 2), (float)(x + w - 4), (float)(y + 10), ColorUtil.injectAlpha(((JColor)container.getValue()).getColor(), 255).getRGB(), ((JColor)container.getValue()).getColor().getRGB());
        drawOutline((double)(x + w - h), (double)(y + 2), (double)(x + w - 4), (double)(y + 10), 1.0f, new Color(255, 111, 0, 199).getRGB());
        WurstTheme.mc.fontRenderer.drawStringWithShadow(container.getName(), (float)(x + 3), (float)(y + (((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).bounding ? (hovered ? 1 : 2) : 2)), -1);
    }
    
    public void drawPickerButton(final ColorButton container, final int x, final int y, final int w, final int h, final boolean hovered) {
        final float[] hsb = { Color.RGBtoHSB(((JColor)container.setting.getValue()).getColor().getRed(), ((JColor)container.setting.getValue()).getColor().getGreen(), ((JColor)container.setting.getValue()).getColor().getBlue(), null)[0], Color.RGBtoHSB(((JColor)container.setting.getValue()).getColor().getRed(), ((JColor)container.setting.getValue()).getColor().getGreen(), ((JColor)container.setting.getValue()).getColor().getBlue(), null)[1], Color.RGBtoHSB(((JColor)container.setting.getValue()).getColor().getRed(), ((JColor)container.setting.getValue()).getColor().getGreen(), ((JColor)container.setting.getValue()).getColor().getBlue(), null)[2] };
        final int alphas = ((JColor)container.setting.getValue()).getColor().getAlpha();
        final int selectedColor = Color.HSBtoRGB(hsb[0], 1.0f, 1.0f);
        final float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        final float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        final float selectedBlue = (selectedColor & 0xFF) / 255.0f;
        Gui.drawRect(x, y + 12, x + 110, y + 150, new Color(255, 111, 0, 199).getRGB());
        this.drawPickerBase(x + 2, y + 18, 106, 108, selectedRed, selectedGreen, selectedBlue, 1.0f);
        this.drawHueSlider(x + 2, y + 127, 109, 6, hsb[0]);
        this.drawAlphaSlider(x + 2, y + 134, 105, 6, selectedRed, selectedGreen, selectedBlue, alphas / 255.0f);
        final float xPos = x + 2 + hsb[1] * 106.0f;
        final float yPos = y + 18 + 108 - hsb[2] * 108.0f;
        pushMatrix();
        drawPolygonOutline((double)(xPos - 2.0f), (double)(yPos + 2.0f), 2.0, 3, 360, Color.black.getRGB());
        popMatrix();
        WurstTheme.mc.fontRenderer.drawStringWithShadow("Rainbow", (float)(int)(x + w / 2.0f - WurstTheme.mc.fontRenderer.getStringWidth("Rainbow") / 2.0f), (float)(y + h), ((JColor)container.setting.getValue()).isRainbow() ? ((JColor)container.setting.getValue()).getColor().getRGB() : -1);
    }
    
    static {
        INSTANCE = new WurstTheme();
    }
}
