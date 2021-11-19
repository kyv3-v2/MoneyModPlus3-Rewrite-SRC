package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import wtf.moneymod.eventhandler.event.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Register(label = "Scout", cat = Category.COMBAT)
public class Scout extends Module
{
    @Value("Time")
    @Bounds(min = 1.0f, max = 8.0f)
    public int time;
    @Value("Spoof")
    @Bounds(min = 1.0f, max = 12.0f)
    public int spoof;
    @Value("Render")
    public boolean render;
    @Value("AutoFire")
    public boolean autoFire;
    private boolean hs;
    int ticks;
    private long lastHsTime;
    long percent;
    @Handler
    public Listener<PacketEvent.Send> packeEventSend;
    
    public Scout() {
        this.time = 2;
        this.spoof = 6;
        this.render = false;
        this.autoFire = true;
        CPacketPlayerDigging packet;
        ItemStack handStack;
        int index;
        this.packeEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, event -> {
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                packet = (CPacketPlayerDigging)event.getPacket();
                if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                    handStack = Scout.mc.player.getHeldItem(EnumHand.MAIN_HAND);
                    if (!handStack.isEmpty() && handStack.getItem() != null && handStack.getItem() instanceof ItemBow && System.currentTimeMillis() - this.lastHsTime >= this.time * 1000) {
                        this.hs = true;
                        this.lastHsTime = System.currentTimeMillis();
                        Scout.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Scout.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                        for (index = 0; index < this.spoof * 10; ++index) {
                            Scout.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Scout.mc.player.posX, Scout.mc.player.posY + 1.0E-5, Scout.mc.player.posZ, false));
                            Scout.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Scout.mc.player.posX, Scout.mc.player.posY - 1.0E-5, Scout.mc.player.posZ, true));
                        }
                        this.hs = false;
                    }
                }
            }
        });
    }
    
    public void onToggle() {
        this.hs = false;
        this.ticks = 0;
        this.lastHsTime = System.currentTimeMillis();
    }
    
    @Override
    public void onTick() {
        if (this.autoFire && Scout.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && Scout.mc.player.isHandActive() && Scout.mc.player.getItemInUseMaxCount() >= 4 && this.percent >= 100L) {
            ++this.ticks;
            if (this.ticks >= 14) {
                Scout.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Scout.mc.player.getHorizontalFacing()));
                Scout.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                Scout.mc.player.stopActiveHand();
                this.ticks = 0;
            }
        }
        this.percent = Math.min((System.currentTimeMillis() - this.lastHsTime) / (this.time * 1000L) * 100L, 100L);
    }
    
    @SubscribeEvent
    public void onRender2D(final RenderGameOverlayEvent.Text event) {
        GlStateManager.pushMatrix();
        final ScaledResolution sr = new ScaledResolution(Scout.mc);
        if (this.render) {
            Scout.mc.fontRenderer.drawStringWithShadow(String.format("%s/100", this.percent) + "%", (float)(int)(sr.getScaledWidth() / 2.0f - Scout.mc.fontRenderer.getStringWidth(String.format("%s/100", this.percent) + "%") / 2.0f), (float)(int)(sr.getScaledHeight() / 2.0f + 10.0f), new Color(170, 170, 170).getRGB());
            Renderer2D.drawRect(sr.getScaledWidth() / 2.0f - 21.0f, sr.getScaledHeight() / 2.0f + 20.0f, sr.getScaledWidth() / 2.0f + 23.0f, sr.getScaledHeight() / 2.0f + 25.0f, new Color(0, 0, 0, 140).getRGB());
            Renderer2D.drawRect(sr.getScaledWidth() / 2.0f - 20.0f, sr.getScaledHeight() / 2.0f + 21.0f, sr.getScaledWidth() / 2.0f - 20.0f + this.percent * 0.42f, sr.getScaledHeight() / 2.0f + 24.0f, (this.percent == 100L) ? Color.green.getRGB() : Color.red.getRGB());
        }
        GlStateManager.popMatrix();
    }
}
