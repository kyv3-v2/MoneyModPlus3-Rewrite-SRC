package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.util.math.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.client.entity.*;

@Register(label = "BurrowBypass", cat = Category.COMBAT)
public class SelfFillBypass extends Module
{
    @Value("Bypass Method")
    public Mode mode;
    @Value("Ticks")
    @Bounds(min = 10.0f, max = 100.0f)
    public int ticks;
    @Value("Toggle Delay")
    @Bounds(min = 10.0f, max = 60.0f)
    public int toggleDelays;
    @Value("One Delay")
    @Bounds(min = 10.0f, max = 60.0f)
    public int oneDelays;
    @Value("Second Delay")
    @Bounds(min = 10.0f, max = 60.0f)
    public int placeDelay;
    BlockPos position;
    int delay;
    int pdelay;
    int stage;
    int jumpdelay;
    int toggledelay;
    boolean jump;
    Timer timer;
    
    public SelfFillBypass() {
        this.mode = Mode.PIGBYPASS;
        this.ticks = 50;
        this.toggleDelays = 20;
        this.oneDelays = 42;
        this.placeDelay = 30;
        this.timer = new Timer();
    }
    
    public void onEnable() {
        this.position = new BlockPos(SelfFillBypass.mc.player.getPositionVector());
    }
    
    public void onToggle() {
        this.pdelay = 0;
        this.stage = 1;
        this.toggledelay = 0;
        this.jumpdelay = 0;
        this.timer.reset();
        this.jump = false;
        Main.TICK_TIMER = 1.0f;
        this.position = null;
        this.delay = 0;
    }
    
    @Override
    public void onTick() {
        if (this.position != null && this.mode == Mode.PIGBYPASS) {
            this.firstmethod();
        }
    }
    
    public void firstmethod() {
        if (this.stage == 1) {
            ++this.delay;
            if (SelfFillBypass.mc.player.onGround) {
                SelfFillBypass.mc.player.jump();
            }
            Main.TICK_TIMER = (float)this.ticks;
            if (this.delay >= this.oneDelays) {
                this.stage = 2;
                this.delay = 0;
                Main.TICK_TIMER = 1.0f;
                this.jump = true;
            }
        }
        if (this.stage == 2) {
            Main.TICK_TIMER = 1.0f;
            if (SelfFillBypass.mc.player.onGround) {
                SelfFillBypass.mc.player.jump();
            }
            BlockUtil.INSTANCE.placeBlock(this.position);
            ++this.pdelay;
            if (this.pdelay >= this.placeDelay) {
                this.stage = 3;
                this.pdelay = 0;
                Main.TICK_TIMER = 1.0f;
            }
        }
        if (this.stage == 3) {
            ++this.toggledelay;
            Main.TICK_TIMER = (float)this.ticks;
            if (SelfFillBypass.mc.player.onGround) {
                SelfFillBypass.mc.player.jump();
            }
            if (this.toggledelay >= this.toggleDelays) {
                final EntityPlayerSP player = SelfFillBypass.mc.player;
                player.motionY -= 0.4;
                Main.TICK_TIMER = 1.0f;
                this.setToggled(false);
            }
        }
    }
    
    public enum Mode
    {
        PIGBYPASS, 
        SECONDBYPASS;
    }
}
