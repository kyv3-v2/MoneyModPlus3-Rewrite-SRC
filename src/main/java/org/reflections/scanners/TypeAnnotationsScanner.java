package org.reflections.scanners;

import java.lang.annotation.*;
import java.util.*;

public class TypeAnnotationsScanner extends AbstractScanner
{
    public void scan(final Object cls) {
        final String className = this.getMetadataAdapter().getClassName(cls);
        for (final String annotationType : this.getMetadataAdapter().getClassAnnotationNames(cls)) {
            if (this.acceptResult(annotationType) || annotationType.equals(Inherited.class.getName())) {
                this.getStore().put((Object)annotationType, (Object)className);
            }
        }
    }
}
