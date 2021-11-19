package wtf.moneymod.client.mixin.mixins.ducks;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ Minecraft.class })
public interface IMinecraft
{
    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(final int p0);
}
