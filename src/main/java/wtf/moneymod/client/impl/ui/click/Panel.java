package wtf.moneymod.client.impl.ui.click;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.impl.ui.click.buttons.*;
import java.util.*;

public class Panel implements Globals
{
    public Module.Category category;
    public ArrayList<Component> components;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    private int height;
    
    public Panel(final Module.Category category) {
        this.category = category;
        this.components = new ArrayList<Component>();
        this.width = 110;
        this.barHeight = 18;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int componentY = this.barHeight;
        for (final Module m : Main.getMain().getModuleManager().get(category)) {
            final ModuleButton moduleButton = new ModuleButton(m, this, componentY);
            this.components.add((Component)moduleButton);
            componentY += 12;
        }
        this.update();
    }
    
    public void renderPanel(final int mouseX, final int mouseY) {
        Screen.abstractTheme.drawHeader(this, this.x, this.y, this.width, this.barHeight, this.isHover(mouseX, mouseY));
        if (this.open && !this.components.isEmpty()) {
            ModuleButton moduleButton;
            this.components.forEach(component -> {
                ((Component)component).render(mouseX, mouseY);
                if (component instanceof ModuleButton) {
                    moduleButton = component;
                }
                return;
            });
        }
        Screen.abstractTheme.drawPanelOutline(this, this.x, this.y, this.width, this.height, this.isHover(mouseX, mouseY));
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void setDrag(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void update() {
        int off = this.barHeight;
        for (final Component comp : this.components) {
            comp.setOffset(off);
            off += comp.getHeight();
        }
        this.height = off;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }
    
    public boolean isHover(final double x, final double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
    
    public int getBarHeight() {
        return this.barHeight;
    }
}
