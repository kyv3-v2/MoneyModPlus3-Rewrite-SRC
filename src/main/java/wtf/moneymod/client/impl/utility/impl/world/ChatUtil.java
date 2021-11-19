package wtf.moneymod.client.impl.utility.impl.world;

import wtf.moneymod.client.impl.utility.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.util.text.*;

public enum ChatUtil implements Globals
{
    INSTANCE;
    
    public String staticName;
    
    private ChatUtil() {
        this.staticName = ChatFormatting.GREEN + "[" + "moneymod" + "]";
    }
    
    public void sendMessage(final String text) {
        this.sendMsgEvent(this.staticName, text, false, 1);
    }
    
    public void sendMessage(final String text, final Boolean silent) {
        this.sendMsgEvent(this.staticName, text, silent, 1);
    }
    
    public void sendMessageId(final String text, final Boolean silent, final int id) {
        this.sendMsgEvent(this.staticName, text, silent, id);
    }
    
    public void sendMsgEvent(final String prefix, final String text, final boolean silent, final int id) {
        if (ChatUtil.mc.player == null) {
            return;
        }
        if (!silent) {
            ChatUtil.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentString(prefix + TextFormatting.GRAY + " " + text));
        }
        else {
            ChatUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new TextComponentString(prefix + TextFormatting.GRAY + " " + text), id);
        }
    }
}
