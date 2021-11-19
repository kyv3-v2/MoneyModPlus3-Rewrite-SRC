package javassist.compiler;

import java.util.*;

public final class KeywordTable extends HashMap
{
    public int lookup(final String name) {
        final Object found = this.get(name);
        if (found == null) {
            return -1;
        }
        return (int)found;
    }
    
    public void append(final String name, final int t) {
        this.put(name, new Integer(t));
    }
}
