package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.mixin.accessors.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import wtf.moneymod.eventhandler.event.*;
import net.minecraft.entity.*;

@Register(label = "Criticals", cat = Category.COMBAT)
public class Criticals extends Module
{
    @Value("Only KA")
    public boolean onlyKillaura;
    @Value("Strict")
    public boolean strict;
    @Handler
    public Listener<PacketEvent.Send> packeEventSend;
    
    public Criticals() {
        this.onlyKillaura = true;
        this.strict = false;
        CPacketUseEntity packet;
        Entity entity;
        double x;
        double y;
        double z;
        this.packeEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (e.getPacket() instanceof CPacketUseEntity) {
                packet = (CPacketUseEntity)e.getPacket();
                if (packet.getAction().equals((Object)CPacketUseEntity.Action.ATTACK) && Criticals.mc.player.onGround && !Criticals.mc.player.isInWater() && !Criticals.mc.player.isInLava()) {
                    if (!this.onlyKillaura || Main.getMain().getModuleManager().get((Class)Aura.class).isToggled()) {
                        if (!((AccessorEntity)Criticals.mc.player).isInWeb()) {
                            entity = packet.getEntityFromWorld((World)Criticals.mc.world);
                            if (entity instanceof EntityLivingBase) {
                                x = Criticals.mc.player.posX;
                                y = Criticals.mc.player.posY;
                                z = Criticals.mc.player.posZ;
                                if (this.strict) {
                                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.07, z, false));
                                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.08, z, false));
                                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, false));
                                }
                                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.05, z, false));
                                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, false));
                                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y + 0.012, z, false));
                                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, false));
                                Criticals.mc.player.onCriticalHit(entity);
                            }
                        }
                    }
                }
            }
        });
    }
}
