package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.*;

@Module.Register(label = "Warp", cat = Module.Category.MOVEMENT)
public class Warp extends Module
{
    @Value("Time")
    @Bounds(min = 1.0f, max = 16.0f)
    public int time;
    @Value("Tick")
    @Bounds(min = 1.0f, max = 8.0f)
    public double tick;
    @Value("Mode")
    public Mode mode;
    @Value("Step")
    public boolean step;
    int delay;
    
    public Warp() {
        this.time = 10;
        this.tick = 4.0;
        this.mode = Mode.TIMER;
        this.step = true;
    }
    
    public void onDisable() {
        Main.TICK_TIMER = 1.0f;
    }
    
    public void onToggle() {
        this.delay = 0;
        Warp.mc.player.stepHeight = 0.6f;
    }
    
    public void onTick() {
        if (this.step) {
            Warp.mc.player.stepHeight = 2.0f;
        }
        if (this.mode == Mode.TIMER) {
            ++this.delay;
            Main.TICK_TIMER = (float)this.tick;
            if (this.delay >= this.time) {
                this.delay = 0;
                this.setToggled(false);
            }
        }
        if (this.mode == Mode.ALWAYS) {
            Main.TICK_TIMER = (float)this.tick;
        }
    }
    
    public enum Mode
    {
        TIMER, 
        ALWAYS;
    }
}
