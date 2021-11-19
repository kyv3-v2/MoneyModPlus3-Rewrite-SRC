package wtf.moneymod.client.mixin.mixins;

import wtf.moneymod.client.impl.utility.*;
import org.spongepowered.asm.mixin.*;
import io.netty.channel.*;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.client.*;
import wtf.moneymod.eventhandler.event.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager implements Globals
{
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    public void receive(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo ci) {
        if (MixinNetworkManager.mc.player == null && MixinNetworkManager.mc.world == null) {
            return;
        }
        final PacketEvent.Receive event = new PacketEvent.Receive((Packet)packet);
        Main.EVENT_BUS.dispatch((Event)event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void send(final Packet<?> packet, final CallbackInfo ci) {
        if (MixinNetworkManager.mc.player == null && MixinNetworkManager.mc.world == null) {
            return;
        }
        final PacketEvent.Send event = new PacketEvent.Send((Packet)packet);
        Main.EVENT_BUS.dispatch((Event)event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
