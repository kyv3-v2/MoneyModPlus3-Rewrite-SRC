package org.reflections.vfs;

import java.util.zip.*;
import java.io.*;

public class ZipFile implements Vfs.File
{
    private final ZipDir root;
    private final ZipEntry entry;
    
    public ZipFile(final ZipDir root, final ZipEntry entry) {
        this.root = root;
        this.entry = entry;
    }
    
    public String getName() {
        final String name = this.entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }
    
    public String getRelativePath() {
        return this.entry.getName();
    }
    
    public InputStream openInputStream() throws IOException {
        return this.root.jarFile.getInputStream(this.entry);
    }
    
    @Override
    public String toString() {
        return this.root.getPath() + "!" + File.separatorChar + this.entry.toString();
    }
}
