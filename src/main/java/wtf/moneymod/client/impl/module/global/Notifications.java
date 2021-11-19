package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import java.util.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import com.mojang.realmsclient.gui.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.*;

@Register(label = "Notifications", cat = Category.GLOBAL)
public class Notifications extends Module
{
    private List<Module> blacklist;
    @Handler
    public Listener<ToggleEvent> eventListener;
    
    public Notifications() {
        this.blacklist = new ArrayList<Module>();
        this.eventListener = new Listener<ToggleEvent>((Class<? extends Event>)ToggleEvent.class, e -> {
            if (!this.blacklist.contains(e.getModule()) && !this.nullCheck()) {
                if (e.getAction() == ToggleEvent.Action.ENABLE) {
                    ChatUtil.INSTANCE.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + e.getModule().getLabel() + " : " + ChatFormatting.GREEN + "Enabled", true);
                }
                else {
                    ChatUtil.INSTANCE.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + e.getModule().getLabel() + " : " + ChatFormatting.RED + "Disabled", true);
                }
            }
        });
    }
    
    public void onEnable() {
        this.blacklist.clear();
        this.blacklist.add(Main.getMain().getModuleManager().get((Class)ClickGui.class));
    }
}
