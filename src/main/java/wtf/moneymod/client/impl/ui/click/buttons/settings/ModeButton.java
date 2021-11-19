package wtf.moneymod.client.impl.ui.click.buttons.settings;

import wtf.moneymod.client.api.setting.*;
import java.util.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.sub.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.impl.ui.click.*;

public class ModeButton extends Component
{
    private final Option<Enum> setting;
    private final ArrayList<SubMode> modes;
    private final ModuleButton button;
    private boolean isHovered;
    private boolean open;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;
    
    public ModeButton(final Option<Enum> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.panel.getX() + button.panel.getWidth();
        this.y = button.panel.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
        this.open = false;
        this.modes = new ArrayList<SubMode>();
        int off = 12;
        for (final Enum e : (Enum[])((Enum)setting.getValue()).getClass().getEnumConstants()) {
            this.modes.add(new SubMode(this, SettingUtils.INSTANCE.getProperName(e), off));
            off += 12;
        }
    }
    
    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        this.modes.forEach(v -> v.mouseClicked(mouseX, mouseY, button));
        if (this.isHovered(mouseX, mouseY) && this.button.open) {
            if (button == 0) {
                this.setting.setValue((Object)SettingUtils.INSTANCE.increaseEnum((Enum)this.setting.getValue()));
            }
            if (button == 1) {
                this.open = !this.open;
                this.button.panel.update();
            }
        }
    }
    
    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        this.isHovered = this.isHovered(mouseX, mouseY);
        this.y = this.button.panel.getY() + this.offset;
        this.x = this.button.panel.getX();
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawModeButton(this.setting, this.button.panel.getX(), this.button.panel.getY() + this.offset, this.button.panel.getWidth(), 12, this.isHovered);
        if (this.open) {
            this.modes.forEach(m -> m.render(mouseX, mouseY));
        }
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 110 && y > this.y && y < this.y + 12;
    }
    
    @Override
    public int getHeight() {
        if (this.open) {
            return 12 + ((Enum[])((Enum)this.setting.getValue()).getClass().getEnumConstants()).length * 12;
        }
        return super.getHeight();
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public ModuleButton getButton() {
        return this.button;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public Option<Enum> getSetting() {
        return this.setting;
    }
}
