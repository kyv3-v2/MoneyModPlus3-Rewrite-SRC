package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.entity.*;

@Module.Register(label = "Sprint", cat = Module.Category.MOVEMENT)
public class Sprint extends Module
{
    public void onTick() {
        if (EntityUtil.INSTANCE.isMoving((EntityLivingBase)Sprint.mc.player)) {
            Sprint.mc.player.setSprinting(true);
        }
    }
}
