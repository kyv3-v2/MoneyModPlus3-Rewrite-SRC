package wtf.moneymod.client;

import wtf.moneymod.eventhandler.*;
import wtf.moneymod.client.impl.ui.click.*;
import net.minecraftforge.common.*;
import wtf.moneymod.client.api.forge.*;
import wtf.moneymod.client.api.management.impl.*;
import org.lwjgl.opengl.*;
import com.google.common.hash.*;
import java.nio.charset.*;
import wtf.moneymod.client.impl.module.*;

public class Main
{
    public static final String MODID = "moneymod";
    public static final String NAME = "Money Mod";
    public static String VERSION;
    private static Main main;
    public static float TICK_TIMER;
    public static final EventBus EVENT_BUS;
    private Screen screen;
    private FpsManagement fpsManagement;
    private ModuleManagement moduleManagement;
    private CommandManagement commandManagement;
    private RotationManagement rotationManagement;
    
    public void init() {
        System.out.println("init");
        this.fpsManagement = new FpsManagement();
        this.rotationManagement = new RotationManagement();
        this.moduleManagement = new ModuleManagement().register();
        Main.VERSION = this.getHash(this.moduleManagement);
        this.commandManagement = new CommandManagement().register();
        ConfigManager.getInstance().load();
        this.screen = new Screen();
        MinecraftForge.EVENT_BUS.register((Object)new EventHandler());
        MinecraftForge.EVENT_BUS.register((Object)PacketManagement.getInstance());
        Runtime.getRuntime().addShutdownHook((Thread)ConfigManager.getInstance());
        Display.setTitle(String.format("moneymod build-%s", Main.VERSION));
    }
    
    public static Main getMain() {
        if (Main.main == null) {
            Main.main = new Main();
        }
        return Main.main;
    }
    
    public ModuleManagement getModuleManager() {
        return this.moduleManagement;
    }
    
    public CommandManagement getCommandManagement() {
        return this.commandManagement;
    }
    
    public FpsManagement getFpsManagement() {
        return this.fpsManagement;
    }
    
    public RotationManagement getRotationManagement() {
        return this.rotationManagement;
    }
    
    public Screen getScreen() {
        return this.screen;
    }
    
    private String getHash(final ModuleManagement module) {
        final StringBuilder sb = new StringBuilder();
        module.forEach(m -> sb.append(m.getClass().hashCode()));
        sb.append(this.getClass().hashCode());
        return Hashing.sha256().hashString((CharSequence)sb.toString(), StandardCharsets.UTF_8).toString().substring(0, 10);
    }
    
    static {
        Main.VERSION = "0.0";
        Main.TICK_TIMER = 1.0f;
        EVENT_BUS = new EventBus();
    }
}
