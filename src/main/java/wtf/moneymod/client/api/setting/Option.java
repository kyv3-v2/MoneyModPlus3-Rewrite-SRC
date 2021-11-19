package wtf.moneymod.client.api.setting;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.concurrent.atomic.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import java.util.*;

public class Option<T>
{
    private final Field field;
    private final String name;
    private T currentValue;
    private final Object host;
    private final Option<Boolean> parent;
    private final List<Option<?>> children;
    private final float min;
    private final float max;
    
    private Option(final Field field, final T currentValue, final Object host, final Option<Boolean> parent) {
        this.children = new ArrayList<Option<?>>();
        final Value includeOp = field.getDeclaredAnnotation(Value.class);
        final Bounds bounds = field.getAnnotation(Bounds.class);
        (this.field = field).setAccessible(true);
        this.name = (includeOp.value().equals("") ? field.getName() : includeOp.value());
        this.currentValue = currentValue;
        this.host = host;
        this.parent = parent;
        this.min = ((bounds == null) ? 0.0f : bounds.min());
        this.max = ((bounds == null) ? 0.0f : bounds.max());
        if (parent != null) {
            parent.children.add(this);
        }
    }
    
    public T getValue() {
        return this.currentValue;
    }
    
    public void setValue(final T value) {
        try {
            this.field.set(this.host, this.currentValue = value);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public Option<Boolean> getParent() {
        return this.parent;
    }
    
    public List<Option<?>> getChildren() {
        return this.children;
    }
    
    public float getMin() {
        return this.min;
    }
    
    public float getMax() {
        return this.max;
    }
    
    public static List<Option<?>> getContainersForObject(final Object obj) {
        final List<Option<?>> containers = new ArrayList<Option<?>>();
        for (final Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent((Class<? extends Annotation>)Value.class)) {
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new IllegalStateException("Fields annotated with IncludeOp must not be final!");
                }
                final AtomicReference<Option<Boolean>> parent = new AtomicReference<Option<Boolean>>();
                final Parent annotation = field.getAnnotation(Parent.class);
                if (annotation != null) {
                    for (final Option container : containers) {
                        if (container.getName().equals(annotation.value())) {
                            parent.set(container);
                            break;
                        }
                    }
                }
                try {
                    containers.add(new Option<Object>(field, field.get(obj), obj, parent.get()));
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return containers;
    }
    
    public static Optional<Option<?>> getByTargetAndId(final Object target, final String id) {
        return getContainersForObject(target).stream().filter(s -> s.name.equalsIgnoreCase(id)).findAny();
    }
    
    public String getDebugInfo() {
        return "ID: " + this.getName() + ", Value: " + this.getValue();
    }
    
    public Object getHost() {
        return this.host;
    }
}
