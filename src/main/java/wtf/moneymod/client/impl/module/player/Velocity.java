package wtf.moneymod.client.impl.module.player;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import net.minecraft.network.play.server.*;
import net.minecraft.entity.projectile.*;
import wtf.moneymod.eventhandler.event.*;

@Module.Register(label = "Velocity", cat = Module.Category.PLAYER)
public class Velocity extends Module
{
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive;
    
    public Velocity() {
        this.packetEventReceive = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if ((e.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)e.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) || e.getPacket() instanceof SPacketExplosion || e.getPacket() instanceof EntityFishHook) {
                e.setCancelled(true);
            }
        });
    }
}
