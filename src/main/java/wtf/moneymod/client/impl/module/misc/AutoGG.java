package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraft.entity.*;

@Register(label = "AutoGG", cat = Category.MOVEMENT)
public class AutoGG extends Module
{
    @Value("Logic")
    public Mode mode;
    private static EntityPlayer target;
    Random random;
    String message;
    String[] randomMessage;
    
    public AutoGG() {
        this.mode = Mode.DEFAULT;
        this.random = new Random();
        this.message = "Good Game! My Dick is Stuck In Car Exhaust Pipe It Hurts. Thanks To MoneyMod+3";
        this.randomMessage = new String[] { "is a noob hahaha fobus on tope", "Good fight! Konas owns me and all", "I guess konas ca is too fast for you", "you just got nae nae'd by konas", "so ez lmao", "you just got nae nae'd by wurst+1", "you just got nae nae'd by wurst+2", "you just got nae nae'd by wurst+3" };
    }
    
    public static void target(final EntityPlayer name) {
        AutoGG.target = name;
    }
    
    @Override
    public void onTick() {
        if (AutoGG.target != null && (AutoGG.target.getHealth() <= 0.0f || AutoGG.target.isDead || AutoGG.mc.player.getDistanceSq((Entity)AutoGG.target) >= 100.0)) {
            switch (this.mode) {
                case RANDOM: {
                    AutoGG.mc.player.sendChatMessage(this.randomMessage[this.random.nextInt(this.randomMessage.length)]);
                    break;
                }
                case DEFAULT: {
                    AutoGG.mc.player.sendChatMessage(this.message);
                    break;
                }
            }
            AutoGG.target = null;
        }
    }
    
    public enum Mode
    {
        DEFAULT, 
        RANDOM;
    }
}
