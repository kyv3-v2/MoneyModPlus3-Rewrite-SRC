package org.reflections.vfs;

import java.net.*;
import java.util.jar.*;
import java.util.*;
import com.google.common.collect.*;
import org.reflections.*;
import java.util.zip.*;
import org.reflections.util.*;
import java.io.*;

public class JarInputDir implements Vfs.Dir
{
    private final URL url;
    JarInputStream jarInputStream;
    long cursor;
    long nextCursor;
    
    public JarInputDir(final URL url) {
        this.cursor = 0L;
        this.nextCursor = 0L;
        this.url = url;
    }
    
    @Override
    public String getPath() {
        return this.url.getPath();
    }
    
    @Override
    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                return (Iterator<Vfs.File>)new AbstractIterator<Vfs.File>() {
                    {
                        try {
                            JarInputDir.this.jarInputStream = new JarInputStream(JarInputDir.this.url.openConnection().getInputStream());
                        }
                        catch (Exception e) {
                            throw new ReflectionsException("Could not open url connection", (Throwable)e);
                        }
                    }
                    
                    protected Vfs.File computeNext() {
                        try {
                            while (true) {
                                final ZipEntry entry = JarInputDir.this.jarInputStream.getNextJarEntry();
                                if (entry == null) {
                                    return (Vfs.File)this.endOfData();
                                }
                                long size = entry.getSize();
                                if (size < 0L) {
                                    size += 4294967295L;
                                }
                                final JarInputDir this$0 = JarInputDir.this;
                                this$0.nextCursor += size;
                                if (!entry.isDirectory()) {
                                    return new JarInputFile(entry, JarInputDir.this, JarInputDir.this.cursor, JarInputDir.this.nextCursor);
                                }
                            }
                        }
                        catch (IOException e) {
                            throw new ReflectionsException("could not get next zip entry", (Throwable)e);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public void close() {
        Utils.close((InputStream)this.jarInputStream);
    }
}
