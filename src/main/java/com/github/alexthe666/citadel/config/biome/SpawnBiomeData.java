package com.github.alexthe666.citadel.config.biome;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class SpawnBiomeData {
   private List<List<SpawnBiomeData.SpawnBiomeEntry>> biomes = new ArrayList<>();

   public SpawnBiomeData() {
   }

   private SpawnBiomeData(SpawnBiomeData.SpawnBiomeEntry[][] biomesRead) {
      this.biomes = new ArrayList<>();

      for(SpawnBiomeData.SpawnBiomeEntry[] innerArray : biomesRead) {
         this.biomes.add(Arrays.asList(innerArray));
      }
   }

   public SpawnBiomeData addBiomeEntry(BiomeEntryType type, boolean negate, String value, int pool) {
      if (this.biomes.isEmpty() || this.biomes.size() < pool + 1) {
         this.biomes.add(new ArrayList<>());
      }

      this.biomes.get(pool).add(new SpawnBiomeData.SpawnBiomeEntry(type, negate, value));
      return this;
   }

   public boolean matches(Biome biomeIn) {
      for(List<SpawnBiomeData.SpawnBiomeEntry> all : this.biomes) {
         boolean overall = true;

         for(SpawnBiomeData.SpawnBiomeEntry cond : all) {
            if (!cond.matches(biomeIn)) {
               overall = false;
            }
         }

         if (overall) {
            return true;
         }
      }

      return false;
   }

   public static class Deserializer implements JsonDeserializer<SpawnBiomeData>, JsonSerializer<SpawnBiomeData> {
      public SpawnBiomeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         JsonObject jsonobject = json.getAsJsonObject();
         SpawnBiomeData.SpawnBiomeEntry[][] biomesRead = (SpawnBiomeData.SpawnBiomeEntry[][])JSONUtils.deserializeClass(
            jsonobject, "biomes", new SpawnBiomeData.SpawnBiomeEntry[0][0], context, SpawnBiomeData.SpawnBiomeEntry[][].class
         );
         return new SpawnBiomeData(biomesRead);
      }

      public JsonElement serialize(SpawnBiomeData src, Type typeOfSrc, JsonSerializationContext context) {
         JsonObject jsonobject = new JsonObject();
         jsonobject.add("biomes", context.serialize(src.biomes));
         return jsonobject;
      }
   }

   private class SpawnBiomeEntry {
      BiomeEntryType type;
      boolean negate;
      String value;

      public SpawnBiomeEntry(BiomeEntryType type, boolean remove, String value) {
         this.type = type;
         this.negate = remove;
         this.value = value;
      }

      public boolean matches(Biome biomeIn) {
         if (biomeIn == null || biomeIn.getRegistryName() == null) {
            return false;
         } else if (this.type == BiomeEntryType.BIOME_DICT) {
            RegistryKey<Biome> biomeKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biomeIn.getRegistryName());
            List<? extends String> biomeTypes = BiomeDictionary.getTypes(biomeKey)
               .stream()
               .map(t -> t.toString().toLowerCase(Locale.ROOT))
               .collect(Collectors.toList());
            if (biomeTypes.contains(this.value)) {
               return !this.negate;
            } else {
               return this.negate;
            }
         } else if (this.type == BiomeEntryType.BIOME_CATEGORY) {
            if (biomeIn.getCategory().getName().toLowerCase(Locale.ROOT).equals(this.value)) {
               return !this.negate;
            } else {
               return this.negate;
            }
         } else if (biomeIn.getRegistryName().toString().equals(this.value)) {
            return !this.negate;
         } else {
            return this.negate;
         }
      }
   }
}
