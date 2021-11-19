package javassist.scopedpool;

import javassist.*;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory
{
    public ScopedClassPool create(final ClassLoader cl, final ClassPool src, final ScopedClassPoolRepository repository) {
        return new ScopedClassPool(cl, src, repository, false);
    }
    
    public ScopedClassPool create(final ClassPool src, final ScopedClassPoolRepository repository) {
        return new ScopedClassPool((ClassLoader)null, src, repository, true);
    }
}
