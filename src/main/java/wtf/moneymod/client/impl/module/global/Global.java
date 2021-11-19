package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;

@Register(label = "Globals", cat = Category.GLOBAL)
public class Global extends Module
{
    @Value("Chat Animation")
    public boolean chatAnim;
    private static Global INSTANCE;
    
    public Global() {
        this.chatAnim = true;
        Global.INSTANCE = this;
    }
    
    public static Global getInstance() {
        return Global.INSTANCE;
    }
}
