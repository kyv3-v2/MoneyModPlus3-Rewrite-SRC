package javassist.bytecode;

import javassist.bytecode.annotation.*;
import java.io.*;
import java.util.*;

public class ParameterAnnotationsAttribute extends AttributeInfo
{
    public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";
    
    public ParameterAnnotationsAttribute(final ConstPool cp, final String attrname, final byte[] info) {
        super(cp, attrname, info);
    }
    
    public ParameterAnnotationsAttribute(final ConstPool cp, final String attrname) {
        this(cp, attrname, new byte[] { 0 });
    }
    
    ParameterAnnotationsAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    public int numParameters() {
        return this.info[0] & 0xFF;
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map classnames) {
        final AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);
        try {
            copier.parameters();
            return new ParameterAnnotationsAttribute(newCp, this.getName(), copier.close());
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    public Annotation[][] getAnnotations() {
        try {
            return new AnnotationsAttribute.Parser(this.info, this.constPool).parseParameters();
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    public void setAnnotations(final Annotation[][] params) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final AnnotationsWriter writer = new AnnotationsWriter((OutputStream)output, this.constPool);
        try {
            final int n = params.length;
            writer.numParameters(n);
            for (final Annotation[] anno : params) {
                writer.numAnnotations(anno.length);
                for (int j = 0; j < anno.length; ++j) {
                    anno[j].write(writer);
                }
            }
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.set(output.toByteArray());
    }
    
    void renameClass(final String oldname, final String newname) {
        final HashMap map = new HashMap();
        map.put(oldname, newname);
        this.renameClass(map);
    }
    
    void renameClass(final Map classnames) {
        final AnnotationsAttribute.Renamer renamer = new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), classnames);
        try {
            renamer.parameters();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    void getRefClasses(final Map classnames) {
        this.renameClass(classnames);
    }
    
    public String toString() {
        final Annotation[][] aa = this.getAnnotations();
        final StringBuilder sbuf = new StringBuilder();
        int k = 0;
        while (k < aa.length) {
            final Annotation[] a = aa[k++];
            int i = 0;
            while (i < a.length) {
                sbuf.append(a[i++].toString());
                if (i != a.length) {
                    sbuf.append(" ");
                }
            }
            if (k != aa.length) {
                sbuf.append(", ");
            }
        }
        return sbuf.toString();
    }
}
