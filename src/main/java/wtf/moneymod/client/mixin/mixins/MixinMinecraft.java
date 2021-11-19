package wtf.moneymod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.api.management.impl.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Minecraft.class })
public class MixinMinecraft
{
    @Inject(method = { "shutdown()V" }, at = { @At("HEAD") })
    public void shutdown(final CallbackInfo callbackInfo) {
        ConfigManager.getInstance().start();
    }
}
