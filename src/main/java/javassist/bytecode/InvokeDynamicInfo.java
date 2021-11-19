package javassist.bytecode;

import java.util.*;
import java.io.*;

class InvokeDynamicInfo extends ConstInfo
{
    static final int tag = 18;
    int bootstrap;
    int nameAndType;
    
    public InvokeDynamicInfo(final int bootstrapMethod, final int ntIndex, final int index) {
        super(index);
        this.bootstrap = bootstrapMethod;
        this.nameAndType = ntIndex;
    }
    
    public InvokeDynamicInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.bootstrap = in.readUnsignedShort();
        this.nameAndType = in.readUnsignedShort();
    }
    
    public int hashCode() {
        return this.bootstrap << 16 ^ this.nameAndType;
    }
    
    public boolean equals(final Object obj) {
        if (obj instanceof InvokeDynamicInfo) {
            final InvokeDynamicInfo iv = (InvokeDynamicInfo)obj;
            return iv.bootstrap == this.bootstrap && iv.nameAndType == this.nameAndType;
        }
        return false;
    }
    
    public int getTag() {
        return 18;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addInvokeDynamicInfo(this.bootstrap, src.getItem(this.nameAndType).copy(src, dest, map));
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(18);
        out.writeShort(this.bootstrap);
        out.writeShort(this.nameAndType);
    }
    
    public void print(final PrintWriter out) {
        out.print("InvokeDynamic #");
        out.print(this.bootstrap);
        out.print(", name&type #");
        out.println(this.nameAndType);
    }
}
