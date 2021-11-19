package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import net.minecraft.network.play.server.*;
import wtf.moneymod.eventhandler.event.*;

@Module.Register(label = "Changer", cat = Module.Category.MOVEMENT)
public class Changer extends Module
{
    @Value("Fps")
    public boolean fps;
    @Value("Fps Value")
    @Bounds(min = 5.0f, max = 1000.0f)
    public int fpsValue;
    @Value("Fov")
    public boolean fov;
    @Value("Fov Value")
    @Bounds(min = 5.0f, max = 169.0f)
    public int fovValue;
    @Value("Gamma")
    public boolean gamma;
    @Value("Gamma Value")
    @Bounds(min = 5.0f, max = 250.0f)
    public int gammaValue;
    @Value("Time")
    public boolean time;
    @Value("Time Value")
    @Bounds(min = 5.0f, max = 24.0f)
    public int timeValue;
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive;
    
    public Changer() {
        this.fps = true;
        this.fpsValue = 240;
        this.fov = true;
        this.fovValue = 110;
        this.gamma = true;
        this.gammaValue = 250;
        this.time = true;
        this.timeValue = 24;
        this.packetEventReceive = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if (!this.nullCheck()) {
                if (e.getPacket() instanceof SPacketTimeUpdate && this.time) {
                    e.setCancelled(true);
                }
            }
        });
    }
    
    public void onTick() {
        if (this.fps) {
            Changer.mc.gameSettings.limitFramerate = this.fpsValue;
        }
        if (this.fov) {
            Changer.mc.gameSettings.fovSetting = (float)this.fovValue;
        }
        if (this.gamma) {
            Changer.mc.gameSettings.gammaSetting = (float)this.gammaValue;
        }
        if (this.time) {
            Changer.mc.world.setWorldTime((long)(this.timeValue * 1000));
        }
    }
}
