package wtf.moneymod.eventhandler.event;

import wtf.moneymod.eventhandler.event.enums.*;

public abstract class Event
{
    private boolean cancelled;
    private Era era;
    
    public Event() {
        this.era = Era.NONE;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void cancel() {
        this.setCancelled(true);
    }
    
    public void setEra(final Era era) {
        this.era = era;
    }
    
    public Era getEra() {
        return this.era;
    }
}
