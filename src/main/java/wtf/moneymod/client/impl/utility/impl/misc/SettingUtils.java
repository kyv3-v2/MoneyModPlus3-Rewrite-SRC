package wtf.moneymod.client.impl.utility.impl.misc;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public enum SettingUtils
{
    INSTANCE;
    
    public ArrayList<String> enumValues(final Enum clazz) {
        return Arrays.stream(clazz.getClass().getEnumConstants()).map((Function<? super Enum, ?>)Enum::name).collect((Collector<? super Object, ?, ArrayList<String>>)Collectors.toCollection((Supplier<R>)ArrayList::new));
    }
    
    public int currentEnum(final Enum clazz) {
        for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ++i) {
            final Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
            if (e.name().equalsIgnoreCase(clazz.name())) {
                return i;
            }
        }
        return -1;
    }
    
    public Enum increaseEnum(final Enum clazz) {
        final int index = this.currentEnum(clazz);
        for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ++i) {
            final Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
            if (i == index + 1) {
                return e;
            }
        }
        return ((Enum[])clazz.getClass().getEnumConstants())[0];
    }
    
    public String getProperName(final Enum clazz) {
        return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
    }
    
    public Enum getProperEnum(final Enum clazz, final String name) {
        for (int i = 0; i < ((Enum[])clazz.getClass().getEnumConstants()).length; ++i) {
            final Enum e = ((Enum[])clazz.getClass().getEnumConstants())[i];
            if (this.getProperName(e).equalsIgnoreCase(name)) {
                return e;
            }
        }
        return ((Enum[])clazz.getClass().getEnumConstants())[0];
    }
}
