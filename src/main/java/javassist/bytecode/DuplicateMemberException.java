package javassist.bytecode;

import javassist.*;

public class DuplicateMemberException extends CannotCompileException
{
    public DuplicateMemberException(final String msg) {
        super(msg);
    }
}
