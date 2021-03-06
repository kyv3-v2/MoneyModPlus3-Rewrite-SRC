package javassist.bytecode;

import java.util.*;
import java.io.*;

class NameAndTypeInfo extends ConstInfo
{
    static final int tag = 12;
    int memberName;
    int typeDescriptor;
    
    public NameAndTypeInfo(final int name, final int type, final int index) {
        super(index);
        this.memberName = name;
        this.typeDescriptor = type;
    }
    
    public NameAndTypeInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.memberName = in.readUnsignedShort();
        this.typeDescriptor = in.readUnsignedShort();
    }
    
    public int hashCode() {
        return this.memberName << 16 ^ this.typeDescriptor;
    }
    
    public boolean equals(final Object obj) {
        if (obj instanceof NameAndTypeInfo) {
            final NameAndTypeInfo nti = (NameAndTypeInfo)obj;
            return nti.memberName == this.memberName && nti.typeDescriptor == this.typeDescriptor;
        }
        return false;
    }
    
    public int getTag() {
        return 12;
    }
    
    public void renameClass(final ConstPool cp, final String oldName, final String newName, final HashMap cache) {
        final String type = cp.getUtf8Info(this.typeDescriptor);
        final String type2 = Descriptor.rename(type, oldName, newName);
        if (type != type2) {
            if (cache == null) {
                this.typeDescriptor = cp.addUtf8Info(type2);
            }
            else {
                cache.remove(this);
                this.typeDescriptor = cp.addUtf8Info(type2);
                cache.put(this, this);
            }
        }
    }
    
    public void renameClass(final ConstPool cp, final Map map, final HashMap cache) {
        final String type = cp.getUtf8Info(this.typeDescriptor);
        final String type2 = Descriptor.rename(type, map);
        if (type != type2) {
            if (cache == null) {
                this.typeDescriptor = cp.addUtf8Info(type2);
            }
            else {
                cache.remove(this);
                this.typeDescriptor = cp.addUtf8Info(type2);
                cache.put(this, this);
            }
        }
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        final String mname = src.getUtf8Info(this.memberName);
        String tdesc = src.getUtf8Info(this.typeDescriptor);
        tdesc = Descriptor.rename(tdesc, map);
        return dest.addNameAndTypeInfo(dest.addUtf8Info(mname), dest.addUtf8Info(tdesc));
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(12);
        out.writeShort(this.memberName);
        out.writeShort(this.typeDescriptor);
    }
    
    public void print(final PrintWriter out) {
        out.print("NameAndType #");
        out.print(this.memberName);
        out.print(", type #");
        out.println(this.typeDescriptor);
    }
}
