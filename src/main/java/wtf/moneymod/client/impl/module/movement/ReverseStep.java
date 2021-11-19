package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;

@Module.Register(label = "ReverseStep", cat = Module.Category.MOVEMENT)
public class ReverseStep extends Module
{
    public void onTick() {
        if ((!ReverseStep.mc.player.isInWater() || !ReverseStep.mc.player.isInLava()) && ReverseStep.mc.player.onGround) {
            ReverseStep.mc.player.motionY = -0.8;
        }
    }
}
