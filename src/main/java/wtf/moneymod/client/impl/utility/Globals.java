package wtf.moneymod.client.impl.utility;

import net.minecraft.client.*;
import com.google.gson.*;

public interface Globals
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final Gson gson = new Gson();
    
    default boolean nullCheck() {
        return Globals.mc.player == null || Globals.mc.world == null;
    }
}
