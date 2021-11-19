package javassist.compiler.ast;

import javassist.compiler.*;

public class Keyword extends ASTree
{
    protected int tokenId;
    
    public Keyword(final int token) {
        this.tokenId = token;
    }
    
    public int get() {
        return this.tokenId;
    }
    
    public String toString() {
        return "id:" + this.tokenId;
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atKeyword(this);
    }
}
