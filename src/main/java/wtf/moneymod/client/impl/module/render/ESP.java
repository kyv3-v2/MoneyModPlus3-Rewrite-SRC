package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import net.minecraft.util.math.*;
import wtf.moneymod.client.impl.utility.impl.misc.*;
import wtf.moneymod.client.impl.utility.impl.shader.*;
import wtf.moneymod.eventhandler.listener.*;
import wtf.moneymod.client.api.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.init.*;
import wtf.moneymod.eventhandler.event.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import wtf.moneymod.client.impl.utility.impl.shader.impl.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;

@Module.Register(label = "ESP", cat = Module.Category.RENDER, exception = true)
public class ESP extends Module
{
    @Value("Players")
    public boolean players;
    @Value("Crystals")
    public boolean crystals;
    @Value("Shaders")
    public boolean shaders;
    @Value("Shader")
    public Shader shader;
    @Value("Color")
    public JColor color;
    @Value("ChorusPredict")
    public boolean chorusPredict;
    @Value("Delay (Sec)")
    @Bounds(min = 1.0f, max = 32.0f)
    public int delay;
    BlockPos predictChorus;
    private final Timer timer;
    boolean nameTags;
    public static ESP INSTANCE;
    public FramebufferShader framebuffer;
    @Handler
    public Listener<PacketEvent.Receive> packetEventReceive;
    @Handler
    public Listener<RenderNameTagEvent> eventListener;
    
    public ESP() {
        this.players = true;
        this.crystals = true;
        this.shaders = true;
        this.shader = Shader.OUTLINE;
        this.color = new JColor(0, 255, 0, false);
        this.chorusPredict = true;
        this.delay = 5;
        this.timer = new Timer();
        this.framebuffer = null;
        SPacketSoundEffect packet;
        this.packetEventReceive = new Listener<PacketEvent.Receive>((Class<? extends Event>)PacketEvent.Receive.class, e -> {
            if (e.getPacket() instanceof SPacketSoundEffect && this.chorusPredict) {
                packet = (SPacketSoundEffect)e.getPacket();
                if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                    this.predictChorus = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                }
            }
            return;
        });
        this.eventListener = new Listener<RenderNameTagEvent>((Class<? extends Event>)RenderNameTagEvent.class, e -> {
            if (this.nameTags) {
                e.cancel();
            }
            return;
        });
        ESP.INSTANCE = this;
    }
    
    public void onToggle() {
        this.predictChorus = null;
    }
    
    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (this.predictChorus != null) {
            if (this.timer.passed(this.delay * 1000)) {
                this.predictChorus = null;
                this.timer.reset();
                return;
            }
            Renderer3D.drawBoxESP(this.predictChorus, this.color.getColor(), 1.0f, true, true, this.color.getColor().getAlpha(), this.color.getColor().getAlpha(), 1.0f);
        }
    }
    
    @SubscribeEvent
    public void onRender2d(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && this.shaders) {
            switch (this.shader) {
                case OUTLINE: {
                    this.framebuffer = OutlineShader.INSTANCE;
                    break;
                }
                case GLOW: {
                    this.framebuffer = GlowShader.INSTANCE;
                    break;
                }
                case SPACE: {
                    this.framebuffer = SpaceShader.INSTANCE;
                    break;
                }
                case SPACESMOKE: {
                    this.framebuffer = SpaceSmokeShader.INSTANCE;
                    break;
                }
            }
            this.framebuffer.startDraw(event.getPartialTicks());
            this.nameTags = true;
            ESP.mc.world.loadedEntityList.forEach(e -> {
                if (e != ESP.mc.player && ((e instanceof EntityPlayer && this.players) || (e instanceof EntityEnderCrystal && this.crystals))) {
                    ESP.mc.getRenderManager().renderEntityStatic((Entity)e, event.getPartialTicks(), true);
                }
                return;
            });
            this.nameTags = false;
            this.framebuffer.stopDraw(this.color.getColor(), 1.0f, 1.0f, 0.8f, 1.0f, 0.5f, 0.5f);
        }
    }
    
    public enum Shader
    {
        OUTLINE, 
        GLOW, 
        SPACE, 
        SPACESMOKE;
    }
}
