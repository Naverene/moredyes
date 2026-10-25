package net.neverandy.moredyes.data.server;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;

/** The item side of ModBlockTagsProvider; these tags give the furnace burn times and vanilla recipe inputs. */
public class ModItemTagsProvider extends ItemTagsProvider
{
    /** Every shulker box, vanilla and dyed, for the recipes that dye one; 1.18.2 has no such item tag of its own. */
    public static final TagKey<Item> SHULKER_BOXES = ItemTags.create(new ResourceLocation(Reference.MOD_ID, "shulker_boxes"));

    public ModItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper)
    {
        super(gen, blockTags, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        copy(BlockTags.WOOL, ItemTags.WOOL);
        copy(BlockTags.CARPETS, ItemTags.CARPETS);
        copy(BlockTags.BEDS, ItemTags.BEDS);
        copy(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
        copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
        copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
        // The block tag has the wall banners too, which have no items of their own.
        for (Block banner : MDBlock.bannerArray)
        {
            tag(ItemTags.BANNERS).add(banner.asItem());
        }
        tag(SHULKER_BOXES).add(Items.SHULKER_BOX);
        for (DyeColor color : DyeColor.values())
        {
            tag(SHULKER_BOXES).add(ShulkerBoxBlock.getBlockByColor(color).asItem());
        }
        for (Block box : MDBlock.shulkerBoxArray)
        {
            tag(SHULKER_BOXES).add(box.asItem());
        }
    }
}
