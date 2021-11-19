package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import java.awt.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraft.entity.player.*;
import wtf.moneymod.client.impl.module.misc.*;
import java.util.concurrent.atomic.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import java.util.stream.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.block.*;
import wtf.moneymod.client.impl.utility.impl.player.*;

@Register(label = "AutoTrap", cat = Category.COMBAT)
public class AutoTrap extends Module
{
    @Value("Mode")
    public Mode mode;
    @Value("BPT")
    @Bounds(min = 1.0f, max = 20.0f)
    public int bpt;
    @Value("Delay")
    @Bounds(min = 1.0f, max = 250.0f)
    public int delay;
    @Value("Range")
    @Bounds(min = 1.0f, max = 8.0f)
    public float range;
    @Value("Disable Range")
    @Bounds(min = 1.0f, max = 12.0f)
    public float disableRange;
    @Value("Retry")
    public boolean retry;
    @Value("Help")
    public boolean help;
    @Value("Disable")
    public boolean disable;
    @Value("Anti Step")
    public boolean antiStep;
    @Value("Render")
    public boolean render;
    final Timer timer;
    boolean didPlace;
    boolean rotating;
    int placed;
    Entity target;
    
    public AutoTrap() {
        this.mode = Mode.FULL;
        this.bpt = 8;
        this.delay = 26;
        this.range = 5.0f;
        this.disableRange = 6.0f;
        this.retry = true;
        this.help = true;
        this.disable = false;
        this.antiStep = true;
        this.render = true;
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onConnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.setToggled(false);
    }
    
    @SubscribeEvent
    public void onRender3D(final RenderWorldLastEvent event) {
        if (this.render && this.target != null) {
            for (final BlockPos bp : (this.mode == Mode.FULL) ? this.getFull(this.target) : this.getSimple(this.target)) {
                Renderer3D.drawBoxESP(bp, Color.WHITE, 1.0f, true, true, 60, 255, 1.0f);
            }
        }
    }
    
    @Override
    protected void onEnable() {
        this.placed = 0;
        this.didPlace = false;
        this.timer.reset();
        this.target = null;
        this.rotating = false;
    }
    
    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        this.target = (Entity)EntityUtil.getTarget(this.range);
        if (this.target != null) {
            AutoGG.target((EntityPlayer)this.target);
        }
        if (this.disableRange >= 1.0f && this.target != null && !this.disable && AutoTrap.mc.player.getDistanceSq(this.target) >= this.disableRange * this.disableRange) {
            this.setToggled(false);
            return;
        }
        if ((!this.timer.passed(this.delay) && this.didPlace) || this.target == null) {
            return;
        }
        if (this.mode == Mode.SIMPLE) {
            if (this.getSimple(this.target).size() == 0) {
                if (this.disable) {
                    this.setToggled(false);
                }
                this.rotating = false;
                return;
            }
            this.placeBlocks(this.getSimple(this.target));
        }
        else {
            if (this.getFull(this.target).size() == 0) {
                if (this.disable) {
                    this.setToggled(false);
                }
                this.rotating = false;
                return;
            }
            this.placeBlocks(this.getFull(this.target));
        }
        this.placed = 0;
        this.timer.reset();
    }
    
    List<BlockPos> getSimple(final Entity target) {
        final AtomicBoolean add = new AtomicBoolean(false);
        final AtomicBoolean atomicBoolean;
        final List<BlockPos> blocks = Stream.of(new BlockPos(target.posX, target.posY + 2.0, target.posZ)).filter(blockPos -> {
            switch (BlockUtil.INSTANCE.isPlaceable(blockPos)) {
                case 0: {
                    if (BlockUtil.INSTANCE.calcSide(blockPos) == null) {
                        atomicBoolean.set(true);
                    }
                    return true;
                }
                case -1: {
                    return this.retry;
                }
                case 1: {
                    return false;
                }
                default: {
                    return false;
                }
            }
        }).collect((Collector<? super BlockPos, ?, List<BlockPos>>)Collectors.toList());
        if (add.get()) {
            if (this.help) {
                blocks.add(new BlockPos((Vec3i)new BlockPos(target.posX + 1.0, target.posY - 1.0, target.posZ)));
            }
            for (int j = 0; j < 3; ++j) {
                blocks.add(new BlockPos(target.posX + 1.0, target.posY + j, target.posZ));
            }
        }
        return blocks;
    }
    
    List<BlockPos> getFull(final Entity target) {
        final List<Vec3d> vec3d = new ArrayList<Vec3d>(Arrays.asList(new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0)));
        if (this.help) {
            vec3d.addAll(0, Arrays.asList(new Vec3d(0.0, -1.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0)));
        }
        if (this.antiStep) {
            vec3d.add(new Vec3d(0.0, 3.0, 0.0));
        }
        return vec3d.stream().map(vec -> new BlockPos(target.getPositionVector()).add(vec.x, vec.y, vec.z)).filter(bp -> {
            switch (BlockUtil.INSTANCE.isPlaceable(bp)) {
                case 0: {
                    return true;
                }
                case -1: {
                    return this.retry;
                }
                case 1: {
                    return false;
                }
                default: {
                    return false;
                }
            }
        }).collect((Collector<? super Object, ?, List<BlockPos>>)Collectors.toList());
    }
    
    void placeBlocks(final List<BlockPos> blockPos) {
        this.rotating = true;
        for (final BlockPos bp : blockPos) {
            if (this.placed >= this.bpt) {
                return;
            }
            final int old = AutoTrap.mc.player.inventory.currentItem;
            if (ItemUtil.switchToHotbarSlot(ItemUtil.findHotbarBlock(BlockObsidian.class), false) == -1) {
                return;
            }
            switch (BlockUtil.INSTANCE.isPlaceable(bp)) {
                case 0: {
                    BlockUtil.INSTANCE.placeBlock(bp);
                    this.didPlace = true;
                    ++this.placed;
                    break;
                }
                case -1: {
                    if (this.retry) {
                        BlockUtil.INSTANCE.placeBlock(bp);
                        ++this.placed;
                        break;
                    }
                    break;
                }
            }
            ItemUtil.switchToHotbarSlot(old, false);
        }
    }
    
    public enum Mode
    {
        SIMPLE, 
        FULL;
    }
}
