package javassist.compiler;

import java.util.*;
import javassist.compiler.ast.*;

public final class SymbolTable extends HashMap
{
    private SymbolTable parent;
    
    public SymbolTable() {
        this(null);
    }
    
    public SymbolTable(final SymbolTable p) {
        this.parent = p;
    }
    
    public SymbolTable getParent() {
        return this.parent;
    }
    
    public Declarator lookup(final String name) {
        final Declarator found = this.get(name);
        if (found == null && this.parent != null) {
            return this.parent.lookup(name);
        }
        return found;
    }
    
    public void append(final String name, final Declarator value) {
        this.put(name, value);
    }
}
