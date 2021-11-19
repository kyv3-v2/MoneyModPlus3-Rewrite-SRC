package wtf.moneymod.eventhandler.listener;

import wtf.moneymod.eventhandler.event.*;
import java.util.function.*;
import java.lang.reflect.*;

public class Listener<T extends Event>
{
    private Consumer<T> consumer;
    private final Class<? extends Event> event;
    private Object object;
    private Field field;
    
    public Listener(final Class<? extends Event> event, final Consumer<T> consumer) {
        this.consumer = consumer;
        this.event = event;
    }
    
    public Consumer<T> getConsumer() {
        return this.consumer;
    }
    
    public Class<? extends Event> getEvent() {
        return this.event;
    }
    
    public Object getObject() {
        return this.object;
    }
    
    public void setObject(final Object object) {
        this.object = object;
    }
    
    public Field getField() {
        return this.field;
    }
    
    public void setField(final Field field) {
        this.field = field;
    }
    
    public Handler getHandler() {
        return this.field.getAnnotation(Handler.class);
    }
}
