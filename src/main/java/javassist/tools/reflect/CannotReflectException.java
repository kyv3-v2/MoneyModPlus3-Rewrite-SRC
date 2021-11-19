package javassist.tools.reflect;

import javassist.*;

public class CannotReflectException extends CannotCompileException
{
    public CannotReflectException(final String msg) {
        super(msg);
    }
}
