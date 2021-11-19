package wtf.moneymod.client.impl.module.player;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import java.util.*;
import net.minecraft.network.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import java.util.concurrent.*;
import net.minecraft.network.play.client.*;
import wtf.moneymod.eventhandler.event.*;

@Module.Register(label = "Blink", cat = Module.Category.PLAYER)
public class Blink extends Module
{
    @Value("Mode")
    public Mode mode;
    @Value("Ticks")
    @Bounds(max = 32.0f)
    public int tick;
    int ticks;
    Queue<Packet<?>> packets;
    @Handler
    public Listener<PacketEvent.Send> packetEventSend;
    
    public Blink() {
        this.mode = Mode.MANUAL;
        this.tick = 8;
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.packetEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (e.getPacket() instanceof CPacketPlayer) {
                this.packets.add((Packet<?>)e.getPacket());
                e.setCancelled(true);
            }
        });
    }
    
    public void onToggle() {
        this.ticks = 0;
        this.clearPackets();
    }
    
    public void onTick() {
        if (this.mode == Mode.TICKS) {
            ++this.ticks;
            if (this.ticks >= this.tick) {
                this.setToggled(false);
            }
        }
    }
    
    public void clearPackets() {
        while (!this.packets.isEmpty()) {
            Blink.mc.getConnection().sendPacket((Packet)this.packets.poll());
        }
    }
    
    public enum Mode
    {
        MANUAL, 
        TICKS;
    }
}
