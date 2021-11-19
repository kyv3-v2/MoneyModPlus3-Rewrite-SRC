package wtf.moneymod.client.impl.ui.click.buttons;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import wtf.moneymod.client.impl.ui.click.buttons.settings.*;
import java.util.*;
import wtf.moneymod.client.impl.ui.click.*;
import wtf.moneymod.client.*;

public class ModuleButton extends Component
{
    public Module module;
    public Panel panel;
    public int offset;
    private boolean isHovered;
    public final ArrayList<Component> components;
    public boolean open;
    private String openText;
    
    public ModuleButton(final Module module, final Panel panel, final int offset) {
        this.openText = "+";
        this.module = module;
        this.panel = panel;
        this.offset = offset;
        this.components = new ArrayList<Component>();
        this.open = false;
        int settingY = this.offset + 12;
        if (!Option.getContainersForObject((Object)module).isEmpty()) {
            for (final Option s : Option.getContainersForObject((Object)module)) {
                if (s.getValue().getClass().isEnum()) {
                    this.components.add(new ModeButton((Option<Enum>)s, this, settingY));
                }
                else {
                    final String simpleName = s.getValue().getClass().getSimpleName();
                    switch (simpleName) {
                        case "Boolean": {
                            this.components.add(new BooleanButton((Option<Boolean>)s, this, settingY));
                            continue;
                        }
                        case "Double":
                        case "Integer":
                        case "Float": {
                            this.components.add(new SliderButton((Option<Number>)s, this, settingY));
                            continue;
                        }
                        case "JColor": {
                            this.components.add(new ColorButton((Option<JColor>)s, this, settingY));
                            continue;
                        }
                        default: {
                            settingY += 12;
                            continue;
                        }
                    }
                }
            }
        }
        this.components.add(new KeyButton(this, settingY));
    }
    
    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
        int settingY = this.offset + 12;
        for (final Component c : this.components) {
            c.setOffset(settingY);
            settingY += c.getHeight();
        }
    }
    
    @Override
    public int getHeight() {
        int height = 12;
        if (this.open) {
            for (final Component component : this.components) {
                height += component.getHeight();
            }
        }
        return height;
    }
    
    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        this.isHovered = this.isHovered(mouseX, mouseY);
        if (!this.components.isEmpty()) {
            this.components.forEach(c -> c.updateComponent(mouseX, mouseY));
        }
    }
    
    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (this.isHovered(mouseX, mouseY) && button == 0) {
            this.module.toggle();
        }
        if (this.isHovered(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.panel.update();
        }
        this.components.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
    }
    
    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        this.components.forEach(c -> c.mouseReleased(mouseX, mouseY, mouseButton));
    }
    
    @Override
    public void keyTyped(final int key) {
        this.components.forEach(c -> c.keyTyped(key));
    }
    
    @Override
    public void render(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawModuleButton(this, this.panel.getX(), this.panel.getY() + this.offset, this.panel.getWidth(), 12, this.isHovered(mouseX, mouseY));
        if (this.open) {
            this.components.forEach(component -> component.render(mouseX, mouseY));
        }
        if (this.isHovered) {
            Main.getMain().getScreen().getDescriptionManager().update(this.module.getDesc(), mouseX, mouseY);
        }
    }
    
    public boolean isHovered(final double x, final double y) {
        return x > this.panel.getX() && x < this.panel.getX() + this.panel.getWidth() && y > this.panel.getY() + this.offset && y < this.panel.getY() + 12 + this.offset;
    }
}
