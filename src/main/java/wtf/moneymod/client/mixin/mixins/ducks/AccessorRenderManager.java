package wtf.moneymod.client.mixin.mixins.ducks;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ RenderManager.class })
public interface AccessorRenderManager
{
    @Accessor
    double getRenderPosX();
    
    @Accessor
    double getRenderPosY();
    
    @Accessor
    double getRenderPosZ();
}
