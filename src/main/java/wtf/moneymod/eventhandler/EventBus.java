package wtf.moneymod.eventhandler;

import wtf.moneymod.eventhandler.event.*;
import java.util.concurrent.*;
import wtf.moneymod.eventhandler.listener.*;
import java.lang.annotation.*;
import wtf.moneymod.eventhandler.event.enums.*;
import java.util.*;
import java.lang.reflect.*;

public class EventBus implements IEventBus
{
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<Listener>> listeners;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    public EventBus() {
        this.listeners = new ConcurrentHashMap<Class<? extends Event>, CopyOnWriteArrayList<Listener>>();
    }
    
    @Override
    public void register(final Object obj) {
        Listener listener;
        Class event;
        Arrays.stream(obj.getClass().getFields()).filter(field -> field.isAnnotationPresent(Handler.class)).forEach(field -> {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            listener = null;
            try {
                listener = (Listener)field.get(obj);
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            if (!EventBus.$assertionsDisabled && listener == null) {
                throw new AssertionError();
            }
            else {
                event = listener.getEvent();
                listener.setObject(obj);
                listener.setField(field);
                if (!this.listeners.containsKey(event)) {
                    this.listeners.put(event, new CopyOnWriteArrayList<Listener>());
                }
                this.listeners.get(event).add(listener);
                this.listeners.get(event).sort(Comparator.comparing(e -> -e.getHandler().priority().priority));
            }
        });
    }
    
    @Override
    public void unregister(final Object obj) {
        this.listeners.values().forEach(arrayList -> arrayList.removeIf(listener -> listener.getObject().equals(obj)));
    }
    
    @Override
    public void dispatch(final Event event) {
        final List<Listener> listenersList = this.listeners.get(event.getClass());
        if (listenersList != null) {
            for (final Listener listener : listenersList) {
                if (event.isCancelled()) {
                    return;
                }
                if (listener.getHandler().era() != Era.NONE && listener.getHandler().era() != event.getEra()) {
                    continue;
                }
                listener.getConsumer().accept(event);
            }
        }
    }
}
