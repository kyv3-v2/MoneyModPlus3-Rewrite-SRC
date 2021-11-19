package wtf.moneymod.client.impl.module.player;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.init.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.*;

@Module.Register(label = "AutoLog", cat = Module.Category.PLAYER)
public class AutoLog extends Module
{
    @Value("Health")
    @Bounds(max = 36.0f)
    public int hpLog;
    
    public AutoLog() {
        this.hpLog = 8;
    }
    
    public void onTick() {
        final float hp = AutoLog.mc.player.getHealth() + AutoLog.mc.player.getAbsorptionAmount();
        if (hp <= this.hpLog && AutoLog.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            AutoLog.mc.getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLog, gg")));
        }
    }
}
