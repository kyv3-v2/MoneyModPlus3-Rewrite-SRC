package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import wtf.moneymod.client.impl.ui.click.*;

public class BooleanButton extends Component
{
    private final Option<Boolean> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    
    public BooleanButton(final Option<Boolean> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
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
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            this.setting.setValue((Object)!(boolean)this.setting.getValue());
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawBooleanButton(this.setting, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 12, this.isHovered);
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }
}
