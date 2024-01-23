package com.github.alexthe666.citadel.server.entity;

import net.minecraft.nbt.CompoundNBT;

public interface ICitadelDataEntity {
   CompoundNBT getCitadelEntityData();

   void setCitadelEntityData(CompoundNBT var1);
}
