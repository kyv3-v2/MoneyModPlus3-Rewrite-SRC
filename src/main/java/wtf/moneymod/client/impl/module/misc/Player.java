package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.impl.module.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.*;
import com.mojang.authlib.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;

@Register(label = "Player", cat = Category.MISC, exception = true)
public class Player extends Module
{
    private EntityOtherPlayerMP player;
    
    @SubscribeEvent
    public void onEvent(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.setToggled(false);
    }
    
    public void onEnable() {
        if (Player.mc.player == null) {
            this.disable();
            return;
        }
        if (this.player == null) {
            (this.player = new EntityOtherPlayerMP((World)Player.mc.world, new GameProfile(UUID.randomUUID(), "Player"))).copyLocationAndAnglesFrom((Entity)Player.mc.player);
            this.player.inventory.copyInventory(Player.mc.player.inventory);
        }
        Player.mc.world.spawnEntity((Entity)this.player);
    }
    
    public void onDisable() {
        if (this.player != null) {
            Player.mc.world.removeEntity((Entity)this.player);
            this.player = null;
        }
    }
}
