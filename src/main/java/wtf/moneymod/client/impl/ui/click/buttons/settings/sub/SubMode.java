package wtf.moneymod.client.impl.ui.click.buttons.settings.sub;

import wtf.moneymod.client.impl.ui.click.buttons.settings.*;
import wtf.moneymod.client.impl.ui.click.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;

public class SubMode extends Component
{
    final ModeButton modeButton;
    final String mode;
    int offset;
    
    public SubMode(final ModeButton modeButton, final String mode, final int offset) {
        this.mode = mode;
        this.offset = offset;
        this.modeButton = modeButton;
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawSubModeButton(this, this.mode, this.modeButton.getButton().panel.getX(), this.modeButton.getButton().panel.getY() + this.modeButton.getOffset() + this.offset, this.modeButton.getButton().panel.getWidth(), 12, this.isHover(mouseX, mouseY));
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (this.isHover(mouseX, mouseY) && this.modeButton.getButton().open && this.modeButton.isOpen() && button == 0) {
            this.modeButton.getSetting().setValue((Object)SettingUtils.INSTANCE.getProperEnum((Enum)this.modeButton.getSetting().getValue(), this.mode));
        }
    }
    
    private boolean isHover(final double x, final double y) {
        return x > this.modeButton.getX() && x < this.modeButton.getX() + 110 && y > this.modeButton.getY() + this.offset && y < this.modeButton.getY() + 12 + this.offset;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public String getMode() {
        return this.mode;
    }
    
    public ModeButton getModeButton() {
        return this.modeButton;
    }
}
