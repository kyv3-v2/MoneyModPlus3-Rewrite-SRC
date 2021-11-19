package wtf.moneymod.client.impl.utility.impl.render;

import java.awt.*;

public class JColor
{
    private Color color;
    private boolean rainbow;
    
    public JColor(final Color color, final boolean rainbow) {
        this.color = color;
        this.rainbow = rainbow;
    }
    
    public JColor(final int r, final int g, final int b, final boolean rainbow) {
        this.color = new Color(r, g, b);
        this.rainbow = rainbow;
    }
    
    public JColor(final int r, final int g, final int b, final int a, final boolean rainbow) {
        this.color = new Color(r, g, b, a);
        this.rainbow = rainbow;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public boolean isRainbow() {
        return this.rainbow;
    }
    
    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
}
