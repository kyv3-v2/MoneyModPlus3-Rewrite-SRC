package wtf.moneymod.client.api.management.impl;

import java.util.*;
import wtf.moneymod.client.impl.command.*;
import wtf.moneymod.client.api.management.*;
import org.reflections.scanners.*;
import org.reflections.*;

public class CommandManagement extends ArrayList<Command> implements IManager<CommandManagement>
{
    String prefix;
    
    public CommandManagement() {
        this.prefix = "$";
    }
    
    public CommandManagement register() {
        final ReflectiveOperationException ex;
        ReflectiveOperationException e;
        new Reflections("wtf.moneymod.client.impl.command", new Scanner[0]).getSubTypesOf((Class)Command.class).forEach(c -> {
            try {
                this.add(c.newInstance());
            }
            catch (InstantiationException | IllegalAccessException ex2) {
                e = ex;
                e.printStackTrace();
            }
            return;
        });
        return this;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
}
