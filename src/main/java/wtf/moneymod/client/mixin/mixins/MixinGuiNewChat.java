package wtf.moneymod.client.mixin.mixins;

import wtf.moneymod.client.impl.utility.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import wtf.moneymod.client.impl.utility.impl.math.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wtf.moneymod.client.impl.module.global.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { GuiNewChat.class }, priority = 9999)
public abstract class MixinGuiNewChat implements Globals
{
    @Shadow
    private boolean isScrolled;
    private float percentComplete;
    private int newLines;
    private long prevMillis;
    private boolean configuring;
    private float animationPercent;
    private int lineBeingDrawn;
    
    public MixinGuiNewChat() {
        this.prevMillis = System.currentTimeMillis();
    }
    
    @Shadow
    public abstract float getChatScale();
    
    private void updatePercentage(final long diff) {
        if (this.percentComplete < 1.0f) {
            this.percentComplete += 0.004f * diff;
        }
        this.percentComplete = (float)MathUtil.INSTANCE.clamp((double)this.percentComplete, 0.0, 1.0);
    }
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void modifyChatRendering(final CallbackInfo ci) {
        if (Global.getInstance().chatAnim) {
            if (this.configuring) {
                ci.cancel();
                return;
            }
            final long current = System.currentTimeMillis();
            final long diff = current - this.prevMillis;
            this.prevMillis = current;
            this.updatePercentage(diff);
            float t = this.percentComplete;
            this.animationPercent = (float)MathUtil.INSTANCE.clamp((double)(1.0f - --t * t * t * t), 0.0, 1.0);
        }
    }
    
    @Inject(method = { "drawChat" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0, shift = At.Shift.AFTER) })
    private void translate(final CallbackInfo ci) {
        if (Global.getInstance().chatAnim) {
            float y = 1.0f;
            if (!this.isScrolled) {
                y += (9.0f - 9.0f * this.animationPercent) * this.getChatScale();
            }
            GlStateManager.translate(0.0f, y, 0.0f);
        }
    }
    
    @ModifyArg(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false), index = 0)
    private int getLineBeingDrawn(final int line) {
        return this.lineBeingDrawn = line;
    }
    
    @ModifyArg(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"), index = 3)
    private int modifyTextOpacity(final int original) {
        if (this.lineBeingDrawn <= this.newLines) {
            int opacity = original >> 24 & 0xFF;
            opacity *= (int)this.animationPercent;
            return (original & 0xFFFFFF) | opacity << 24;
        }
        return original;
    }
    
    @Inject(method = { "printChatMessageWithOptionalDeletion" }, at = { @At("HEAD") })
    private void resetPercentage(final CallbackInfo ci) {
        this.percentComplete = 0.0f;
    }
    
    @ModifyVariable(method = { "setChatLine" }, at = @At("STORE"), ordinal = 0)
    private List<ITextComponent> setNewLines(final List<ITextComponent> original) {
        this.newLines = original.size() - 1;
        return original;
    }
    
    @ModifyVariable(method = { "getChatComponent" }, at = @At(value = "STORE", ordinal = 0), ordinal = 3)
    private int modifyX(final int original) {
        return original - 0;
    }
    
    @ModifyVariable(method = { "getChatComponent" }, at = @At(value = "STORE", ordinal = 0), ordinal = 4)
    private int modifyY(final int original) {
        return original + 1;
    }
}
