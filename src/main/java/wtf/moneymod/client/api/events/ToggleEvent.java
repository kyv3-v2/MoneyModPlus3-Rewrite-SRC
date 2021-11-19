package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.impl.module.*;

public class ToggleEvent extends Event
{
    private final Action action;
    private final Module module;
    
    public ToggleEvent(final Action action, final Module module) {
        this.action = action;
        this.module = module;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public enum Action
    {
        ENABLE, 
        DISABLE;
    }
}
