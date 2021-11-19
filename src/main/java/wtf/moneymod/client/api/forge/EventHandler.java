package wtf.moneymod.client.api.forge;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.*;
import org.lwjgl.input.*;
import wtf.moneymod.client.impl.module.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import wtf.moneymod.client.impl.command.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import wtf.moneymod.client.api.setting.*;
import java.awt.*;
import wtf.moneymod.client.impl.utility.impl.render.*;

public class EventHandler implements Globals
{
    @SubscribeEvent
    public void onInput(final InputEvent.KeyInputEvent event) {
        Main.getMain().getModuleManager().get(module -> Keyboard.getEventKey() != 0 && Keyboard.getEventKeyState() && Keyboard.getEventKey() == module.getKey()).forEach(Module::toggle);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || this.nullCheck()) {
            return;
        }
        Main.getMain().getModuleManager().get(Module::isToggled).forEach(Module::onTick);
    }
    
    @SubscribeEvent
    public void onClientChat(final ClientChatEvent event) {
        final String message = event.getMessage();
        if (message.startsWith(Main.getMain().getCommandManagement().getPrefix())) {
            final String temp = message.substring(1);
            for (final Command command : Main.getMain().getCommandManagement()) {
                final String[] split = temp.split(" ");
                for (final String name : command.getAlias()) {
                    if (name.equalsIgnoreCase(split[0])) {
                        command.execute(split);
                        event.setCanceled(true);
                        EventHandler.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                        return;
                    }
                }
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRenderUpdate(final RenderWorldLastEvent event) {
        for (final Module m : Main.getMain().getModuleManager()) {
            for (final Option<?> setting : Option.getContainersForObject(m)) {
                if (setting.getValue() instanceof JColor) {
                    final JColor color = (JColor)setting.getValue();
                    final float[] hsb = Color.RGBtoHSB(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), null);
                    if (!color.isRainbow()) {
                        continue;
                    }
                    setting.setValue(new JColor(ColorUtil.injectAlpha(ColorUtil.rainbowColor(0, hsb[1], hsb[2]), color.getColor().getAlpha()), color.isRainbow()));
                }
            }
        }
    }
}
