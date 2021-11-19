package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;

public class DamageBlockEvent extends Event
{
    private BlockPos blockPos;
    private EnumFacing faceDirection;
    
    public DamageBlockEvent(final BlockPos blockPos, final EnumFacing faceDirection) {
        this.blockPos = blockPos;
        this.faceDirection = faceDirection;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public EnumFacing getFaceDirection() {
        return this.faceDirection;
    }
}
