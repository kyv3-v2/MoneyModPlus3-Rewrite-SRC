package org.spongepowered.tools.obfuscation.interfaces;

import javax.annotation.processing.*;
import org.spongepowered.asm.util.*;

public interface IMixinAnnotationProcessor extends Messager, IOptionProvider
{
    CompilerEnvironment getCompilerEnvironment();
    
    ProcessingEnvironment getProcessingEnvironment();
    
    IObfuscationManager getObfuscationManager();
    
    ITokenProvider getTokenProvider();
    
    ITypeHandleProvider getTypeProvider();
    
    IJavadocProvider getJavadocProvider();
    
    public enum CompilerEnvironment
    {
        JAVAC, 
        JDT;
    }
}
