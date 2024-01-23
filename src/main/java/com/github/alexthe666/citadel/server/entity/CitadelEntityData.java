package com.github.alexthe666.citadel.server.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class CitadelEntityData {
   public static CompoundNBT getOrCreateCitadelTag(LivingEntity entity) {
      CompoundNBT tag = getCitadelTag(entity);
      return tag == null ? new CompoundNBT() : tag;
   }

   public static CompoundNBT getCitadelTag(LivingEntity entity) {
      return entity instanceof ICitadelDataEntity ? ((ICitadelDataEntity)entity).getCitadelEntityData() : new CompoundNBT();
   }

   public static void setCitadelTag(LivingEntity entity, CompoundNBT tag) {
      if (entity instanceof ICitadelDataEntity) {
         ((ICitadelDataEntity)entity).setCitadelEntityData(tag);
      }
   }
}
