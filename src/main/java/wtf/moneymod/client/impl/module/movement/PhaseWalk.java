package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.eventhandler.listener.*;
import wtf.moneymod.client.api.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.network.play.client.*;

@Module.Register(label = "PhaseWalk", cat = Module.Category.MOVEMENT)
public class PhaseWalk extends Module
{
    @Value("Attempts")
    @Bounds(min = 1.0f, max = 10.0f)
    public int attempts;
    @Value("Speed")
    @Bounds(min = 1.0f, max = 10.0f)
    public int speed;
    Timer timer;
    boolean cancel;
    int teleportID;
    @Handler
    public Listener<PacketEvent.Send> packeEventSend;
    @Handler
    public Listener<MoveEvent> onMove;
    
    public PhaseWalk() {
        this.attempts = 10;
        this.speed = 3;
        this.timer = new Timer();
        this.cancel = false;
        this.teleportID = 0;
        this.packeEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (this.nullCheck()) {
                return;
            }
            else {
                if (e.getPacket() instanceof SPacketPlayerPosLook) {
                    this.teleportID = ((SPacketPlayerPosLook)e.getPacket()).getTeleportId();
                    PhaseWalk.mc.getConnection().sendPacket((Packet)new CPacketConfirmTeleport(this.teleportID + 1));
                }
                return;
            }
        });
        this.onMove = new Listener<MoveEvent>((Class<? extends Event>)MoveEvent.class, e -> {
            if (!this.nullCheck()) {
                if (PhaseWalk.mc.player.collidedHorizontally) {
                    e.motionX = 0.0;
                    e.motionY = 0.0;
                    e.motionZ = 0.0;
                }
            }
        });
    }
    
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        PhaseWalk.mc.player.motionX = 0.0;
        PhaseWalk.mc.player.motionY = 0.0;
        PhaseWalk.mc.player.motionZ = 0.0;
        if (PhaseWalk.mc.player.collidedHorizontally) {
            if (this.timer.isPassed()) {
                final double[] dArray = EntityUtil.forward(this.speed / 100.0);
                for (int i = 0; i < this.attempts; ++i) {
                    this.sendPackets(PhaseWalk.mc.player.posX + dArray[0], PhaseWalk.mc.player.posY + (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown() ? 5 : (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown() ? -5 : 0)) * this.speed / 100.0, PhaseWalk.mc.player.posZ + dArray[1]);
                }
                this.timer.reset();
            }
        }
        else {
            this.cancel = false;
        }
    }
    
    public void sendPackets(final double d, final double d2, final double d3) {
        this.cancel = false;
        PhaseWalk.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(d, d2, d3, PhaseWalk.mc.player.onGround));
        PhaseWalk.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(0.0, 1337.0, 0.0, PhaseWalk.mc.player.onGround));
        this.cancel = true;
    }
}
