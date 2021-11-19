package wtf.moneymod.client.api.management.impl;

import java.util.*;
import wtf.moneymod.client.api.management.*;

public final class FriendManagement extends ArrayList<String> implements IManager<FriendManagement>
{
    private static final FriendManagement INSTANCE;
    
    public FriendManagement register() {
        return this;
    }
    
    public static FriendManagement getInstance() {
        return FriendManagement.INSTANCE;
    }
    
    public boolean is(final String name) {
        return this.stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }
    
    static {
        INSTANCE = new FriendManagement();
    }
}
