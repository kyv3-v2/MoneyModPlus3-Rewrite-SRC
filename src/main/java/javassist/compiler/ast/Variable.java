package javassist.compiler.ast;

import javassist.compiler.*;

public class Variable extends Symbol
{
    protected Declarator declarator;
    
    public Variable(final String sym, final Declarator d) {
        super(sym);
        this.declarator = d;
    }
    
    public Declarator getDeclarator() {
        return this.declarator;
    }
    
    public String toString() {
        return this.identifier + ":" + this.declarator.getType();
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atVariable(this);
    }
}
