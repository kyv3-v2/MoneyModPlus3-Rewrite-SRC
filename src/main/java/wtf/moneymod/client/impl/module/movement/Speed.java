package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.eventhandler.listener.*;
import wtf.moneymod.client.api.events.*;
import net.minecraft.network.play.server.*;
import wtf.moneymod.eventhandler.event.*;
import net.minecraft.network.play.client.*;
import wtf.moneymod.client.mixin.mixins.ducks.*;
import wtf.moneymod.client.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.init.*;
import java.math.*;

@Module.Register(label = "Speed", cat = Module.Category.MOVEMENT)
public class Speed extends Module
{
    @Value("Mode")
    public Mode mode;
    @Value("Vanlla Speed")
    @Bounds(min = 1.0f, max = 10.0f)
    public float speed;
    @Value("Timer")
    public boolean timer;
    @Value("AutoSprint")
    public boolean autoSprint;
    @Value("Water")
    public boolean water;
    @Value("OnGround Strict")
    public boolean onGroundStrict;
    private boolean flip;
    private int rhh;
    private int stage;
    private double moveSpeed;
    private double distance;
    @Handler
    public Listener<PacketEvent.Receive> receiveListener;
    @Handler
    public Listener<PacketEvent.Send> sendListener;
    @Handler
    public Listener<UpdateWalkingPlayerEvent> eventListener;
    @Handler
    public Listener<MoveEvent> moveListener;
    
    public Speed() {
        this.mode = Mode.STRAFE;
        this.speed = 4.0f;
        this.timer = true;
        this.autoSprint = true;
        this.water = true;
        this.onGroundStrict = true;
        this.flip = false;
        this.rhh = 0;
        this.stage = 0;
        this.moveSpeed = 0.0;
        this.distance = 0.0;
        this.receiveListener = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if (e.getPacket() instanceof SPacketPlayerPosLook && (this.mode == Mode.STRAFE || this.mode == Mode.ONGROUND)) {
                this.rhh = 6;
                if (this.mode == Mode.ONGROUND) {
                    this.stage = 2;
                }
                else {
                    this.stage = 1;
                    this.flip = false;
                }
                this.distance = 0.0;
                this.moveSpeed = this.getBaseMoveSpeed();
            }
            return;
        });
        CPacketPlayer packet;
        this.sendListener = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (e.getPacket() instanceof CPacketPlayer) {
                packet = (CPacketPlayer)e.getPacket();
                if (this.mode == Mode.ONGROUND && this.stage == 3) {
                    ((AccessorCPacketPlayer)packet).setY(packet.getY(0.0) + 0.4);
                }
            }
            return;
        });
        double d3;
        double d4;
        this.eventListener = new Listener<UpdateWalkingPlayerEvent>((Class<? extends Event>)UpdateWalkingPlayerEvent.class, e -> {
            if (e.getStage() == 0) {
                d3 = Speed.mc.player.posX - Speed.mc.player.prevPosX;
                d4 = Speed.mc.player.posZ - Speed.mc.player.prevPosZ;
                this.distance = Math.sqrt(d3 * d3 + d4 * d4);
            }
            return;
        });
        String mode;
        EntityPlayerSP player;
        double var;
        float val;
        float val2;
        float[] dir;
        double var2;
        double speedval;
        this.moveListener = new Listener<MoveEvent>((Class<? extends Event>)MoveEvent.class, event -> {
            if (!this.nullCheck()) {
                if (!Speed.mc.player.isElytraFlying() && Speed.mc.player.fallDistance < 4.0f) {
                    if (this.water || (!Speed.mc.player.isInWater() && !Speed.mc.player.isInLava())) {
                        if (this.rhh > 0) {
                            --this.rhh;
                        }
                        if (this.autoSprint) {
                            Speed.mc.player.setSprinting(true);
                        }
                        mode = this.mode.name().toLowerCase();
                        if (mode.equals("strafe")) {
                            if (this.timer) {
                                Main.TICK_TIMER = 1.08f;
                            }
                            if (this.round(Speed.mc.player.posY - (int)Speed.mc.player.posY, 3) == this.round(0.138, 3)) {
                                player = Speed.mc.player;
                                --player.motionY;
                                event.motionY -= 0.09316090325960147;
                            }
                            if (this.stage == 2 && this.isMoving()) {
                                if (Speed.mc.player.collidedVertically) {
                                    event.motionY = 0.4;
                                    Speed.mc.player.motionY = 0.3995;
                                    this.flip = !this.flip;
                                    Main.TICK_TIMER = 1.0f;
                                    if (this.flip) {
                                        this.moveSpeed *= 1.5499999523162842;
                                    }
                                    else {
                                        this.moveSpeed *= 1.3949999809265137;
                                    }
                                }
                            }
                            else if (this.stage == 3 && this.isMoving()) {
                                var = 0.66 * (this.distance - this.getBaseMoveSpeed());
                                this.moveSpeed = this.distance - var;
                                if (this.timer) {
                                    if (this.flip) {
                                        Main.TICK_TIMER = 1.125f;
                                    }
                                    else {
                                        Main.TICK_TIMER = 1.0088f;
                                    }
                                }
                            }
                            else {
                                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically) {
                                    this.stage = 1;
                                }
                                this.moveSpeed = this.distance - this.distance / 159.0;
                            }
                            val = 1.0f;
                            val2 = (float)(val * this.getBaseMoveSpeed());
                            this.moveSpeed = Math.max(this.moveSpeed, val2);
                            dir = this.rhc(this.moveSpeed);
                            event.motionX = dir[0];
                            event.motionZ = dir[1];
                            ++this.stage;
                        }
                        else if (mode.equals("onground")) {
                            if (Speed.mc.player.collidedHorizontally || !this.checkMove()) {
                                Main.TICK_TIMER = 1.0f;
                            }
                            else {
                                if (!this.onGroundStrict) {
                                    if (!Speed.mc.player.onGround) {
                                        Main.TICK_TIMER = 1.0f;
                                    }
                                    else if (this.stage == 2) {
                                        Main.TICK_TIMER = 1.0f;
                                        if (this.rhh > 0) {
                                            this.moveSpeed = this.getBaseMoveSpeed();
                                        }
                                        this.moveSpeed *= 2.149;
                                        this.stage = 3;
                                    }
                                    else if (this.stage == 3) {
                                        if (this.timer) {
                                            Main.TICK_TIMER = Math.max(1.0f + new Random().nextFloat(), 1.2f);
                                        }
                                        else {
                                            Main.TICK_TIMER = 1.0f;
                                        }
                                        this.stage = 2;
                                        var2 = 0.66 * (this.distance - this.getBaseMoveSpeed());
                                        this.moveSpeed = this.distance - var2;
                                    }
                                }
                                this.rhQ_rhP(event, this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed()));
                            }
                        }
                        else if (mode.equals("groundstrafe")) {
                            if (!Speed.mc.player.collidedHorizontally && !Speed.mc.player.movementInput.sneak) {
                                if (!Speed.mc.player.isHandActive() || !(Speed.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood)) {
                                    if (!(!this.checkMove())) {
                                        if (Speed.mc.player.onGround) {
                                            if (Speed.mc.player.ticksExisted % 2 == 0) {
                                                Main.TICK_TIMER = 1.0f;
                                                this.stage = 2;
                                            }
                                            else {
                                                if (this.timer) {
                                                    Main.TICK_TIMER = 1.2f;
                                                }
                                                else {
                                                    Main.TICK_TIMER = 1.0f;
                                                }
                                                this.stage = 3;
                                            }
                                            this.moveSpeed = this.getBaseMoveSpeed();
                                        }
                                        else {
                                            Main.TICK_TIMER = 1.0f;
                                            this.stage = 0;
                                        }
                                        this.rhQ_rhP(event, this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed()));
                                    }
                                }
                            }
                        }
                        else if (mode.equals("vanilla")) {
                            speedval = this.speed / 5.0;
                            this.rhQ_rhP(event, speedval);
                        }
                    }
                }
            }
        });
    }
    
    public void onToggle() {
        try {
            this.stage = 2;
            this.distance = 0.0;
            this.moveSpeed = this.getBaseMoveSpeed();
            Main.TICK_TIMER = 1.0f;
            if (this.autoSprint && Speed.mc.player != null) {
                Speed.mc.player.setSprinting(false);
            }
        }
        catch (Exception ex) {}
    }
    
    public double getBaseMoveSpeed() {
        double d = 0.2873;
        if (Speed.mc.player != null && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int n = Speed.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            d *= 1.0 + 0.2 * (n + 1);
        }
        return d;
    }
    
    public void rhQ_rhP(final MoveEvent event, final double speed) {
        float moveForward = Speed.mc.player.movementInput.moveForward;
        float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
        float rotationYaw = Speed.mc.player.rotationYaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.motionX = 0.0;
            event.motionZ = 0.0;
            return;
        }
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        final double newX = moveForward * speed * motionX + moveStrafe * speed * motionZ;
        final double newZ = moveForward * speed * motionZ - moveStrafe * speed * motionX;
        event.motionX = newX;
        event.motionZ = newZ;
    }
    
    public float[] rhf(final float yaw, final double niggers) {
        float moveForward = Speed.mc.player.movementInput.moveForward;
        float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
        float rotationYaw = yaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            final float[] ret = { 0.0f, 0.0f };
            return ret;
        }
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        final double newX = moveForward * niggers * motionX + moveStrafe * niggers * motionZ;
        final double newZ = moveForward * niggers * motionZ - moveStrafe * niggers * motionX;
        final float[] ret2 = { (float)newX, (float)newZ };
        return ret2;
    }
    
    public float[] rhc(final double niggers) {
        final float yaw = Speed.mc.player.prevRotationYaw + (Speed.mc.player.rotationYaw - Speed.mc.player.prevRotationYaw) * Speed.mc.getRenderPartialTicks();
        return this.rhf(yaw, niggers);
    }
    
    public boolean isMoving() {
        return Speed.mc.player.movementInput.moveForward != 0.0f || Speed.mc.player.movementInput.moveStrafe != 0.0f;
    }
    
    public double round(final double value, final int places) {
        final BigDecimal b = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return b.doubleValue();
    }
    
    public boolean checkMove() {
        return Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f;
    }
    
    public enum Mode
    {
        STRAFE, 
        ONGROUND, 
        GROUNDSTRAGE, 
        VANILLA;
    }
}
