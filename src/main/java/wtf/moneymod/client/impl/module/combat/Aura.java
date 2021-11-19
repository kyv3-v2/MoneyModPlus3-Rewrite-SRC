package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.entity.player.*;
import wtf.moneymod.client.impl.module.misc.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.util.math.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.function.*;
import wtf.moneymod.client.api.management.impl.*;
import net.minecraft.util.*;
import net.minecraft.item.*;

@Register(label = "Aura", cat = Category.COMBAT)
public class Aura extends Module
{
    @Value("Mode")
    public Mode mode;
    @Value("Range")
    @Bounds(max = 6.0f)
    public double range;
    @Value("Render")
    public boolean render;
    @Value("Color")
    public JColor color;
    float yaw;
    float pitch;
    boolean rotating;
    Entity target;
    
    public Aura() {
        this.mode = Mode.SWITCH;
        this.range = 4.2;
        this.render = false;
        this.color = new JColor(255, 255, 255, 120, false);
        this.yaw = 0.0f;
        this.pitch = 0.0f;
    }
    
    @Override
    protected void onEnable() {
        this.target = null;
    }
    
    @Override
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        this.target = this.findTarget(EntityPlayer.class::isInstance);
        if (this.target != null) {
            AutoGG.target((EntityPlayer)this.target);
        }
        if (this.target != null) {
            switch (this.mode) {
                case SWITCH: {
                    ItemUtil.getHotbarItems().keySet().stream().filter(e -> e.getItem() instanceof ItemSword).max(Comparator.comparing(e -> ((ItemSword)e.getItem()).getAttackDamage() + EnchantmentHelper.getModifierForCreature(e, EnumCreatureAttribute.UNDEFINED))).ifPresent(bestSword -> ItemUtil.switchToHotbarSlot(ItemUtil.getHotbarItems().get(bestSword), false));
                    break;
                }
                case ONLY: {
                    if (!(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                        return;
                    }
                    break;
                }
            }
            this.attack();
        }
        else {
            this.rotating = false;
        }
    }
    
    @SubscribeEvent
    public void Render3D(final RenderWorldLastEvent event) {
        if (this.target != null && this.render) {
            if (this.mode == Mode.ONLY && !(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                return;
            }
            Renderer3D.drawBoxESP(new AxisAlignedBB(this.target.posX - 0.30000001192092896, this.target.posY, this.target.posZ - 0.30000001192092896, this.target.posX + 0.30000001192092896, this.target.posY + 1.899999976158142, this.target.posZ + 0.30000001192092896), this.color.getColor(), 1.0f, true, true, this.color.getColor().getAlpha(), 255);
        }
    }
    
    private Entity findTarget(final Predicate<Entity> predicate) {
        return (Entity)Aura.mc.world.loadedEntityList.stream().filter(e -> !FriendManagement.getInstance().is(e.getName()) && e != Aura.mc.player && Aura.mc.player.getPositionVector().add(0.0, (double)Aura.mc.player.eyeHeight, 0.0).distanceTo(e.getPositionVector().add(0.0, e.height / 2.0, 0.0)) <= this.range).filter(predicate).min(Comparator.comparing(e -> Aura.mc.player.getDistanceSq(e))).orElse(null);
    }
    
    private void attack() {
        if (Aura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
            Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, this.target);
            Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
            Aura.mc.player.resetCooldown();
        }
    }
    
    public enum Mode
    {
        NONE, 
        ONLY, 
        SWITCH;
    }
}
