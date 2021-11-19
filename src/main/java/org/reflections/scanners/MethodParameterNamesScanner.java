package org.reflections.scanners;

import javassist.bytecode.*;
import java.lang.reflect.*;
import com.google.common.base.*;
import org.reflections.adapters.*;
import java.util.*;

public class MethodParameterNamesScanner extends AbstractScanner
{
    public void scan(final Object cls) {
        final MetadataAdapter md = this.getMetadataAdapter();
        for (final Object method : md.getMethods(cls)) {
            final String key = md.getMethodFullKey(cls, method);
            if (this.acceptResult(key)) {
                final LocalVariableAttribute table = (LocalVariableAttribute)((MethodInfo)method).getCodeAttribute().getAttribute("LocalVariableTable");
                final int length = table.tableLength();
                int i = Modifier.isStatic(((MethodInfo)method).getAccessFlags()) ? 0 : 1;
                if (i >= length) {
                    continue;
                }
                final List<String> names = new ArrayList<String>(length - i);
                while (i < length) {
                    names.add(((MethodInfo)method).getConstPool().getUtf8Info(table.nameIndex(i++)));
                }
                this.getStore().put((Object)key, (Object)Joiner.on(", ").join((Iterable)names));
            }
        }
    }
}
