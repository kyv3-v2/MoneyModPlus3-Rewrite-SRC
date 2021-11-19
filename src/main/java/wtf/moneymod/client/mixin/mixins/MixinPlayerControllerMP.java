package wtf.moneymod.client.mixin.mixins;

import wtf.moneymod.client.mixin.accessors.*;
import wtf.moneymod.client.impl.utility.*;
import net.minecraft.client.multiplayer.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.client.*;
import wtf.moneymod.eventhandler.event.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP implements IPlayerControllerMP, Globals
{
    @Shadow
    private int blockHitDelay;
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void clickBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable<Boolean> cir) {
        final DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        Main.EVENT_BUS.dispatch((Event)event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)false);
            cir.cancel();
        }
    }
    
    public void setBlockHitDelay(final int delay) {
        this.blockHitDelay = delay;
    }
}
