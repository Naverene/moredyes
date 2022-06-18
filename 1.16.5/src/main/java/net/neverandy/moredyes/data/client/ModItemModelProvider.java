package net.neverandy.moredyes.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

public class ModItemModelProvider extends ItemModelProvider
{

    public static String[][] nameArray = new String[Reference.BLOCKTYPES.length][ColorStrings.ALL.length];

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Reference.MOD_ID, existingFileHelper);
        createNameArray();
    }

    @Override
    protected void registerModels()
    {
        MoreDyes.LOGGER.debug("registerModels");
        for (int i = 0; i < nameArray.length; i++)
        {
            String[] blockType = nameArray[i];
            MoreDyes.LOGGER.info(Reference.BLOCKTYPES[i] + " for loop");
            for (String name : blockType)
            {
                withExistingParent(name, modLoc("block/" + name));
            }
        }
    }

    public void createNameArray()
    {
        for (int x = 0; x < nameArray.length; x++)
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                nameArray[x][i] = Reference.BLOCKTYPES[x] + "_" + ColorStrings.ALL[i];
            }
        }
    }
}