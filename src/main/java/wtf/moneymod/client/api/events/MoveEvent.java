package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.*;

public class MoveEvent extends Event
{
    public double motionX;
    public double motionY;
    public double motionZ;
    
    public MoveEvent(final double motionX, final double motionY, final double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}
