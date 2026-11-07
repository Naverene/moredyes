package info.kg6jay.moredyes.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.utility.BlockInfo;

/**
 * A dyed redstone lamp, like the colored lamps of RedPower and Project Red. A normal lamp lights up while it is
 * powered,
 * like vanilla's; an inverted one is lit until it is powered. Like vanilla's lamp, each is two blocks, one lit and one
 * dark, that swap when the power changes, keeping the color in the metadata. Only the one placed without power has an
 * item (the dark normal lamp and the lit inverted lamp), and both drop it. A lamp turns on at once and off four ticks
 * later, so a short pulse doesn't make it flicker.
 */
public class MDBlockLamp extends MDBlockColored {

    private final boolean inverted;
    private final boolean lit;
    /** The same lamp in the other state; set by {@link #pair}. */
    private MDBlockLamp other;

    public MDBlockLamp(String[] colors, BlockInfo info, String colorSet, boolean inverted, boolean lit) {
        super(colors, info, colorSet);
        this.inverted = inverted;
        this.lit = lit;
        if (lit) {
            this.setLightLevel(1.0F);
        }
    }

    /** Links the dark and lit block of one lamp. The one without an item shares the other's name. */
    public static void pair(MDBlockLamp dark, MDBlockLamp lit) {
        dark.other = lit;
        lit.other = dark;
        MDBlockLamp hidden = dark.inverted ? dark : lit;
        hidden.setCreativeTab(null);
        hidden.setBlockName(
            hidden.other.getUnlocalizedName()
                .substring("tile.".length()));
    }

    /** Whether this is the block of the lamp that has an item: the one placed without power. */
    public boolean hasItem() {
        return this.lit == this.inverted;
    }

    private MDBlockLamp itemBlock() {
        return this.hasItem() ? this : this.other;
    }

    private boolean shouldBeLit(World world, int x, int y, int z) {
        return world.isBlockIndirectlyGettingPowered(x, y, z) != this.inverted;
    }

    private void swap(World world, int x, int y, int z) {
        world.setBlock(x, y, z, this.other, world.getBlockMetadata(x, y, z), 2);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote && this.lit != this.shouldBeLit(world, x, y, z)) {
            this.swap(world, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (world.isRemote || this.lit == this.shouldBeLit(world, x, y, z)) {
            return;
        }
        if (this.lit) {
            world.scheduleBlockUpdate(x, y, z, this, 4);
        } else {
            this.swap(world, x, y, z);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote && this.lit && !this.shouldBeLit(world, x, y, z)) {
            this.swap(world, x, y, z);
        }
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(this.itemBlock());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(this.itemBlock());
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (this.hasItem()) {
            super.getSubBlocks(item, tab, list);
        }
    }

    /** A lit lamp is drawn at full brightness, so it looks like the light comes from it. */
    @Override
    public int getRenderType() {
        return this.lit ? RenderIds.litLamp : super.getRenderType();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String style = this.blockName.toLowerCase()
            .contains("solid") ? "lamp_solid" : "lamp";
        this.icon = TintedTextures.register(iconRegister, this.lit ? style + "_on" : style);
    }
}
