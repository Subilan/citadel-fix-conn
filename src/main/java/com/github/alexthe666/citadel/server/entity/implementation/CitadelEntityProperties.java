package com.github.alexthe666.citadel.server.entity.implementation;

import com.github.alexthe666.citadel.server.entity.datatracker.EntityProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class CitadelEntityProperties extends EntityProperties {
   public int testInteger = 0;

   @Override
   public void init() {
   }

   @Override
   public int getTrackingTime() {
      return 20;
   }

   @Override
   public void saveNBTData(CompoundNBT compound) {
      compound.putInt("TestInteger", this.testInteger);
   }

   @Override
   public void loadNBTData(CompoundNBT compound) {
      this.testInteger = compound.getInt("TestInteger");
   }

   @Override
   public String getID() {
      return "citadel:test_properties";
   }

   @Override
   public Class getEntityClass() {
      return LivingEntity.class;
   }
}
