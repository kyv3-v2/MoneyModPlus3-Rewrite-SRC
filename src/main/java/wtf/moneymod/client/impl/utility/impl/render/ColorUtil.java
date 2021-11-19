package wtf.moneymod.client.impl.utility.impl.render;

import java.awt.*;
import org.lwjgl.opengl.*;

public class ColorUtil
{
    public static Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static Color injectBrightness(final Color color, final float brightness) {
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], brightness);
    }
    
    public static Color rainbowColor(final int delay, final float s, final float b) {
        return Color.getHSBColor((System.currentTimeMillis() + delay) % 11520L / 11520.0f, s, b);
    }
    
    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static Color releasedDynamicRainbow(final int delay, final int alpha) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;
        final Color c = Color.getHSBColor((float)(rainbowState / 360.0), 1.0f, 1.0f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
}
