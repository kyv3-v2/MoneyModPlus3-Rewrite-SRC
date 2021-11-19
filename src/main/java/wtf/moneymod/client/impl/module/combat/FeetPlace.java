package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import net.minecraft.init.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.block.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;

@Register(label = "FeetPlace", cat = Category.COMBAT)
public class FeetPlace extends Module
{
    @Value("Delay")
    @Bounds(max = 250.0f)
    public int delay;
    @Value("BPS")
    @Bounds(max = 20.0f, min = 1.0f)
    public int bps;
    @Value("Retry")
    public boolean retry;
    @Value("Retries")
    @Bounds(max = 25.0f, min = 1.0f)
    public int retries;
    @Value("Cleaner")
    public boolean cleaner;
    @Value("Help")
    public boolean help;
    @Value("Jump Disable")
    public boolean jumpDisable;
    @Value("Disable")
    public boolean disable;
    @Value("AutoCenter")
    public boolean center;
    private final Timer timer;
    private int placed;
    public boolean didPlace;
    private double y;
    private HashMap<BlockPos, Integer> retriesCount;
    
    public FeetPlace() {
        this.delay = 50;
        this.bps = 8;
        this.retry = true;
        this.retries = 5;
        this.cleaner = true;
        this.help = true;
        this.jumpDisable = true;
        this.disable = false;
        this.center = false;
        this.timer = new Timer();
        this.retriesCount = new HashMap<BlockPos, Integer>();
    }
    
    @Override
    protected void onEnable() {
        this.placed = 0;
        this.y = FeetPlace.mc.player.posY;
        this.didPlace = false;
        this.timer.reset();
        this.retriesCount.clear();
    }
    
    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        this.doFeetPlace();
    }
    
    private void doFeetPlace() {
        if (!this.timer.passed(this.delay) && this.didPlace) {
            return;
        }
        if (FeetPlace.mc.player.posY != this.y && this.jumpDisable) {
            this.setToggled(false);
        }
        final int offset = (FeetPlace.mc.world.getBlockState(new BlockPos(FeetPlace.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST && FeetPlace.mc.player.posY - Math.floor(FeetPlace.mc.player.posY) > 0.5) ? 1 : 0;
        if (BlockUtil.INSTANCE.getUnsafePositions(FeetPlace.mc.player.getPositionVector(), offset).size() == 0) {
            if (this.disable) {
                this.setToggled(false);
            }
            return;
        }
        if (this.help) {
            this.placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(FeetPlace.mc.player.getPositionVector(), offset - 1));
        }
        this.placeBlocks(BlockUtil.INSTANCE.getUnsafePositions(FeetPlace.mc.player.getPositionVector(), offset));
        this.placed = 0;
        this.timer.reset();
    }
    
    private void placeBlocks(final List<BlockPos> blocks) {
        for (final BlockPos bp : blocks) {
            if (this.placed >= this.bps) {
                return;
            }
            final int old = FeetPlace.mc.player.inventory.currentItem;
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
                    if (!this.retry) {
                        break;
                    }
                    this.retriesCount.putIfAbsent(bp, 0);
                    if (this.retriesCount.get(bp) > this.retries) {
                        return;
                    }
                    System.out.println(this.retriesCount.get(bp));
                    if (this.cleaner) {
                        for (final Entity e : FeetPlace.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(bp))) {
                            if (e instanceof EntityEnderCrystal) {
                                FeetPlace.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(e));
                            }
                        }
                    }
                    BlockUtil.INSTANCE.placeBlock(bp);
                    this.retriesCount.replace(bp, this.retriesCount.get(bp) + 1);
                    ++this.placed;
                    break;
                }
            }
            if (FeetPlace.mc.player.inventory.currentItem == old) {
                continue;
            }
            ItemUtil.switchToHotbarSlot(old, false);
        }
    }
}
