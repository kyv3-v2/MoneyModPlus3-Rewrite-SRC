package org.spongepowered.tools.obfuscation.validation;

import org.spongepowered.tools.obfuscation.*;
import org.spongepowered.tools.obfuscation.interfaces.*;
import java.util.*;
import org.spongepowered.tools.obfuscation.mirror.*;
import javax.lang.model.element.*;

public class ParentValidator extends MixinValidator
{
    public ParentValidator(final IMixinAnnotationProcessor ap) {
        super(ap, IMixinValidator.ValidationPass.EARLY);
    }
    
    public boolean validate(final TypeElement mixin, final AnnotationHandle annotation, final Collection<TypeHandle> targets) {
        if (mixin.getEnclosingElement().getKind() != ElementKind.PACKAGE && !mixin.getModifiers().contains(Modifier.STATIC)) {
            this.error("Inner class mixin must be declared static", (Element)mixin);
        }
        return true;
    }
}
