package wtf.moneymod.client.mixin;

import net.minecraftforge.fml.relauncher.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import java.util.*;

public class Loader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public Loader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.moneymod.json");
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        Loader.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        Loader.isObfuscatedEnvironment = false;
    }
}
