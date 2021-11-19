package wtf.moneymod.client.mixin.mixins;

import wtf.moneymod.client.mixin.accessors.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;

@Mixin({ Entity.class })
public class MixinEntity implements AccessorEntity
{
    @Shadow
    public boolean isInWeb;
    
    public boolean isInWeb() {
        return this.isInWeb;
    }
    
    public void setInWeb(final boolean state) {
        this.isInWeb = state;
    }
}
