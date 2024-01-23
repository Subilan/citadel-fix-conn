package com.github.alexthe666.citadel.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;

public abstract class CitadelPatreonRenderer {
   private static final Map<String, CitadelPatreonRenderer> PATREON_RENDERER_MAP = new LinkedHashMap<>();

   public static CitadelPatreonRenderer get(String identifier) {
      return PATREON_RENDERER_MAP.get(identifier);
   }

   public static void register(String identifier, CitadelPatreonRenderer renderer) {
      PATREON_RENDERER_MAP.put(identifier, renderer);
   }

   public abstract void render(MatrixStack var1, IRenderTypeBuffer var2, int var3, float var4, LivingEntity var5, float var6, float var7, float var8);

   public static String getIdOfNext(String identifier) {
      Object[] ids = PATREON_RENDERER_MAP.keySet().toArray();
      if (identifier.equals("none") && ids.length > 0) {
         return (String)ids[0];
      } else {
         for(int i = 0; i < ids.length - 1; ++i) {
            if (ids[i].equals(identifier)) {
               return (String)ids[i + 1];
            }
         }

         return "none";
      }
   }

   public static String getDefault() {
      return "citadel";
   }
}
