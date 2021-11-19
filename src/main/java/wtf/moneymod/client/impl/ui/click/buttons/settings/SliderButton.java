package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import java.math.*;
import wtf.moneymod.client.impl.ui.click.*;

public class SliderButton extends Component
{
    private final Option<Number> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private float renderWidth;
    
    public SliderButton(final Option<Number> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
    }
    
    private static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        this.isHovered = this.isHovered(mouseX, mouseY);
        this.y = this.button.panel.getY() + this.offset;
        this.x = this.button.panel.getX();
        final double diff = Math.min(106.0, Math.max(0.0, mouseX - this.x));
        final double min = this.setting.getMin();
        final double max = this.setting.getMax();
        final double inc = ((Number)this.setting.getValue()).getClass().getSimpleName().equalsIgnoreCase("Integer") ? 1.0 : 0.1;
        this.renderWidth = (float)(104.0 * (((inc == 1.0) ? ((float)((Number)this.setting.getValue()).intValue()) : ((Number)this.setting.getValue()).floatValue()) - min) / (max - min));
        if (this.dragging) {
            if (diff == 0.0) {
                if (inc == 1.0) {
                    this.setting.setValue((Object)(int)this.setting.getMin());
                }
                else {
                    this.setting.setValue((Object)this.setting.getMin());
                }
            }
            else {
                final float newValue = (float)round(diff / 104.0 * (max - min) + min, 1);
                final float precision = (float)(1.0 / inc);
                if (inc == 0.1) {
                    this.setting.setValue((Object)(Math.round(Math.max(min, Math.min(max, newValue)) * precision) / precision));
                }
                else {
                    this.setting.setValue((Object)(int)Math.max(min, Math.min(max, newValue)));
                }
            }
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawSliderButton(this.setting, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 12, this.renderWidth, this.isHovered);
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }
}
