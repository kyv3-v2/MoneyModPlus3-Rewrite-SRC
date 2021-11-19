package wtf.moneymod.client.impl.ui.click.theme;

import wtf.moneymod.client.impl.ui.click.theme.impl.*;

public enum Themes
{
    NODUS((AbstractTheme)NodusTheme.getInstance()), 
    WURST((AbstractTheme)WurstTheme.getInstance());
    
    private AbstractTheme abstractTheme;
    
    private Themes(final AbstractTheme theme) {
        this.abstractTheme = theme;
    }
    
    public AbstractTheme getTheme() {
        return this.abstractTheme;
    }
}
