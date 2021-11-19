package wtf.moneymod.client.impl.module.render;

import wtf.moneymod.client.impl.module.*;
import wtf.moneymod.client.api.setting.annotatable.*;
import wtf.moneymod.client.mixin.mixins.ducks.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import java.util.*;
import wtf.moneymod.client.impl.utility.impl.math.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import wtf.moneymod.client.impl.utility.impl.render.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.text.*;
import net.minecraft.nbt.*;
import net.minecraft.item.*;
import java.awt.*;
import wtf.moneymod.client.api.management.impl.*;
import com.mojang.realmsclient.gui.*;
import wtf.moneymod.client.impl.utility.impl.world.*;
import net.minecraft.entity.*;
import net.minecraft.client.network.*;

@Module.Register(label = "NameTags", cat = Module.Category.RENDER)
public class NameTags extends Module
{
    @Value("Size")
    @Bounds(min = 0.1f, max = 1.0f)
    public float sizeNameTags;
    @Value("Range")
    @Bounds(min = 80.0f, max = 300.0f)
    public int range;
    @Value("Thickness")
    @Bounds(min = 0.1f, max = 3.0f)
    public float thickness;
    @Value("Fill Color")
    public JColor fillColor;
    @Value("Outline Color")
    public JColor outlineColor;
    @Value("Text Color")
    public JColor textColor;
    @Value("Fill")
    public boolean fill;
    @Value("Outline")
    public boolean outline;
    @Value("Self")
    public boolean self;
    @Value("Name")
    public boolean name;
    @Value("Friend")
    public boolean friend;
    @Value("Ping")
    public boolean ping;
    @Value("Health")
    public boolean health;
    @Value("HealthColor")
    public boolean healthcolor;
    @Value("Gamemode")
    public boolean gamemode;
    @Value("Totem")
    public boolean totems;
    @Value("Items")
    public boolean items;
    @Value("MainHand")
    public boolean mainhand;
    @Value("Offhand")
    public boolean offhand;
    @Value("Armor")
    public boolean armor;
    @Value("Armor Dura")
    public boolean armorDura;
    AccessorRenderManager renderManager;
    HashMap<String, Integer> totemPops;
    
    public NameTags() {
        this.sizeNameTags = 0.5f;
        this.range = 300;
        this.thickness = 1.0f;
        this.fillColor = new JColor(0, 0, 0, false);
        this.outlineColor = new JColor(0, 0, 0, false);
        this.textColor = new JColor(255, 255, 255, false);
        this.fill = true;
        this.outline = false;
        this.self = false;
        this.name = true;
        this.friend = true;
        this.ping = true;
        this.health = true;
        this.healthcolor = true;
        this.gamemode = false;
        this.totems = false;
        this.items = true;
        this.mainhand = true;
        this.offhand = true;
        this.armor = true;
        this.armorDura = true;
        this.renderManager = (AccessorRenderManager)NameTags.mc.getRenderManager();
        this.totemPops = new HashMap<String, Integer>();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        if (this.nullCheck()) {
            this.totemPops.clear();
            return;
        }
        NameTags.mc.world.loadedEntityList.forEach(e -> {
            if (e instanceof EntityPlayer && this.totemPops.containsKey(((Entity)e).getName()) && (e.getHealth() <= 0.0f || ((Entity)e).isDead || NameTags.mc.player.getDistance((Entity)e) >= this.range)) {
                this.totemPops.remove(((Entity)e).getName());
            }
        });
    }
    
    @SubscribeEvent
    public void onRender3D(final RenderWorldLastEvent event) {
        for (final Entity player : NameTags.mc.world.loadedEntityList) {
            if (player instanceof EntityPlayer && !player.isDead && ((EntityPlayer)player).getHealth() > 0.0f) {
                if (NameTags.mc.player.getDistance(player) > this.range) {
                    continue;
                }
                if (player == NameTags.mc.player) {
                    if (!this.self) {
                        continue;
                    }
                    this.renderNameTage((EntityPlayer)player);
                }
                else {
                    this.renderNameTage((EntityPlayer)player);
                }
            }
        }
    }
    
    private void renderNameTage(final EntityPlayer player) {
        if (NameTags.mc.getRenderViewEntity() == null) {
            return;
        }
        final MathUtil instance = MathUtil.INSTANCE;
        final double x = MathUtil.INSTANCE.interpolate(player.lastTickPosX, player.posX, NameTags.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosX();
        final double y = MathUtil.INSTANCE.interpolate(player.lastTickPosY, player.posY, NameTags.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosY() + (player.isSneaking() ? 0.5 : 0.7);
        final double z = MathUtil.INSTANCE.interpolate(player.lastTickPosZ, player.posZ, NameTags.mc.getRenderPartialTicks()) - this.renderManager.getRenderPosZ();
        final double delta = NameTags.mc.getRenderPartialTicks();
        final Entity localPlayer = NameTags.mc.getRenderViewEntity();
        final double originalPositionX = localPlayer.posX;
        final double originalPositionY = localPlayer.posY;
        final double originalPositionZ = localPlayer.posZ;
        localPlayer.posX = MathUtil.INSTANCE.interpolate(localPlayer.prevPosX, localPlayer.posX, delta);
        localPlayer.posY = MathUtil.INSTANCE.interpolate(localPlayer.prevPosY, localPlayer.posY, delta);
        localPlayer.posZ = MathUtil.INSTANCE.interpolate(localPlayer.prevPosZ, localPlayer.posZ, delta);
        final String tag = this.getTagString(player);
        final double distance = localPlayer.getDistance(x + NameTags.mc.getRenderManager().viewerPosX, y + NameTags.mc.getRenderManager().viewerPosY, z + NameTags.mc.getRenderManager().viewerPosZ);
        final int width = NameTags.mc.fontRenderer.getStringWidth(tag) >> 1;
        double scale = (float)(((distance / 5.0 <= 2.0) ? 2.0 : (distance / 5.0 * (this.sizeNameTags + 1.0f))) * 2.5 * (this.sizeNameTags / 100.0f));
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate(x, y + 1.399999976158142, z);
        GlStateManager.rotate(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(NameTags.mc.getRenderManager().playerViewX, (NameTags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GL11.glDepthRange(0.0, 0.1);
        if (this.fill) {
            Renderer2D.drawRect((float)(-width - 2), (float)(-(NameTags.mc.fontRenderer.FONT_HEIGHT + 1)), width + 2.0f, 1.5f, this.fillColor.getColor().getRGB());
        }
        if (this.outline) {
            Renderer2D.drawOutline(-width - 2, -(NameTags.mc.fontRenderer.FONT_HEIGHT + 1), width + 2.0f, 1.5, this.thickness, this.outlineColor.getColor().getRGB());
        }
        if (this.items) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (int i = 0; i < 4; ++i) {
                xOffset -= 8;
            }
            final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            xOffset -= 8;
            if (this.offhand) {
                this.renderItemStack(renderOffhand, xOffset);
                this.renderDurabilityLabel(renderOffhand, xOffset, -50);
            }
            xOffset += 16;
            for (int j = 0; j < 4; ++j) {
                if (this.armor) {
                    this.renderItemStack(((ItemStack)player.inventory.armorInventory.get(j)).copy(), xOffset);
                }
                if (this.armorDura) {
                    this.renderDurabilityLabel(((ItemStack)player.inventory.armorInventory.get(j)).copy(), xOffset, this.armor ? -50 : -21);
                }
                xOffset += 16;
            }
            if (this.mainhand) {
                this.renderItemStack(player.getHeldItemMainhand().copy(), xOffset);
                this.renderDurabilityLabel(player.getHeldItemMainhand().copy(), xOffset, -50);
            }
            GlStateManager.popMatrix();
        }
        NameTags.mc.fontRenderer.drawStringWithShadow(tag, (float)(-width), -8.0f, this.textColor.getColor().getRGB());
        localPlayer.posX = originalPositionX;
        localPlayer.posY = originalPositionY;
        localPlayer.posZ = originalPositionZ;
        GL11.glDepthRange(0.0, 1.0);
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderItemStack(final ItemStack stack, final int x) {
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        NameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -27);
        NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRenderer, stack, x, -27);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentLabel(stack, x, -27);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }
    
    private void renderEnchantmentLabel(final ItemStack stack, final int x, final int y) {
        int enchantmentY = y - 8;
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short value = enchants.getCompoundTagAt(index).getShort("value");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)value);
            if (enc != null && !enc.getName().contains("fall")) {
                if (enc.getName().contains("all") || enc.getName().contains("explosion")) {
                    NameTags.mc.fontRenderer.drawStringWithShadow(enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase()) : (enc.getTranslatedName((int)level).substring(0, 1).toLowerCase() + level), (float)(x * 2), (float)enchantmentY, -1);
                    enchantmentY -= 8;
                }
            }
        }
    }
    
    private void renderDurabilityLabel(final ItemStack stack, final int x, final int y) {
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            NameTags.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), (float)(y - 10), new Color((int)(red * 255.0f), (int)(green * 255.0f), 0).getRGB());
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
    }
    
    private String getTagString(final EntityPlayer player) {
        final StringBuilder sb = new StringBuilder();
        if (FriendManagement.getInstance().is(player.getName()) && this.friend) {
            sb.append(ChatFormatting.AQUA);
        }
        if (this.ping) {
            try {
                final NetworkPlayerInfo npi = NameTags.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
                sb.append(npi.getResponseTime());
            }
            catch (Exception e) {
                sb.append("0");
            }
            sb.append("ms");
        }
        if (this.name) {
            sb.append(" ").append(player.getName());
        }
        if (this.health) {
            if (this.healthcolor) {
                sb.append(this.getHealthColor(EntityUtil.getHealth((EntityLivingBase)player)));
            }
            sb.append(" ").append((int)EntityUtil.getHealth((EntityLivingBase)player)).append(ChatFormatting.RESET);
        }
        if (FriendManagement.getInstance().is(player.getName()) && this.friend) {
            sb.append(ChatFormatting.AQUA);
        }
        if (this.gamemode) {
            sb.append(" [");
            try {
                final String sus = this.getShortName(NameTags.mc.player.connection.getPlayerInfo(player.getGameProfile().getId()).getGameType().getName());
                sb.append(sus);
            }
            catch (Exception ignored) {
                sb.append("S");
            }
            sb.append("]");
        }
        if (this.totems) {
            this.totemPops.putIfAbsent(player.getName(), 0);
            if (this.totemPops.get(player.getName()) != 0) {
                sb.append(" -").append(this.totemPops.get(player.getName()));
            }
        }
        return sb.toString();
    }
    
    private ChatFormatting getHealthColor(final double health) {
        if (health >= 20.0) {
            return ChatFormatting.GREEN;
        }
        if (health >= 16.0) {
            return ChatFormatting.DARK_GREEN;
        }
        if (health >= 10.0) {
            return ChatFormatting.GOLD;
        }
        if (health >= 4.0) {
            return ChatFormatting.RED;
        }
        return ChatFormatting.DARK_RED;
    }
    
    private String getShortName(final String gameType) {
        if (gameType.equalsIgnoreCase("survival")) {
            return "S";
        }
        if (gameType.equalsIgnoreCase("creative")) {
            return "C";
        }
        if (gameType.equalsIgnoreCase("adventure")) {
            return "A";
        }
        if (gameType.equalsIgnoreCase("spectator")) {
            return "SP";
        }
        return "NONE";
    }
}
