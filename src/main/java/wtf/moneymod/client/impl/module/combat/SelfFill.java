package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import wtf.moneymod.client.impl.utility.impl.world.*;

@Register(label = "SelfFill", cat = Category.COMBAT)
public class SelfFill extends Module
{
    @Value("Height")
    @Bounds(min = -8.0f, max = 8.0f)
    public int height;
    private final List<Double> offsets;
    private BlockPos startPos;
    int tick;
    boolean fill;
    
    public SelfFill() {
        this.height = 4;
        this.offsets = Arrays.asList(0.4199999, 0.7531999, 1.0013359, 1.1661092);
        this.tick = 0;
        this.fill = false;
    }
    
    private boolean check() {
        return SelfFill.mc.player.onGround && SelfFill.mc.world.getBlockState(this.startPos).getBlock() == Blocks.AIR && SelfFill.mc.world.getBlockState(this.startPos.add(0, 3, 0)).getBlock() == Blocks.AIR;
    }
    
    public void onEnable() {
        this.fill = true;
    }
    
    @Override
    public void onTick() {
        final int startSlot = SelfFill.mc.player.inventory.currentItem;
        this.startPos = new BlockPos(SelfFill.mc.player.getPositionVector());
        if (ItemUtil.findHotbarBlock(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, (Block)Blocks.CHEST) == -1) {
            this.setToggled(false);
            this.tick = 0;
            return;
        }
        if (!this.check()) {
            return;
        }
        ++this.tick;
        if (this.fill) {
            ItemUtil.switchToHotbarSlot(ItemUtil.findHotbarBlock(Blocks.ENDER_CHEST, Blocks.OBSIDIAN, (Block)Blocks.CHEST), false);
            this.offsets.forEach(offset -> SelfFill.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY + offset, SelfFill.mc.player.posZ, true)));
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfFill.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.INSTANCE.placeBlock(this.startPos);
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfFill.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            SelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(SelfFill.mc.player.posX, SelfFill.mc.player.posY + this.height, SelfFill.mc.player.posZ, false));
            ItemUtil.switchToHotbarSlot(startSlot, false);
            this.fill = false;
        }
        if (this.tick >= 8) {
            this.tick = 0;
            this.setToggled(false);
        }
    }
}
