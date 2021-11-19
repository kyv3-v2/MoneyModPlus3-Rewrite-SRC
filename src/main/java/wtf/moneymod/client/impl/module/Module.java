package wtf.moneymod.client.impl.module;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.*;
import net.minecraftforge.common.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.event.*;
import java.lang.annotation.*;

public class Module implements Globals
{
    private final String label;
    private final String desc;
    private final Category category;
    private boolean toggled;
    private final boolean configException;
    private int key;
    
    public Module() {
        final Register register = this.getClass().getAnnotation(Register.class);
        this.label = register.label();
        this.desc = register.desc();
        this.category = register.cat();
        this.configException = register.exception();
        this.key = register.key();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public boolean isConfigException() {
        return this.configException;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    protected void onEnable() {
    }
    
    protected void onDisable() {
    }
    
    protected void onToggle() {
    }
    
    public void onTick() {
    }
    
    public void enable() {
        this.toggled = true;
        this.onToggle();
        this.onEnable();
        Main.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
        Main.EVENT_BUS.dispatch((Event)new ToggleEvent(ToggleEvent.Action.ENABLE, this));
    }
    
    public void disable() {
        this.toggled = false;
        this.onToggle();
        this.onDisable();
        Main.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Main.EVENT_BUS.dispatch((Event)new ToggleEvent(ToggleEvent.Action.DISABLE, this));
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public void setToggled(final boolean toggled) {
        if (toggled) {
            this.enable();
        }
        else {
            this.disable();
        }
    }
    
    public void toggle() {
        this.setToggled(!this.toggled);
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        PLAYER("Player"), 
        MOVEMENT("Movement"), 
        MISC("Misc"), 
        RENDER("Render"), 
        GLOBAL("Global");
        
        private final String name;
        
        private Category(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Register {
        String label();
        
        Category cat();
        
        String desc() default "";
        
        boolean exception() default false;
        
        int key() default 0;
    }
}
