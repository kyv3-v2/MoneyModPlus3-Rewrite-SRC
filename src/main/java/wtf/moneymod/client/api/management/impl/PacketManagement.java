package wtf.moneymod.client.api.management.impl;

import java.util.*;
import net.minecraft.network.*;
import wtf.moneymod.client.impl.utility.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class PacketManagement extends ArrayDeque<Packet> implements Globals
{
    private static final PacketManagement INSTANCE;
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (PacketManagement.mc.world == null || PacketManagement.mc.player == null) {
            return;
        }
        for (int j = 0; j < 6; ++j) {
            if (this.peekFirst() != null) {
                PacketManagement.mc.player.connection.sendPacket((Packet)this.pollFirst());
            }
        }
    }
    
    public static PacketManagement getInstance() {
        return PacketManagement.INSTANCE;
    }
    
    static {
        INSTANCE = new PacketManagement();
    }
}
