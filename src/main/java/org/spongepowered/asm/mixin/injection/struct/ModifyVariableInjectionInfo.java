package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.transformer.*;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.injection.code.*;
import org.spongepowered.asm.mixin.injection.modify.*;

public class ModifyVariableInjectionInfo extends InjectionInfo
{
    public ModifyVariableInjectionInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
        super(mixin, method, annotation);
    }
    
    protected Injector parseInjector(final AnnotationNode injectAnnotation) {
        return (Injector)new ModifyVariableInjector((InjectionInfo)this, LocalVariableDiscriminator.parse(injectAnnotation));
    }
    
    protected String getDescription() {
        return "Variable modifier method";
    }
}
