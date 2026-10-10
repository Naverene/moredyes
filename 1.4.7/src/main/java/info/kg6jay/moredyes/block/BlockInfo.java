package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

/** What a kind of dyed block is made of: its name, material, hardness, sound and the tool that mines it. */
public class BlockInfo {

    /** Short name, used for the block ID setting in the config and the block's name ("stonebrickCarved"). */
    public final String name;
    /** The name players see after the color's hex code ("Carved Stone Brick"). */
    public final String displayName;
    public final Material material;
    public final float hardness;
    public final StepSound sound;
    /** Tool class that mines this block fastest, or null for none (like vanilla leaves, wool and glass). */
    public final String tool;
    public final int harvestLevel;
    public final float resistance;
    public CreativeTabs tab;

    public BlockInfo(String name, String displayName, Material material, float hardness, StepSound sound,
        String tool, int harvestLevel, float resistance) {
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.hardness = hardness;
        this.sound = sound;
        this.tool = tool;
        this.harvestLevel = harvestLevel;
        this.resistance = resistance;
    }

    public BlockInfo(String name, String displayName, Material material, float hardness, StepSound sound,
        String tool, int harvestLevel) {
        this(name, displayName, material, hardness, sound, tool, harvestLevel, 1.0F);
    }

    public BlockInfo tab(CreativeTabs tab) {
        this.tab = tab;
        return this;
    }

    /** Applies everything but the material, which the block's constructor takes. */
    public void apply(Block block) {
        block.setHardness(this.hardness);
        block.setResistance(this.resistance);
        block.setStepSound(this.sound);
        block.setBlockName("moredyes." + this.name);
        if (this.tab != null) {
            block.setCreativeTab(this.tab);
        }
        if (this.tool != null && this.tool.length() > 0) {
            MinecraftForge.setBlockHarvestLevel(block, this.tool, this.harvestLevel);
        }
    }
}
