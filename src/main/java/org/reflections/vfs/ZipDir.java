package org.reflections.vfs;

import java.util.jar.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.zip.*;
import org.reflections.*;
import java.io.*;

public class ZipDir implements Vfs.Dir
{
    final ZipFile jarFile;
    
    public ZipDir(final JarFile jarFile) {
        this.jarFile = jarFile;
    }
    
    public String getPath() {
        return this.jarFile.getName();
    }
    
    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                return (Iterator<Vfs.File>)new AbstractIterator<Vfs.File>() {
                    final Enumeration<? extends ZipEntry> entries = ZipDir.this.jarFile.entries();
                    
                    protected Vfs.File computeNext() {
                        while (this.entries.hasMoreElements()) {
                            final ZipEntry entry = (ZipEntry)this.entries.nextElement();
                            if (!entry.isDirectory()) {
                                return (Vfs.File)new org.reflections.vfs.ZipFile(ZipDir.this, entry);
                            }
                        }
                        return (Vfs.File)this.endOfData();
                    }
                };
            }
        };
    }
    
    public void close() {
        try {
            this.jarFile.close();
        }
        catch (IOException e) {
            if (Reflections.log != null) {
                Reflections.log.warn("Could not close JarFile", (Throwable)e);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.jarFile.getName();
    }
}
