package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.impl.module.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import java.util.*;
import wtf.moneymod.eventhandler.event.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.init.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import wtf.moneymod.client.impl.utility.impl.render.*;

@Register(label = "ChorusHelper", cat = Category.MISC)
public class ChorusHelper extends Module
{
    boolean checkChorus;
    boolean hackPacket;
    boolean posTp;
    double posX;
    double posY;
    double posZ;
    Queue<CPacketPlayer> packets;
    Queue<CPacketConfirmTeleport> packetss;
    SPacketPlayerPosLook serverPos;
    @Value("Render")
    public boolean render;
    @Value("Color")
    public JColor color;
    @Handler
    public Listener<PacketEvent.Send> packeEventSend;
    @Handler
    public Listener<PacketEvent.Receive> packeEventReceive;
    
    public ChorusHelper() {
        this.checkChorus = false;
        this.hackPacket = false;
        this.posTp = false;
        this.packets = new LinkedList<CPacketPlayer>();
        this.packetss = new LinkedList<CPacketConfirmTeleport>();
        this.render = true;
        this.color = new JColor(0, 255, 0, true);
        this.packeEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (e.getPacket() instanceof CPacketConfirmTeleport && this.checkChorus) {
                this.packetss.add((CPacketConfirmTeleport)e.getPacket());
                e.cancel();
            }
            if (e.getPacket() instanceof CPacketPlayer && this.checkChorus) {
                this.packets.add((CPacketPlayer)e.getPacket());
                e.cancel();
            }
            return;
        });
        this.packeEventReceive = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if (e.getPacket() instanceof SPacketPlayerPosLook) {
                this.serverPos = (SPacketPlayerPosLook)e.getPacket();
            }
        });
    }
    
    public void onToggle() {
        this.checkChorus = false;
        this.hackPacket = false;
        this.posTp = false;
        this.serverPos = null;
    }
    
    @Override
    public void onTick() {
        if (this.checkChorus && !ChorusHelper.mc.player.getPosition().equals((Object)new BlockPos(this.posX, this.posY, this.posZ)) && !this.posTp && ChorusHelper.mc.player.getDistance(this.posX, this.posY, this.posZ) > 1.0) {
            ChorusHelper.mc.player.setPosition(this.posX, this.posY, this.posZ);
            this.posTp = true;
        }
        if (this.checkChorus && ChorusHelper.mc.player.isSneaking()) {
            this.doTeleport();
        }
    }
    
    public void doTeleport() {
        this.checkChorus = false;
        this.hackPacket = true;
        this.serverPos = null;
        while (!this.packets.isEmpty()) {
            ChorusHelper.mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
        while (!this.packetss.isEmpty()) {
            ChorusHelper.mc.player.connection.sendPacket((Packet)this.packetss.poll());
        }
        this.hackPacket = false;
        this.checkChorus = false;
    }
    
    @SubscribeEvent
    public void finishEating(final LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() == ChorusHelper.mc.player && event.getResultStack().getItem().equals(Items.CHORUS_FRUIT)) {
            this.posX = ChorusHelper.mc.player.posX;
            this.posY = ChorusHelper.mc.player.posY;
            this.posZ = ChorusHelper.mc.player.posZ;
            this.posTp = false;
            this.checkChorus = true;
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.serverPos != null && this.checkChorus) {
            Renderer3D.drawBoxESP(new BlockPos(this.serverPos.getX(), this.serverPos.getY(), this.serverPos.getZ()), this.color.getColor(), 0.2f, true, true, this.color.getColor().getAlpha(), this.color.getColor().getAlpha(), 2.0f);
        }
    }
}
