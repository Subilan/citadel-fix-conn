package com.github.alexthe666.citadel.server.entity.datatracker;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class EntityDataCapabilityStorage implements IStorage<IEntityData> {
   @Nullable
   public INBT writeNBT(Capability<IEntityData> capability, IEntityData instance, Direction side) {
      CompoundNBT compound = new CompoundNBT();
      instance.saveNBTData(compound);
      return compound;
   }

   public void readNBT(Capability<IEntityData> capability, IEntityData instance, Direction side, INBT nbt) {
      instance.loadNBTData((CompoundNBT)nbt);
   }
}
