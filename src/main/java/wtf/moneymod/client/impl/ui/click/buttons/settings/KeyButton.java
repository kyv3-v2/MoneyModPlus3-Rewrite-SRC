package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.impl.ui.click.buttons.*;
import wtf.moneymod.client.impl.ui.click.*;

public class KeyButton extends Component
{
    private boolean binding;
    public final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    
    public KeyButton(final ModuleButton button, final int offset) {
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
            this.binding = !this.binding;
        }
    }
    
    @Override
    public void keyTyped(final int key) {
        if (this.binding) {
            if (key == 211) {
                this.button.module.setKey(0);
            }
            else {
                this.button.module.setKey(key);
            }
            this.binding = false;
        }
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawKeyButton(this, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 12, this.isHovered(mouseX, mouseY));
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }
    
    public boolean isBinding() {
        return this.binding;
    }
}
