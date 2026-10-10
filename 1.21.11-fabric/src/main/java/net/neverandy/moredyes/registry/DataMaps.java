package net.neverandy.moredyes.registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import net.neverandy.moredyes.MoreDyes;

/**
 * Which items burn in a furnace and which can be composted. The NeoForge build keeps these as data maps
 * ({@code data/neoforge/data_maps/item/furnace_fuels.json} and {@code compostables.json}, written by its
 * tools/generate_resources.py); this build copies them into the jar under {@code moredyes/fabric/} and hands them to
 * Fabric's fuel and composting registries.
 */
public final class DataMaps {

    private DataMaps() {}

    /** Call after the items are registered. */
    public static void register() {
        Map<Item, Integer> fuels = new LinkedHashMap<>();
        values("furnace_fuels.json").forEach((item, value) -> fuels.put(item, value.get("burn_time").getAsInt()));
        FuelRegistryEvents.BUILD.register((builder, context) -> fuels.forEach(builder::add));
        values("compostables.json").forEach((item, value) ->
            CompostingChanceRegistry.INSTANCE.add(item, value.get("chance").getAsFloat()));
    }

    /** The entries of a data map, by item. */
    private static Map<Item, JsonObject> values(String name) {
        Map<Item, JsonObject> values = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : read(name).getAsJsonObject("values").entrySet()) {
            Item item = BuiltInRegistries.ITEM.getOptional(Identifier.parse(entry.getKey()))
                .orElseThrow(() -> new IllegalStateException("Unknown item " + entry.getKey() + " in " + name));
            values.put(item, entry.getValue().getAsJsonObject());
        }
        return values;
    }

    /** One of the NeoForge data files copied into the jar under moredyes/fabric. */
    public static JsonObject read(String name) {
        String path = "/" + MoreDyes.MOD_ID + "/fabric/" + name;
        try (InputStream in = DataMaps.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing " + path + " in the More Dyes jar");
            }
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + path, e);
        }
    }
}
