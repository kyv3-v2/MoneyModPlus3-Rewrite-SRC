package javassist.bytecode;

import java.util.*;
import java.io.*;

class MethodTypeInfo extends ConstInfo
{
    static final int tag = 16;
    int descriptor;
    
    public MethodTypeInfo(final int desc, final int index) {
        super(index);
        this.descriptor = desc;
    }
    
    public MethodTypeInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.descriptor = in.readUnsignedShort();
    }
    
    public int hashCode() {
        return this.descriptor;
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof MethodTypeInfo && ((MethodTypeInfo)obj).descriptor == this.descriptor;
    }
    
    public int getTag() {
        return 16;
    }
    
    public void renameClass(final ConstPool cp, final String oldName, final String newName, final HashMap cache) {
        final String desc = cp.getUtf8Info(this.descriptor);
        final String desc2 = Descriptor.rename(desc, oldName, newName);
        if (desc != desc2) {
            if (cache == null) {
                this.descriptor = cp.addUtf8Info(desc2);
            }
            else {
                cache.remove(this);
                this.descriptor = cp.addUtf8Info(desc2);
                cache.put(this, this);
            }
        }
    }
    
    public void renameClass(final ConstPool cp, final Map map, final HashMap cache) {
        final String desc = cp.getUtf8Info(this.descriptor);
        final String desc2 = Descriptor.rename(desc, map);
        if (desc != desc2) {
            if (cache == null) {
                this.descriptor = cp.addUtf8Info(desc2);
            }
            else {
                cache.remove(this);
                this.descriptor = cp.addUtf8Info(desc2);
                cache.put(this, this);
            }
        }
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        String desc = src.getUtf8Info(this.descriptor);
        desc = Descriptor.rename(desc, map);
        return dest.addMethodTypeInfo(dest.addUtf8Info(desc));
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(16);
        out.writeShort(this.descriptor);
    }
    
    public void print(final PrintWriter out) {
        out.print("MethodType #");
        out.println(this.descriptor);
    }
}
