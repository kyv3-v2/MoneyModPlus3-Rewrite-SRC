package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.impl.utility.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;

public class RotationManagement implements Globals
{
    private float yaw;
    private float pitch;
    
    public void update() {
        this.yaw = RotationManagement.mc.player.rotationYaw;
        this.pitch = RotationManagement.mc.player.rotationPitch;
    }
    
    public void reset() {
        RotationManagement.mc.player.rotationYaw = this.yaw;
        RotationManagement.mc.player.rotationPitch = this.pitch;
        RotationManagement.mc.player.rotationYawHead = this.yaw;
    }
    
    public void set(final float yaw, final float pitch, final boolean packet) {
        if (packet) {
            RotationManagement.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, RotationManagement.mc.player.onGround));
        }
        else {
            RotationManagement.mc.player.rotationYaw = yaw;
            RotationManagement.mc.player.rotationYawHead = yaw;
            RotationManagement.mc.player.rotationPitch = pitch;
        }
    }
    
    public float[] look(final BlockPos bp, final boolean packet) {
        return this.look(bp, packet, true);
    }
    
    public float[] look(final Entity bp, final boolean packet) {
        return this.look(bp, packet, true);
    }
    
    public float[] look(final BlockPos bp, final boolean packet, final boolean set) {
        final float[] angles = calcAngle(RotationManagement.mc.player.getPositionEyes(RotationManagement.mc.getRenderPartialTicks()), new Vec3d((double)(bp.getX() + 0.5f), (double)(bp.getY() + 0.5f), (double)(bp.getZ() + 0.5f)));
        if (set) {
            this.set(angles[0], angles[1], packet);
        }
        return angles;
    }
    
    public float[] look(final Entity entity, final boolean packet, final boolean set) {
        final float[] angles = calcAngle(RotationManagement.mc.player.getPositionEyes(RotationManagement.mc.getRenderPartialTicks()), entity.getPositionEyes(RotationManagement.mc.getRenderPartialTicks()));
        if (set) {
            this.set(angles[0], angles[1], packet);
        }
        return angles;
    }
    
    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }
}
