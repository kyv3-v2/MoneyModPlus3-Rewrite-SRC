package wtf.moneymod.client.impl.command.impl;

import wtf.moneymod.client.impl.command.*;
import java.util.*;
import wtf.moneymod.client.api.management.impl.*;
import com.mojang.realmsclient.gui.*;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("f add/del <nick>", new String[] { "f", "Friend", "friend" });
    }
    
    public void execute(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 2) {
            this.sendUsage();
            return;
        }
        final String lowerCase = str[0].toLowerCase();
        switch (lowerCase) {
            case "add": {
                if (FriendManagement.getInstance().is(str[1].toLowerCase())) {
                    return;
                }
                FriendManagement.getInstance().add((Object)str[1]);
                this.print(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.GREEN + "added");
                break;
            }
            case "del": {
                FriendManagement.getInstance().remove((Object)str[1]);
                this.print(ChatFormatting.GRAY + "friend " + ChatFormatting.WHITE + "> " + str[1].toLowerCase() + " " + ChatFormatting.RED + "deleted");
                break;
            }
        }
    }
}
