package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.entity.player.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.eventhandler.listener.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.mixin.mixins.ducks.*;
import net.minecraft.init.*;
import wtf.moneymod.client.impl.module.misc.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraftforge.client.event.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraftforge.fml.common.eventhandler.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import wtf.moneymod.client.api.management.impl.*;

@Register(label = "AutoCrystal", cat = Category.COMBAT)
public class AutoCrystal extends Module
{
    @Value("Place")
    public boolean place;
    @Value("Break")
    public boolean hit;
    @Value("Logic")
    public Logic logic;
    @Value("Target Range")
    @Bounds(max = 16.0f)
    public int targetRange;
    @Value("Place Range ")
    @Bounds(max = 6.0f)
    public int placeRange;
    @Value("Break Range ")
    @Bounds(max = 6.0f)
    public int breakRange;
    @Value("Wall Range")
    @Bounds(max = 6.0f)
    public float wallRange;
    @Value("Break Delay")
    @Bounds(max = 200.0f)
    public int breakDelay;
    @Value("Place Delay")
    @Bounds(max = 200.0f)
    public int placeDelay;
    @Value("BoostDelay")
    @Bounds(max = 200.0f)
    public int boostdelay;
    @Value("MinDamage")
    @Bounds(max = 36.0f)
    public int mindmg;
    @Value("MaxSelfDamage")
    @Bounds(max = 36.0f)
    public int maxselfdamage;
    @Value("FacePlaceDamage")
    @Bounds(max = 36.0f)
    public int faceplacehp;
    @Value("ArmorScale")
    @Bounds(max = 100.0f)
    public int armorscale;
    @Value("TickExisted")
    @Bounds(max = 20.0f)
    public int tickexisted;
    @Value("Predict")
    public boolean boost;
    @Value("Rotate")
    public boolean rotateons;
    @Value("Second")
    public boolean secondCheck;
    @Value("Swap")
    public Swap swap;
    @Value("Color")
    public JColor color;
    private final Set<BlockPos> placeSet;
    private BlockPos renderPos;
    private BlockPos currentBlock;
    public EntityPlayer currentTarget;
    private boolean offhand;
    private boolean rotating;
    private boolean lowArmor;
    private double currentDamage;
    private int ticks;
    private float yaw;
    private float pitch;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer predictTimer;
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive;
    @Handler
    public Listener<PacketEvent.Send> packetEventSend;
    
    public AutoCrystal() {
        this.place = true;
        this.hit = true;
        this.logic = Logic.BREAKPLACE;
        this.targetRange = 12;
        this.placeRange = 5;
        this.breakRange = 5;
        this.wallRange = 3.5f;
        this.breakDelay = 40;
        this.placeDelay = 20;
        this.boostdelay = 80;
        this.mindmg = 6;
        this.maxselfdamage = 6;
        this.faceplacehp = 8;
        this.armorscale = 12;
        this.tickexisted = 3;
        this.boost = true;
        this.rotateons = true;
        this.secondCheck = true;
        this.swap = Swap.NONE;
        this.color = new JColor(255, 0, 0, 180, true);
        this.placeSet = new HashSet<BlockPos>();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.predictTimer = new Timer();
        SPacketSpawnObject packet2;
        AccessorCPacketUseEntity hitPacket;
        int entityId;
        this.packetEventReceive = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if (e.getPacket() instanceof SPacketSpawnObject && this.boost) {
                packet2 = (SPacketSpawnObject)e.getPacket();
                if (packet2.getType() == 51) {
                    if (this.placeSet.contains(new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ()).down()) && this.predictTimer.passed(this.boostdelay)) {
                        hitPacket = (AccessorCPacketUseEntity)new CPacketUseEntity();
                        entityId = packet2.getEntityID();
                        hitPacket.setEntityId(entityId);
                        hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
                        AutoCrystal.mc.getConnection().sendPacket((Packet)hitPacket);
                        this.predictTimer.reset();
                    }
                }
            }
            return;
        });
        this.packetEventSend = new Listener<PacketEvent.Send>((Class<? extends Event>)PacketEvent.Send.class, e -> {
            if (e.getPacket() instanceof CPacketPlayer && this.rotating && this.rotateons) {
                ((AccessorCPacketPlayer)e.getPacket()).setYaw(this.yaw);
                ((AccessorCPacketPlayer)e.getPacket()).setPitch(this.pitch);
            }
        });
    }
    
    public void onToggle() {
        this.rotating = false;
        this.placeSet.clear();
        this.renderPos = null;
        this.breakTimer.reset();
        this.placeTimer.reset();
        this.predictTimer.reset();
    }
    
    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        if (this.ticks++ > 20) {
            this.ticks = 0;
            this.placeSet.clear();
            this.renderPos = null;
        }
        this.offhand = (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
        this.currentTarget = EntityUtil.getTarget((float)this.targetRange);
        if (this.currentTarget != null) {
            AutoGG.target(this.currentTarget);
            this.lowArmor = ItemUtil.isArmorLow(this.currentTarget, this.armorscale);
            this.doAutoCrystal();
        }
    }
    
    public void doAutoCrystal() {
        if (this.logic == Logic.BREAKPLACE) {
            if (this.hit) {
                this.dbreak();
            }
            if (this.place) {
                this.place();
            }
        }
        else {
            if (this.place) {
                this.place();
            }
            if (this.hit) {
                this.dbreak();
            }
        }
    }
    
    public void doHandActive(final EnumHand hand) {
        if (hand != null) {
            AutoCrystal.mc.player.setActiveHand(hand);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.renderPos != null) {
            Renderer3D.drawBoxESP(this.renderPos, this.color.getColor(), 0.3f, true, true, this.color.getColor().getAlpha(), this.color.getColor().getAlpha(), 1.0f);
        }
    }
    
    private void place() {
        EnumHand hand = null;
        BlockPos placePos = null;
        double maxDamage = 0.5;
        for (final BlockPos pos : BlockUtil.INSTANCE.getSphere((float)this.placeRange, true)) {
            final BlockUtil instance = BlockUtil.INSTANCE;
            final double targetDamage;
            final double selfDamage;
            if (BlockUtil.canPlaceCrystal(pos, this.secondCheck) && ((targetDamage = EntityUtil.INSTANCE.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase)this.currentTarget)) >= this.mindmg || EntityUtil.getHealth((EntityLivingBase)this.currentTarget) <= this.faceplacehp || this.lowArmor) && (selfDamage = EntityUtil.INSTANCE.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase)AutoCrystal.mc.player)) + 2.0 < this.maxselfdamage && selfDamage < targetDamage) {
                if (maxDamage > targetDamage) {
                    continue;
                }
                if (this.currentTarget.isDead) {
                    continue;
                }
                placePos = pos;
                maxDamage = targetDamage;
            }
        }
        if (this.swap == Swap.SILENT) {
            if (ItemUtil.findHotbarBlock(ItemEndCrystal.class) == -1) {
                return;
            }
        }
        else if (this.swap == Swap.AUTO) {
            final int crystal = ItemUtil.findHotbarBlock(ItemEndCrystal.class);
            if (crystal == -1) {
                return;
            }
            if (!AutoCrystal.mc.player.isHandActive()) {
                ItemUtil.switchToHotbarSlot(crystal, false);
            }
            else {
                placePos = null;
            }
        }
        else if (this.swap == Swap.NONE && !this.offhand && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            return;
        }
        if (maxDamage != 0.5 && this.placeTimer.passed(this.placeDelay)) {
            final int old = AutoCrystal.mc.player.inventory.currentItem;
            if (AutoCrystal.mc.player.isHandActive()) {
                hand = AutoCrystal.mc.player.getActiveHand();
            }
            if (this.swap == Swap.SILENT) {
                ItemUtil.switchToHotbarSlot(ItemUtil.findHotbarBlock(ItemEndCrystal.class), false);
            }
            if (this.rotateons) {
                this.rotate(placePos);
            }
            if (placePos == null || (this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) == null) {
                return;
            }
            final EnumFacing facing = EnumFacing.UP;
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            AutoCrystal.mc.playerController.updateController();
            if (this.swap == Swap.SILENT) {
                ItemUtil.switchToHotbarSlot(old, false);
            }
            this.doHandActive(hand);
            this.placeSet.add(placePos);
            this.renderPos = new BlockPos(placePos.getX(), placePos.getY(), placePos.getZ());
            this.currentBlock = placePos;
            this.currentDamage = maxDamage;
            this.placeTimer.reset();
        }
        else {
            this.rotating = false;
        }
    }
    
    private void dbreak() {
        Entity maxCrystal = null;
        double maxDamage = 0.5;
        for (final Entity crystal : AutoCrystal.mc.world.loadedEntityList) {
            if (!(crystal instanceof EntityEnderCrystal)) {
                continue;
            }
            final float f = AutoCrystal.mc.player.canEntityBeSeen(crystal) ? ((float)this.breakRange) : this.wallRange;
            final double targetDamage;
            final double selfDamage;
            if (f <= AutoCrystal.mc.player.getDistance(crystal) || ((targetDamage = EntityUtil.INSTANCE.calculate(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)this.currentTarget)) < this.mindmg && EntityUtil.getHealth((EntityLivingBase)this.currentTarget) > this.faceplacehp && !this.lowArmor) || (selfDamage = EntityUtil.INSTANCE.calculate(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)AutoCrystal.mc.player)) + 2.0 >= this.maxselfdamage || selfDamage >= targetDamage) {
                continue;
            }
            if (maxDamage > targetDamage) {
                continue;
            }
            maxCrystal = crystal;
            maxDamage = targetDamage;
        }
        if (maxCrystal != null && this.breakTimer.passed(this.breakDelay)) {
            if (maxCrystal.ticksExisted < this.tickexisted) {
                return;
            }
            if (this.rotateons) {
                this.rotate(maxCrystal);
            }
            AutoCrystal.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(maxCrystal));
            this.breakTimer.reset();
        }
        else {
            this.rotating = false;
        }
    }
    
    private void rotate(final BlockPos bp) {
        final float[] angles = RotationManagement.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(bp.getX() + 0.5f), (double)(bp.getY() + 0.5f), (double)(bp.getZ() + 0.5f)));
        this.yaw = angles[0];
        this.pitch = angles[1];
        this.rotating = true;
    }
    
    private void rotate(final Entity e) {
        final float[] angles = RotationManagement.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), e.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()));
        this.yaw = angles[0];
        this.pitch = angles[1];
        this.rotating = true;
    }
    
    public enum Swap
    {
        NONE, 
        AUTO, 
        SILENT;
    }
    
    public enum Logic
    {
        BREAKPLACE, 
        PLACEBREAK;
    }
}
