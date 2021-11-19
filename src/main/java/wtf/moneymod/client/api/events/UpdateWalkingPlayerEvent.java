package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.*;

public class UpdateWalkingPlayerEvent extends Event
{
    private final int stage;
    
    public UpdateWalkingPlayerEvent(final int stage) {
        this.stage = stage;
    }
    
    public int getStage() {
        return this.stage;
    }
}
