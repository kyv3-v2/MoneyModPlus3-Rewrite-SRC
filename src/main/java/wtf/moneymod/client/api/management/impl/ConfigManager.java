package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.impl.utility.*;
import wtf.moneymod.client.*;
import wtf.moneymod.client.impl.module.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import wtf.moneymod.client.api.setting.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import com.google.gson.*;
import wtf.moneymod.client.impl.utility.impl.render.*;

public class ConfigManager extends Thread implements Globals
{
    private static final ConfigManager CONFIG_MANAGER;
    private static final File mainFolder;
    private static final String modulesFolder;
    
    public static File getMainFolder() {
        return ConfigManager.mainFolder;
    }
    
    public void load() {
        try {
            this.loadModules();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadModules() throws Exception {
        for (final Module m : Main.getMain().getModuleManager()) {
            this.loadModule(m);
        }
    }
    
    private void loadModule(final Module m) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: iconst_1       
        //     4: anewarray       Ljava/lang/String;
        //     7: dup            
        //     8: iconst_0       
        //     9: new             Ljava/lang/StringBuilder;
        //    12: dup            
        //    13: invokespecial   java/lang/StringBuilder.<init>:()V
        //    16: aload_1         /* m */
        //    17: invokevirtual   wtf/moneymod/client/impl/module/Module.getLabel:()Ljava/lang/String;
        //    20: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    23: ldc             ".json"
        //    25: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    28: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    31: aastore        
        //    32: invokestatic    java/nio/file/Paths.get:(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
        //    35: astore_2        /* path */
        //    36: aload_2         /* path */
        //    37: invokeinterface java/nio/file/Path.toFile:()Ljava/io/File;
        //    42: invokevirtual   java/io/File.exists:()Z
        //    45: ifne            49
        //    48: return         
        //    49: aload_0         /* this */
        //    50: aload_2         /* path */
        //    51: invokeinterface java/nio/file/Path.toFile:()Ljava/io/File;
        //    56: invokevirtual   wtf/moneymod/client/api/management/impl/ConfigManager.loadFile:(Ljava/io/File;)Ljava/lang/String;
        //    59: astore_3        /* rawJson */
        //    60: new             Lcom/google/gson/JsonParser;
        //    63: dup            
        //    64: invokespecial   com/google/gson/JsonParser.<init>:()V
        //    67: aload_3         /* rawJson */
        //    68: invokevirtual   com/google/gson/JsonParser.parse:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //    71: invokevirtual   com/google/gson/JsonElement.getAsJsonObject:()Lcom/google/gson/JsonObject;
        //    74: astore          jsonObject
        //    76: aload           jsonObject
        //    78: ldc             "Enabled"
        //    80: invokevirtual   com/google/gson/JsonObject.get:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //    83: ifnull          135
        //    86: aload           jsonObject
        //    88: ldc             "KeyBind"
        //    90: invokevirtual   com/google/gson/JsonObject.get:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //    93: ifnull          135
        //    96: aload           jsonObject
        //    98: ldc             "Enabled"
        //   100: invokevirtual   com/google/gson/JsonObject.get:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //   103: invokevirtual   com/google/gson/JsonElement.getAsBoolean:()Z
        //   106: ifeq            121
        //   109: aload_1         /* m */
        //   110: invokevirtual   wtf/moneymod/client/impl/module/Module.isConfigException:()Z
        //   113: ifne            121
        //   116: aload_1         /* m */
        //   117: iconst_1       
        //   118: invokevirtual   wtf/moneymod/client/impl/module/Module.setToggled:(Z)V
        //   121: aload_1         /* m */
        //   122: aload           jsonObject
        //   124: ldc             "KeyBind"
        //   126: invokevirtual   com/google/gson/JsonObject.get:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //   129: invokevirtual   com/google/gson/JsonElement.getAsInt:()I
        //   132: invokevirtual   wtf/moneymod/client/impl/module/Module.setKey:(I)V
        //   135: aload_1         /* m */
        //   136: invokestatic    wtf/moneymod/client/api/setting/Option.getContainersForObject:(Ljava/lang/Object;)Ljava/util/List;
        //   139: aload           jsonObject
        //   141: invokedynamic   BootstrapMethod #0, accept:(Lcom/google/gson/JsonObject;)Ljava/util/function/Consumer;
        //   146: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
        //   151: return         
        //    Exceptions:
        //  throws java.lang.Exception
        //    StackMapTable: 00 03 FC 00 31 07 00 64 FD 00 47 07 00 4C 07 00 81 0D
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.Decompiler.decompile(Decompiler.java:70)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.decompile(Deobfuscator3000.java:536)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.decompileAndDeobfuscate(Deobfuscator3000.java:550)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.processMod(Deobfuscator3000.java:508)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.lambda$18(Deobfuscator3000.java:328)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public String loadFile(final File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        final StringBuilder resultStringBuilder = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    
    @Override
    public void run() {
        if (!ConfigManager.mainFolder.exists() && !ConfigManager.mainFolder.mkdirs()) {
            System.out.println("Failed to create config folder");
        }
        if (!new File(ConfigManager.modulesFolder).exists() && !new File(ConfigManager.modulesFolder).mkdirs()) {
            System.out.println("Failed to create modules folder");
        }
        try {
            this.saveModules();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveModules() throws IOException {
        for (final Module m : Main.getMain().getModuleManager()) {
            this.saveModule(m);
        }
    }
    
    private void saveModule(final Module m) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: iconst_1       
        //     4: anewarray       Ljava/lang/String;
        //     7: dup            
        //     8: iconst_0       
        //     9: new             Ljava/lang/StringBuilder;
        //    12: dup            
        //    13: invokespecial   java/lang/StringBuilder.<init>:()V
        //    16: aload_1         /* m */
        //    17: invokevirtual   wtf/moneymod/client/impl/module/Module.getLabel:()Ljava/lang/String;
        //    20: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    23: ldc             ".json"
        //    25: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    28: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    31: aastore        
        //    32: invokestatic    java/nio/file/Paths.get:(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
        //    35: astore_2        /* path */
        //    36: aload_0         /* this */
        //    37: aload_2         /* path */
        //    38: invokespecial   wtf/moneymod/client/api/management/impl/ConfigManager.createFile:(Ljava/nio/file/Path;)V
        //    41: new             Lcom/google/gson/JsonObject;
        //    44: dup            
        //    45: invokespecial   com/google/gson/JsonObject.<init>:()V
        //    48: astore_3        /* jsonObject */
        //    49: aload_3         /* jsonObject */
        //    50: ldc             "Enabled"
        //    52: new             Lcom/google/gson/JsonPrimitive;
        //    55: dup            
        //    56: aload_1         /* m */
        //    57: invokevirtual   wtf/moneymod/client/impl/module/Module.isToggled:()Z
        //    60: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    63: invokespecial   com/google/gson/JsonPrimitive.<init>:(Ljava/lang/Boolean;)V
        //    66: invokevirtual   com/google/gson/JsonObject.add:(Ljava/lang/String;Lcom/google/gson/JsonElement;)V
        //    69: aload_3         /* jsonObject */
        //    70: ldc             "KeyBind"
        //    72: new             Lcom/google/gson/JsonPrimitive;
        //    75: dup            
        //    76: aload_1         /* m */
        //    77: invokevirtual   wtf/moneymod/client/impl/module/Module.getKey:()I
        //    80: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    83: invokespecial   com/google/gson/JsonPrimitive.<init>:(Ljava/lang/Number;)V
        //    86: invokevirtual   com/google/gson/JsonObject.add:(Ljava/lang/String;Lcom/google/gson/JsonElement;)V
        //    89: aload_1         /* m */
        //    90: invokestatic    wtf/moneymod/client/api/setting/Option.getContainersForObject:(Ljava/lang/Object;)Ljava/util/List;
        //    93: aload_3         /* jsonObject */
        //    94: invokedynamic   BootstrapMethod #1, accept:(Lcom/google/gson/JsonObject;)Ljava/util/function/Consumer;
        //    99: invokeinterface java/util/List.forEach:(Ljava/util/function/Consumer;)V
        //   104: aload_2         /* path */
        //   105: getstatic       wtf/moneymod/client/api/management/impl/ConfigManager.gson:Lcom/google/gson/Gson;
        //   108: new             Lcom/google/gson/JsonParser;
        //   111: dup            
        //   112: invokespecial   com/google/gson/JsonParser.<init>:()V
        //   115: aload_3         /* jsonObject */
        //   116: invokevirtual   com/google/gson/JsonObject.toString:()Ljava/lang/String;
        //   119: invokevirtual   com/google/gson/JsonParser.parse:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
        //   122: invokevirtual   com/google/gson/Gson.toJson:(Lcom/google/gson/JsonElement;)Ljava/lang/String;
        //   125: invokevirtual   java/lang/String.getBytes:()[B
        //   128: iconst_0       
        //   129: anewarray       Ljava/nio/file/OpenOption;
        //   132: invokestatic    java/nio/file/Files.write:(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;
        //   135: pop            
        //   136: return         
        //    Exceptions:
        //  throws java.io.IOException
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.Decompiler.decompile(Decompiler.java:70)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.decompile(Deobfuscator3000.java:536)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.decompileAndDeobfuscate(Deobfuscator3000.java:550)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.processMod(Deobfuscator3000.java:508)
        //     at org.ugp.mc.deobfuscator.Deobfuscator3000.lambda$18(Deobfuscator3000.java:328)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void createFile(final Path path) {
        if (Files.exists(path, new LinkOption[0])) {
            new File(path.normalize().toString()).delete();
        }
        try {
            Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static ConfigManager getInstance() {
        return ConfigManager.CONFIG_MANAGER;
    }
    
    static {
        mainFolder = new File("moneymod");
        modulesFolder = ConfigManager.mainFolder.getAbsolutePath() + "/modules";
        CONFIG_MANAGER = new ConfigManager();
    }
}
