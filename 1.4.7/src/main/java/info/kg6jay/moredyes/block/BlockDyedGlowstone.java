package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/** Dyed glowstone: full light, and drops 2-4 glowstone dust like vanilla. Silk touch gives the block itself. */
public class BlockDyedGlowstone extends BlockDyed {

    public BlockDyedGlowstone(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
        this.setLightValue(1.0F);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        int count = MathHelper.clamp_int(2 + world.rand.nextInt(3) + world.rand.nextInt(fortune + 1), 1, 4);
        drops.add(new ItemStack(Item.lightStoneDust, count));
        return drops;
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            BlockDyedStone.Silk.harvest(this, world, player, x, y, z);
        } else {
            super.harvestBlock(world, player, x, y, z, meta);
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
