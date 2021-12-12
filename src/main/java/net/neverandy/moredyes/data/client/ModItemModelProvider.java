package net.neverandy.moredyes.data.client;

import com.google.gson.JsonElement;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {

    public static final Logger LOGGER = LogManager.getLogger();

    public static String[][] nameArray = new String[Reference.BLOCKTYPES.length][ColorStrings.ALL.length];

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MOD_ID, existingFileHelper);
        createNameArray();
    }

    @Override
    protected void registerModels() {
        LOGGER.info("registerModels");
//        for(String[] blockType : nameArray) {
        for (int i = 0; i< nameArray.length; i++){
            String[] blockType = nameArray[i];
            LOGGER.info(Reference.BLOCKTYPES[i] + " for loop");
            for (String name : blockType) {
                withExistingParent(name, modLoc("block/" + name));
            }
        }
    }

    public void createNameArray(){
        for(int x = 0; x < nameArray.length; x++){
            for (int i = 0; i < ColorStrings.ALL.length; i++){
                nameArray[x][i] = Reference.BLOCKTYPES[x]+"_"+ColorStrings.ALL[i];
            }
        }
    }
}