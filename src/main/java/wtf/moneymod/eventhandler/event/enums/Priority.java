package wtf.moneymod.eventhandler.event.enums;

public enum Priority
{
    HIGHEST(2), 
    HIGH(1), 
    MEDIUM(0), 
    LOW(-1), 
    LOWEST(-2);
    
    public int priority;
    
    private Priority(final int priority) {
        this.priority = priority;
    }
}
