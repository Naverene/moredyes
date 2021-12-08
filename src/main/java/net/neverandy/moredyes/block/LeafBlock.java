package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LeafBlock extends LeavesBlock
{
    private final String color;
    private final int index;
    public LeafBlock(String blockColor, BlockInfo info, int i)
    {
        super(Properties.of(info.blockMaterial).sound(info.sound).harvestLevel(info.harvestLevel));
        this.index=i;
        this.color = blockColor;
        setRegistryName(info.blockName+"_"+this.color);

        //initModel();
        //this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 7).setValue(PERSISTENT, Boolean.FALSE));

    }
    @OnlyIn(Dist.CLIENT)
    public void initModel()
    {
        ModelLoader.addSpecialModel(new ResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
    }
    protected BlockState createBlockState()
    {
        //return new BlockState(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
        return null;
    }
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public BlockState getStateFromMeta(int meta)
    {
        return this.getBlock().defaultBlockState();
        //return this.getBlock().defaultBlockState().hasProperty(this.stateDefinition.any().getValue(PERSISTENT).booleanValue(), Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(BlockState state)
    {
        int i = 0;
        if(state.getValue(PERSISTENT))
        {
            i |= 4;
        }
        if(state.getValue(PERSISTENT)) //? CHECK_DECAY
        {
            i |= 8;
        }
        return i;
    }
    public List<ItemStack> onSheared(ItemStack item, World world, BlockPos pos, int fortune)
    {
        return java.util.Arrays.asList(new ItemStack(this, 1));
    }
    @Nullable
    public Item getItemDropped(BlockState state, Random rand, int fortune)
    {
        //TODO: FIX ME
        return this.asItem();
        //return this.asItem(MDBlock.sapling[this.index]);
    }

}