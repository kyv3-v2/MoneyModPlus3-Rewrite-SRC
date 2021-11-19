package wtf.moneymod.client.impl.module.global;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.impl.ui.click.theme.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import wtf.moneymod.client.*;
import net.minecraft.client.gui.*;

@Register(label = "ClickGui", cat = Category.GLOBAL, key = 210)
public class ClickGui extends Module
{
    @Value("Theme")
    public Themes theme;
    @Value("Descriptions")
    public boolean desc;
    @Value("Color")
    public JColor color;
    @Value("Blur")
    public boolean blur;
    @Value("Bounding")
    public boolean bounding;
    
    public ClickGui() {
        this.theme = Themes.NODUS;
        this.desc = true;
        this.color = new JColor(0, 255, 0, true);
        this.blur = false;
        this.bounding = true;
    }
    
    @Override
    protected void onEnable() {
        if (ClickGui.mc.currentScreen == null) {
            ClickGui.mc.displayGuiScreen((GuiScreen)Main.getMain().getScreen());
        }
        this.disable();
    }
}
