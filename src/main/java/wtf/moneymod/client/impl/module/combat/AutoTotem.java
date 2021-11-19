package wtf.moneymod.client.impl.module.combat;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import wtf.moneymod.client.impl.utility.impl.player.*;

@Register(label = "AutoTotem", cat = Category.COMBAT)
public class AutoTotem extends Module
{
    @Value("Health")
    @Bounds(max = 36.0f)
    public int health;
    @Value("Item")
    public Mode mode;
    @Value("Right Click Gapple")
    public boolean rightClickGapple;
    
    public AutoTotem() {
        this.health = 16;
        this.mode = Mode.CRYSTAL;
        this.rightClickGapple = true;
    }
    
    @Override
    public void onTick() {
        if (this.nullCheck() || AutoTotem.mc.currentScreen instanceof GuiInventory) {
            return;
        }
        final float hp = AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount();
        if (hp > this.health && AutoTotem.mc.player.fallDistance != 5.0f) {
            if (this.rightClickGapple && AutoTotem.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && AutoTotem.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.GOLDEN_APPLE));
            }
            else {
                switch (this.mode) {
                    case TOTEM: {
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
                        break;
                    }
                    case GAPPLE: {
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.GOLDEN_APPLE));
                        break;
                    }
                    case CRYSTAL: {
                        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.END_CRYSTAL));
                        break;
                    }
                }
            }
            return;
        }
        ItemUtil.swapItemsOffhand(ItemUtil.getItemSlot(Items.TOTEM_OF_UNDYING));
    }
    
    public enum Mode
    {
        TOTEM, 
        GAPPLE, 
        CRYSTAL;
    }
}
