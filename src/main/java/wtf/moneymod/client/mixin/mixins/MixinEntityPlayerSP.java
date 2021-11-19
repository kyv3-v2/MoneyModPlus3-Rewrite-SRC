package wtf.moneymod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import net.minecraft.entity.*;
import wtf.moneymod.client.*;
import wtf.moneymod.eventhandler.event.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.api.events.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { EntityPlayerSP.class }, priority = 9999)
public class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType type, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(x, y, z);
        Main.EVENT_BUS.dispatch((Event)event);
        super.move(type, event.motionX, event.motionY, event.motionZ);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") })
    public void pre(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        Main.EVENT_BUS.dispatch((Event)event);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") })
    public void post(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        Main.EVENT_BUS.dispatch((Event)event);
    }
}
