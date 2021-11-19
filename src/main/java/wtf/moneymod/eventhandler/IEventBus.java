package wtf.moneymod.eventhandler;

import wtf.moneymod.eventhandler.event.*;

public interface IEventBus
{
    void register(final Object p0);
    
    void unregister(final Object p0);
    
    void dispatch(final Event p0);
}
