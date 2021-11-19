package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.util.*;

@Module.Register(label = "Swing", cat = Module.Category.MOVEMENT)
public class Swing extends Module
{
    @Value("Offhand")
    public boolean offhand;
    
    public Swing() {
        this.offhand = true;
    }
    
    public void onTick() {
        Swing.mc.player.swingingHand = (this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }
}
