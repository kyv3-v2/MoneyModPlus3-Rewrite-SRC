package wtf.moneymod.client.impl.module.misc;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;

@Register(label = "MiddleClick", cat = Category.MISC)
public class MiddleClick extends Module
{
    @Value("Mode")
    public Mode mode;
    
    public MiddleClick() {
        this.mode = Mode.FRIEND;
    }
    
    @Override
    public void onTick() {
    }
    
    public enum Mode
    {
        FRIEND, 
        PEARL, 
        XP;
    }
}
