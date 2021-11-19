package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.impl.command.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import java.util.stream.*;
import wtf.moneymod.client.impl.module.*;
import java.util.*;

public class ModuleCommand extends Command
{
    public ModuleCommand() {
        super("<ModuleName> || <ModuleName> <SettingName/List> || <ModuleName> <SettingName> <NewValue>", (String[])Main.getMain().getModuleManager().stream().map(m -> m.getLabel().toLowerCase()).toArray(String[]::new));
    }
    
    public void execute(final String[] args) {
        final Module module = Main.getMain().getModuleManager().get(args[0]);
        if (args.length == 1) {
            module.toggle();
            return;
        }
        final Optional<Option<?>> container = (Optional<Option<?>>)Option.getByTargetAndId((Object)module, args[1]);
        if (args.length != 2) {
            if (args.length == 3) {
                if (container.isPresent()) {
                    try {
                        if (container.get().getValue() instanceof Double) {
                            container.get().setValue((Object)Double.parseDouble(args[2]));
                        }
                        else if (container.get().getValue() instanceof Float) {
                            container.get().setValue((Object)Float.parseFloat(args[2]));
                        }
                        else if (container.get().getValue() instanceof Integer) {
                            container.get().setValue((Object)Integer.parseInt(args[2]));
                        }
                        else if (container.get().getValue() instanceof Boolean) {
                            container.get().setValue((Object)Boolean.parseBoolean(args[2]));
                        }
                        else if (container.get().getValue().getClass().isEnum()) {
                            container.get().setValue((Object)SettingUtils.INSTANCE.getProperEnum((Enum)container.get().getValue(), args[2]));
                        }
                        else {
                            System.out.println(container.get().getValue().getClass().getSimpleName());
                        }
                        ChatUtil.INSTANCE.sendMessage(String.format("%s set value to %s", container.get().getName(), container.get().getValue()));
                    }
                    catch (Exception e) {
                        this.sendUsage();
                        e.printStackTrace();
                    }
                }
                else {
                    this.sendUsage();
                    System.out.println("he");
                }
            }
            return;
        }
        if (args[1].equalsIgnoreCase("list")) {
            ChatUtil.INSTANCE.sendMessage((String)Option.getContainersForObject((Object)module).stream().map(s -> String.format("%s[%s]", s.getName(), s.getValue())).collect(Collectors.joining(", ")));
            return;
        }
        if (container.isPresent()) {
            ChatUtil.INSTANCE.sendMessage(container.get().getDebugInfo() + ", Type: " + container.get().getHost().getClass().getSimpleName());
        }
        else {
            this.sendUsage();
        }
    }
}
