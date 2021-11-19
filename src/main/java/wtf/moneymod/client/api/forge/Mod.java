package wtf.moneymod.client.api.forge;

import net.minecraftforge.fml.common.event.*;
import wtf.moneymod.client.*;

@net.minecraftforge.fml.common.Mod(modid = "moneymod", name = "Money Mod", version = "1.0")
public class Mod
{
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Main.getMain().init();
    }
}
