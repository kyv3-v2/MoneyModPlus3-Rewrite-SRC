package wtf.moneymod.client.impl.command;

import wtf.moneymod.client.impl.utility.impl.world.*;

public abstract class Command implements ICommand
{
    private String label;
    private String syntax;
    private String[] alias;
    
    public Command(final String syntax, final String... alias) {
        this.alias = alias;
        this.syntax = syntax;
        this.label = alias[0];
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String[] getAlias() {
        return this.alias;
    }
    
    public String getSyntax() {
        return this.syntax;
    }
    
    @Override
    public abstract void execute(final String[] p0);
    
    protected void print(final String message) {
        ChatUtil.INSTANCE.sendMessage(message);
    }
    
    protected void sendUsage() {
        ChatUtil.INSTANCE.sendMessage("Invalid Usage! " + this.getSyntax());
    }
}
