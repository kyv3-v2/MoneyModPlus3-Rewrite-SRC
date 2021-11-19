package wtf.moneymod.client.mixin.mixins;

import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Timer.class })
public class MixinTimer
{
    @Shadow
    public float elapsedPartialTicks;
    
    @Inject(method = { "updateTimer" }, at = { @At(value = "FIELD", target = "net/minecraft/util/Timer.elapsedPartialTicks:F", ordinal = 1) })
    public void updateTimer(final CallbackInfo info) {
        this.elapsedPartialTicks *= Main.TICK_TIMER;
    }
}
