package net.neverandy.moredyes.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;

public class ModLangProvider extends LanguageProvider
{
    public ModLangProvider(DataGenerator gen, String modid, String locale)
    {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            add(MDBlock.stoneArray[i], ColorStrings.ALL[i].toUpperCase() + " Stone");
            add(MDBlock.stonebrickArray[i], ColorStrings.ALL[i].toUpperCase() + " Stonebrick");
            add(MDBlock.brickArray[i], ColorStrings.ALL[i].toUpperCase() + " Bricks");
            add(MDBlock.coalArray[i], ColorStrings.ALL[i].toUpperCase() + " Block of Coal");
            add(MDBlock.clayArray[i], ColorStrings.ALL[i].toUpperCase() + " Clay");
            add(MDBlock.logArray[i], ColorStrings.ALL[i].toUpperCase() + " Oak Log");
            add(MDBlock.stonebrickCarvedArray[i], ColorStrings.ALL[i].toUpperCase() + " Stonebrick Carved");
            add(MDBlock.stonebrickCrackedArray[i], ColorStrings.ALL[i].toUpperCase() + " Stonebrick Cracked");
            add(MDBlock.lapisArray[i], ColorStrings.ALL[i].toUpperCase() + " Lapis Lazuli Block");
            add(MDBlock.obsidianArray[i], ColorStrings.ALL[i].toUpperCase() + " Obsdidian");
            add(MDBlock.glowstoneArray[i], ColorStrings.ALL[i].toUpperCase() + " Glowstone");
            add(MDBlock.cobbleArray[i], ColorStrings.ALL[i].toUpperCase() + " Cobble");
            add(MDBlock.glassArray[i], ColorStrings.ALL[i].toUpperCase() + " Glass");
            add(MDBlock.glassFoggyArray[i], ColorStrings.ALL[i].toUpperCase() + " Stained Glass");
            add(MDBlock.workbenchArray[i], ColorStrings.ALL[i].toUpperCase() + " Crafting Table");
            add(MDBlock.sandArray[i], ColorStrings.ALL[i].toUpperCase() + " Sand");
            add(MDBlock.woolArray[i], ColorStrings.ALL[i].toUpperCase() + " Wool");
            add(MDBlock.redstoneArray[i], ColorStrings.ALL[i].toUpperCase() + " Block of Redstone");
            add(MDBlock.tulipArray[i], ColorStrings.ALL[i].toUpperCase() + " Tulip");
            add(MDBlock.quartzArray[i], ColorStrings.ALL[i].toUpperCase() + " Block of Quartz");
            add(MDBlock.plankArray[i], ColorStrings.ALL[i].toUpperCase() + " Oak Plank");
            add(MDBlock.soulsandArray[i], ColorStrings.ALL[i].toUpperCase() + " Soulsand");
            add(MDBlock.sandstoneArray[i], ColorStrings.ALL[i].toUpperCase() + " Sandstone");
            //add(MDBlock.sandstoneCarvedArray[i], ColorStrings.ALL[i].toUpperCase() + " Carved Sandstone");
            //add(MDBlock.sandstoneSmoothArray[i], ColorStrings.ALL[i].toUpperCase() + " Smooth Sandstone");
            add(MDBlock.andesiteArray[i], ColorStrings.ALL[i].toUpperCase() + " Andesite");
            add(MDBlock.dioriteArray[i], ColorStrings.ALL[i].toUpperCase() + " Diorite");
            add(MDItem.dye[i],ColorStrings.ALL[i].toUpperCase() + " Dye");
            add(MDBlock.concretePowderArray[i],ColorStrings.ALL[i].toUpperCase() + " Concrete Powder");
            add(MDBlock.ladderBlockArray[i], ColorStrings.ALL[i].toUpperCase() + " Ladder");

        }

    }
}
