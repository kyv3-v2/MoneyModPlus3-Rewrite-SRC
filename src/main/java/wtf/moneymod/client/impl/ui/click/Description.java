package wtf.moneymod.client.impl.ui.click;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.impl.module.global.*;
import wtf.moneymod.client.*;
import java.awt.*;
import net.minecraft.client.gui.*;

public class Description implements Globals
{
    private boolean draw;
    private int mouseX;
    private int mouseY;
    private String text;
    
    public void reset() {
        this.draw = false;
        this.mouseX = 0;
        this.mouseY = 0;
        this.text = "";
    }
    
    public void update(final String text, final int mouseX, final int mouseY) {
        this.draw = true;
        this.text = text;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    
    public void draw() {
        if (this.draw) {
            final ClickGui clickgui = (ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class);
            if (clickgui.desc && this.text.length() > 0) {
                final ScaledResolution sr = new ScaledResolution(Description.mc);
                final int width = Description.mc.fontRenderer.getStringWidth(this.text);
                final int height = Description.mc.fontRenderer.FONT_HEIGHT;
                boolean left = false;
                if (this.mouseX + width >= sr.getScaledWidth_double()) {
                    left = true;
                }
                final int startx = left ? (this.mouseX - width - 2) : (this.mouseX + 2);
                Gui.drawRect(startx, this.mouseY - height - 1, startx + width + 3, this.mouseY + 1, new Color(0, 0, 0, 170).getRGB());
                Description.mc.fontRenderer.drawStringWithShadow(this.text, (float)(startx + 2), (float)(this.mouseY - height + 1), new Color(255, 255, 255, 255).getRGB());
            }
        }
    }
}
