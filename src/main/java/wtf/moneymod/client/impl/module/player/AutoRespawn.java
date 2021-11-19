package wtf.moneymod.client.impl.module.player;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.client.gui.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import com.mojang.realmsclient.gui.*;

@Module.Register(label = "AutoRespawn", cat = Module.Category.PLAYER)
public class AutoRespawn extends Module
{
    @Value("DeathCoords")
    public boolean deathcoords;
    boolean value;
    
    public AutoRespawn() {
        this.deathcoords = false;
        this.value = false;
    }
    
    public void onTick() {
        if (this.nullCheck()) {
            return;
        }
        if (AutoRespawn.mc.currentScreen instanceof GuiGameOver && !this.value) {
            AutoRespawn.mc.player.respawnPlayer();
            AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
            this.value = true;
        }
        if (this.value) {
            if (this.deathcoords) {
                ChatUtil.INSTANCE.sendMessage(ChatFormatting.GOLD + "[PlayerDeath] " + ChatFormatting.YELLOW + (int)AutoRespawn.mc.player.posX + " " + (int)AutoRespawn.mc.player.posY + " " + (int)AutoRespawn.mc.player.posZ);
            }
            this.value = false;
        }
    }
}
