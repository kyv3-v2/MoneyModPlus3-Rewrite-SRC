package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.management.*;
import org.reflections.scanners.*;
import org.reflections.*;
import wtf.moneymod.client.api.setting.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ModuleManagement extends ArrayList<Module> implements IManager<ModuleManagement>
{
    public ModuleManagement register() {
        Module module;
        final ReflectiveOperationException ex;
        ReflectiveOperationException e;
        new Reflections("wtf.moneymod.client.impl.module", new Scanner[0]).getSubTypesOf((Class)Module.class).forEach(c -> {
            try {
                module = c.newInstance();
                this.add(module);
                Option.getContainersForObject(module);
            }
            catch (InstantiationException | IllegalAccessException ex2) {
                e = ex;
                e.printStackTrace();
            }
            return;
        });
        this.sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getLabel));
        return this;
    }
    
    public ArrayList<Module> get(final Predicate<Module> predicate) {
        return this.stream().filter(predicate).collect((Collector<? super Module, ?, ArrayList<Module>>)Collectors.toCollection((Supplier<R>)ArrayList::new));
    }
    
    public ArrayList<Module> get(final Module.Category category) {
        return this.get(m -> m.getCategory().equals(category));
    }
    
    public Module getFirst(final Predicate<Module> predicate) {
        return this.stream().filter(predicate).findFirst().orElse(null);
    }
    
    public Module get(final String name) {
        return this.getFirst(m -> m.getLabel().equalsIgnoreCase(name));
    }
    
    public Module get(final Class<? extends Module> clazz) {
        return this.getFirst(m -> m.getClass().equals(clazz));
    }
}
