package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.init.*;
import wtf.moneymod.client.mixin.mixins.ducks.*;
import net.minecraft.item.*;
import wtf.moneymod.client.impl.utility.impl.player.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

@Register(label = "ExpTweaks", cat = Category.MISC)
public class ExpTweaks extends Module
{
    @Value("Silent")
    public boolean silent;
    
    public ExpTweaks() {
        this.silent = false;
    }
    
    @Override
    public void onTick() {
        if (ExpTweaks.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && !this.silent) {
            ((IMinecraft)ExpTweaks.mc).setRightClickDelayTimer(0);
        }
        if (this.silent) {
            final int old = ExpTweaks.mc.player.inventory.currentItem;
            final int xp = ItemUtil.findHotbarBlock(ItemExpBottle.class);
            if (xp == -1) {
                return;
            }
            ItemUtil.switchToHotbarSlot(xp, false);
            ExpTweaks.mc.playerController.processRightClick((EntityPlayer)ExpTweaks.mc.player, (World)ExpTweaks.mc.world, EnumHand.MAIN_HAND);
            ItemUtil.switchToHotbarSlot(old, false);
        }
    }
}
