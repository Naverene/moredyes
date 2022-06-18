package net.neverandy.moredyes.data.server;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider
{
    public ModLootTableProvider(DataGenerator dataGeneratorIn)
    {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return null;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
        map.forEach((p_218436_2_, p_218436_3_) ->
        {
            LootTableManager.validateLootTable(validationtracker, p_218436_2_, p_218436_3_);
        });
    }
    public static class ModBlockLootTables extends BlockLootTables
    {
        @Override
        protected void addTables()
        {
            for (int i = 0; i < ColorStrings.ALL.length; i++)
            {
                int finalI = i;
                registerDropSelfLootTable(MDBlock.oakPlankArray[i]);
                registerDropSelfLootTable(MDBlock.cobbleArray[i]);
                registerDropSelfLootTable(MDBlock.pistonArray[i]);
                registerDropSelfLootTable(MDBlock.andesiteArray[i]);
                registerDropSelfLootTable(MDBlock.concretePowderArray[i]);
                registerDropSelfLootTable(MDBlock.dioriteArray[i]);
                registerDropSelfLootTable(MDBlock.coalArray[i]);
                registerDropSelfLootTable(MDBlock.oakLogArray[i]);
                registerDropSelfLootTable(MDBlock.brickArray[i]);
                registerDropSelfLootTable(MDBlock.stonebrickArray[i]);
                registerDropSelfLootTable(MDBlock.stonebrickCarvedArray[i]);
                registerDropSelfLootTable(MDBlock.stonebrickCrackedArray[i]);
                registerDropSelfLootTable(MDBlock.sandArray[i]);
                registerDropSelfLootTable(MDBlock.sandstoneSmoothArray[i]);
                registerDropSelfLootTable(MDBlock.sandstoneArray[i]);
                registerDropSelfLootTable(MDBlock.sandstoneCarvedArray[i]);
                registerDropSelfLootTable(MDBlock.clayArray[i]);
                registerDropSelfLootTable(MDBlock.hardenedClayArray[i]);

                //registerLootTable(MDBlock.stoneArray[i], dropping(MDBlock.cobbleArray[i]));
                registerLootTable(MDBlock.stoneArray[i], (stone) -> droppingWithSilkTouch(MDBlock.stoneArray[finalI], MDBlock.cobbleArray[finalI]));
            }

        }
    }


}