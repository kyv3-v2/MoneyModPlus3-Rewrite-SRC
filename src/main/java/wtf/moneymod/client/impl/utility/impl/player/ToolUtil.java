package wtf.moneymod.client.impl.utility.impl.player;

import wtf.moneymod.client.impl.utility.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.state.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;

public enum ToolUtil implements Globals
{
    INSTANCE;
    
    public boolean canHarvestBlock(final Block block, final BlockPos pos, final ItemStack stack) {
        IBlockState state = ToolUtil.mc.world.getBlockState(pos);
        state = state.getBlock().getActualState(state, (IBlockAccess)ToolUtil.mc.world, pos);
        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }
        final String tool = block.getHarvestTool(state);
        if (stack.isEmpty() || tool == null) {
            return ToolUtil.mc.player.canHarvestBlock(state);
        }
        final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, (EntityPlayer)ToolUtil.mc.player, state);
        if (toolLevel < 0) {
            return ToolUtil.mc.player.canHarvestBlock(state);
        }
        return toolLevel >= block.getHarvestLevel(state);
    }
    
    public int bestSlot(final BlockPos pos) {
        int best = 0;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = ToolUtil.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                float speed = stack.getDestroySpeed(ToolUtil.mc.world.getBlockState(pos));
                if (speed > 1.0f) {
                    final int eff;
                    speed += (float)(((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        best = i;
                    }
                }
            }
        }
        return best;
    }
    
    public long time(final BlockPos pos) {
        return this.time(pos, EnumHand.MAIN_HAND);
    }
    
    public long time(final BlockPos pos, final EnumHand hand) {
        return this.time(pos, ToolUtil.mc.player.getHeldItem(hand));
    }
    
    public long time(final BlockPos pos, final ItemStack stack) {
        final IBlockState state = ToolUtil.mc.world.getBlockState(pos);
        final Block block = state.getBlock();
        float toolMultiplier = stack.getDestroySpeed(state);
        if (!this.canHarvestBlock(block, pos, stack) && block != Blocks.ENDER_CHEST) {
            toolMultiplier = 1.0f;
        }
        else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) != 0) {
            toolMultiplier += (float)(Math.pow(EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack), 2.0) + 1.0);
        }
        if (ToolUtil.mc.player.isPotionActive(MobEffects.HASTE)) {
            toolMultiplier *= 1.0f + (ToolUtil.mc.player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2f;
        }
        if (ToolUtil.mc.player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1 = 0.0f;
            switch (ToolUtil.mc.player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0: {
                    f1 = 0.3f;
                    break;
                }
                case 1: {
                    f1 = 0.09f;
                    break;
                }
                case 2: {
                    f1 = 0.0027f;
                    break;
                }
                default: {
                    f1 = 8.1E-4f;
                    break;
                }
            }
            toolMultiplier *= f1;
        }
        if (ToolUtil.mc.player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier((EntityLivingBase)ToolUtil.mc.player)) {
            toolMultiplier /= 5.0f;
        }
        float dmg = toolMultiplier / state.getBlockHardness((World)ToolUtil.mc.world, pos);
        if (this.canHarvestBlock(block, pos, stack) || block == Blocks.ENDER_CHEST) {
            dmg /= 30.0f;
        }
        else {
            dmg /= 100.0f;
        }
        if (dmg > 1.0f) {
            dmg = 0.0f;
        }
        final float ticks = (float)(Math.floor(1.0f / dmg) + 1.0);
        return (long)(ticks / 20.0f * 1000.0f);
    }
}
