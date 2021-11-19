package javassist.bytecode;

import java.util.*;
import java.io.*;

class IntegerInfo extends ConstInfo
{
    static final int tag = 3;
    int value;
    
    public IntegerInfo(final int v, final int index) {
        super(index);
        this.value = v;
    }
    
    public IntegerInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.value = in.readInt();
    }
    
    public int hashCode() {
        return this.value;
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof IntegerInfo && ((IntegerInfo)obj).value == this.value;
    }
    
    public int getTag() {
        return 3;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map map) {
        return dest.addIntegerInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(3);
        out.writeInt(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Integer ");
        out.println(this.value);
    }
}
