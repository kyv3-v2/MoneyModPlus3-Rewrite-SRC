package javassist.compiler.ast;

import javassist.compiler.*;

public class StringL extends ASTree
{
    protected String text;
    
    public StringL(final String t) {
        this.text = t;
    }
    
    public String get() {
        return this.text;
    }
    
    public String toString() {
        return "\"" + this.text + "\"";
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atStringL(this);
    }
}
