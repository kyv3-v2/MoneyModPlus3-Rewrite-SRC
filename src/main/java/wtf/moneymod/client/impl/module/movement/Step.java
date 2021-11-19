package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;

@Module.Register(label = "Step", cat = Module.Category.MOVEMENT)
public class Step extends Module
{
    @Value("Height")
    @Bounds(max = 4.0f)
    public double height;
    
    public Step() {
        this.height = 2.0;
    }
    
    public void onTick() {
        if (!Step.mc.player.isInLava() && !Step.mc.player.isInWater() && Step.mc.player.onGround) {
            Step.mc.player.stepHeight = (float)this.height;
        }
    }
    
    public void onDisable() {
        Step.mc.player.stepHeight = 0.6f;
    }
}
