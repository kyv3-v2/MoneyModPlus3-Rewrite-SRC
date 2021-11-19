package wtf.moneymod.client.impl.module.player;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import java.awt.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.eventhandler.listener.*;
import wtf.moneymod.client.api.events.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import wtf.moneymod.client.impl.utility.impl.math.*;
import wtf.moneymod.client.mixin.accessors.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;

@Module.Register(label = "SpeedMine", cat = Module.Category.PLAYER)
public class SpeedMine extends Module
{
    @Value("Renderer")
    public RenderMode mode;
    @Value("Render")
    public boolean render;
    @Value("Silent")
    public boolean silent;
    @Value("Instant Rebreak")
    public boolean instant;
    @Value("Range")
    @Bounds(min = 4.0f, max = 30.0f)
    public int range;
    @Value("Packet Spam")
    @Bounds(min = 1.0f, max = 10.0f)
    public int spam;
    public Color color;
    public Color readyColor;
    private BlockPos currentPos;
    private final Timer timer;
    private long start;
    private int old;
    private int delay;
    public boolean swap;
    public boolean checked;
    @Handler
    public Listener<MoveEvent> moveEventListener;
    @Handler
    public Listener<DamageBlockEvent> damageBlockEvent;
    
    public SpeedMine() {
        this.mode = RenderMode.FADE;
        this.render = true;
        this.silent = true;
        this.instant = true;
        this.range = 16;
        this.spam = 1;
        this.color = new Color(255, 0, 0, 75);
        this.readyColor = new Color(0, 255, 0, 75);
        this.timer = new Timer();
        this.swap = false;
        this.moveEventListener = new Listener<MoveEvent>((Class<? extends Event>)MoveEvent.class, e -> {
            if (this.currentPos != null) {
                if (this.instant) {
                    if (SpeedMine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                        if (!this.checked) {
                            this.checked = true;
                            this.start = System.currentTimeMillis();
                            this.timer.reset();
                        }
                    }
                    else {
                        this.checked = false;
                    }
                }
                if (this.instant && SpeedMine.mc.player.inventory.currentItem == ToolUtil.INSTANCE.bestSlot(this.currentPos) && this.getBlockProgress(this.currentPos, SpeedMine.mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(this.currentPos)), this.start) <= 0.1 && SpeedMine.mc.world.getBlockState(this.currentPos).getBlock() != Blocks.AIR && (!this.swap || this.delay > 2)) {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.currentPos, EnumFacing.DOWN));
                }
                if (!this.swap) {
                    this.old = SpeedMine.mc.player.inventory.currentItem;
                }
                if (SpeedMine.mc.player.getDistanceSq(this.currentPos) >= MathUtil.INSTANCE.square((float)this.range)) {
                    this.currentPos = null;
                }
            }
            try {
                ((IPlayerControllerMP)SpeedMine.mc.playerController).setBlockHitDelay(0);
            }
            catch (Exception ex) {}
            return;
        });
        int j;
        this.damageBlockEvent = new Listener<DamageBlockEvent>((Class<? extends Event>)DamageBlockEvent.class, e -> {
            if (this.swap) {
                e.setCancelled(true);
            }
            else if (!this.nullCheck() && BlockUtil.INSTANCE.canBlockBeBroken(e.getBlockPos())) {
                if (this.currentPos != null) {
                    if (e.getBlockPos().toLong() == this.currentPos.toLong() && !this.swap && this.getBlockProgress(this.currentPos, SpeedMine.mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(this.currentPos)), this.start) <= 0.1 && SpeedMine.mc.world.getBlockState(this.currentPos).getBlock() != Blocks.AIR) {
                        ItemUtil.switchToHotbarSlot(ToolUtil.INSTANCE.bestSlot(this.currentPos), this.silent);
                        this.swap = true;
                        e.cancel();
                        return;
                    }
                    else if (e.getBlockPos().toLong() != this.currentPos.toLong()) {
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentPos, EnumFacing.DOWN));
                    }
                }
                SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                for (j = 0; j < this.spam; ++j) {
                    SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, e.getBlockPos(), e.getFaceDirection()));
                }
                SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getBlockPos(), EnumFacing.DOWN));
                this.currentPos = e.getBlockPos();
                this.start = System.currentTimeMillis();
                this.timer.reset();
                e.setCancelled(true);
            }
        });
    }
    
    protected void onToggle() {
        this.old = -1;
        this.currentPos = null;
        this.delay = 0;
    }
    
    public void onTick() {
        if (this.swap) {
            SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.currentPos, EnumFacing.DOWN));
            if (this.delay >= 2) {
                if (this.old != -1) {
                    ItemUtil.switchToHotbarSlot(this.old, false);
                }
                this.swap = false;
                this.delay = 0;
            }
            ++this.delay;
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.currentPos == null || !this.render || SpeedMine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR || SpeedMine.mc.world.getBlockState(this.currentPos).getBlock() instanceof BlockLiquid) {
            return;
        }
        final AxisAlignedBB bb = SpeedMine.mc.world.getBlockState(this.currentPos).getSelectedBoundingBox((World)SpeedMine.mc.world, this.currentPos);
        final float progress = this.getBlockProgress(this.currentPos, SpeedMine.mc.player.inventory.getStackInSlot(ToolUtil.INSTANCE.bestSlot(this.currentPos)), this.start);
        if (progress <= 0.1) {
            Renderer3D.drawBoxESP(bb, this.readyColor, 1.0f, true, true, this.readyColor.getAlpha(), 255);
        }
        else if (this.mode == RenderMode.FADE) {
            Renderer3D.drawBoxESP(bb, new Color((int)(this.color.getRed() * progress), (int)(this.readyColor.getGreen() * (1.0f - progress)), this.color.getBlue()), 1.0f, true, true, this.color.getAlpha(), 255);
        }
        else {
            Renderer3D.INSTANCE.drawProgressBox(bb, progress, this.color);
        }
    }
    
    float getBlockProgress(final BlockPos blockPos, final ItemStack stack, final long start) {
        return (float)MathUtil.INSTANCE.clamp(1.0 - (System.currentTimeMillis() - start) / (double)ToolUtil.INSTANCE.time(blockPos, stack), 0.0, 1.0);
    }
    
    public enum RenderMode
    {
        FADE, 
        EXPAND;
    }
}
