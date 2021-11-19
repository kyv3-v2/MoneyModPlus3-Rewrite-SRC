package wtf.moneymod.client.mixin.mixins;

import wtf.moneymod.client.mixin.accessors.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.api.events.*;
import wtf.moneymod.client.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.impl.module.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EntityRenderer.class })
public class MixinEntityRenderer implements IEntityRenderer
{
    @Shadow
    private void setupCameraTransform(final float partialTicks, final int pass) {
    }
    
    @Inject(method = { "drawNameplate" }, at = { @At("HEAD") }, cancellable = true)
    private static void drawNameplate(final FontRenderer fontRendererIn, final String str, final float x, final float y, final float z, final int verticalShift, final float viewerYaw, final float viewerPitch, final boolean isThirdPersonFrontal, final boolean isSneaking, final CallbackInfo callbackInfo) {
        final RenderNameTagEvent event = new RenderNameTagEvent();
        Main.EVENT_BUS.dispatch((Event)event);
        if (Main.getMain().getModuleManager().get((Class)NameTags.class).isToggled() || event.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    public void setupCamera(final float partialTicks, final int pass) {
        this.setupCameraTransform(partialTicks, pass);
    }
}
