package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.entity.*;

@Module.Register(label = "FastSwim", cat = Module.Category.MOVEMENT)
public class FastSwim extends Module
{
    public void onTick() {
        if (FastSwim.mc.player.isInLava() || (FastSwim.mc.player.isInWater() && EntityUtil.INSTANCE.isMoving((EntityLivingBase)FastSwim.mc.player))) {
            FastSwim.mc.player.setSprinting(true);
            if (FastSwim.mc.gameSettings.keyBindJump.isKeyDown()) {
                FastSwim.mc.player.motionY = 0.098;
            }
        }
    }
}
