package wtf.moneymod.client.impl.ui.click;

import java.awt.*;
import wtf.moneymod.client.impl.ui.click.theme.*;
import wtf.moneymod.client.impl.ui.click.theme.impl.*;
import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.impl.module.global.*;
import wtf.moneymod.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class Screen extends GuiScreen
{
    public static List<Panel> panels;
    public static Color color;
    public static Description description;
    public static AbstractTheme abstractTheme;
    
    public Screen() {
        Screen.abstractTheme = new NodusTheme();
        Screen.panels = new ArrayList<Panel>();
        Screen.description = new Description();
        int frameX = 10;
        for (final Module.Category category : Module.Category.values()) {
            final Panel panel = new Panel(category);
            panel.setY(10);
            panel.setX(frameX);
            Screen.panels.add(panel);
            frameX += panel.getWidth() + 10;
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Screen.abstractTheme = ((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).theme.getTheme();
        Screen.description.reset();
        if (this.mc.player != null) {
            this.doScroll();
        }
        final ClickGui globals = (ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class);
        Screen.color = globals.color.getColor();
        Screen.panels.forEach(panel -> {
            panel.renderPanel(mouseX, mouseY);
            panel.updatePosition(mouseX, mouseY);
            panel.getComponents().forEach(c -> c.updateComponent((double)mouseX, (double)mouseY));
            return;
        });
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Screen.description.draw();
    }
    
    private void doScroll() {
        final int n = Mouse.getDWheel();
        if (n >= 0) {
            if (n > 0) {
                for (final Panel panel : Screen.panels) {
                    panel.setY(panel.getY() + 8);
                }
            }
        }
        else {
            for (final Panel panel : Screen.panels) {
                panel.setY(panel.getY() - 8);
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Panel panel : Screen.panels) {
            if (panel.isHover((double)mouseX, (double)mouseY) && mouseButton == 0) {
                panel.setDrag(true);
                panel.dragX = mouseX - panel.getX();
                panel.dragY = mouseY - panel.getY();
            }
            if (panel.isHover((double)mouseX, (double)mouseY) && mouseButton == 1) {
                panel.setOpen(!panel.isOpen());
            }
            if (panel.isOpen() && !panel.getComponents().isEmpty()) {
                for (final Component component : panel.getComponents()) {
                    component.mouseClicked((double)mouseX, (double)mouseY, mouseButton);
                }
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Panel panel : Screen.panels) {
            panel.setDrag(false);
        }
        for (final Panel panel : Screen.panels) {
            if (panel.isOpen() && !panel.getComponents().isEmpty()) {
                for (final Component component : panel.getComponents()) {
                    component.mouseReleased((double)mouseX, (double)mouseY, state);
                }
            }
        }
    }
    
    public void drawGradient(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        this.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        for (final Panel panel : Screen.panels) {
            if (panel.isOpen() && keyCode != 1 && !panel.getComponents().isEmpty()) {
                for (final Component component : panel.getComponents()) {
                    component.keyTyped(keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }
    
    public void onGuiClosed() {
        if (this.mc.entityRenderer.isShaderActive()) {
            this.mc.entityRenderer.stopUseShader();
        }
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public Description getDescriptionManager() {
        return Screen.description;
    }
    
    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer && ((ClickGui)Main.getMain().getModuleManager().get((Class)ClickGui.class)).blur) {
            if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
}
