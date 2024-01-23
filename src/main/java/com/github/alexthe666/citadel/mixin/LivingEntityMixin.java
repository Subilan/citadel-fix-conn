package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.entity.ICitadelDataEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity implements ICitadelDataEntity {
   private static final DataParameter<CompoundNBT> CITADEL_DATA = EntityDataManager.createKey(LivingEntity.class, DataSerializers.COMPOUND_NBT);

   protected LivingEntityMixin(EntityType<? extends Entity> entityType, World world) {
      super(entityType, world);
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"Lnet/minecraft/entity/LivingEntity;registerData()V"}
   )
   private void citadel_registerData(CallbackInfo ci) {
      this.dataManager.register(CITADEL_DATA, new CompoundNBT());
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"Lnet/minecraft/entity/LivingEntity;writeAdditional(Lnet/minecraft/nbt/CompoundNBT;)V"}
   )
   private void citadel_writeAdditional(CompoundNBT compoundNBT, CallbackInfo ci) {
      CompoundNBT citadelDat = this.getCitadelEntityData();
      if (citadelDat != null) {
         compoundNBT.put("CitadelData", citadelDat);
      }
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"Lnet/minecraft/entity/LivingEntity;readAdditional(Lnet/minecraft/nbt/CompoundNBT;)V"}
   )
   private void citadel_readAdditional(CompoundNBT compoundNBT, CallbackInfo ci) {
      if (compoundNBT.contains("CitadelData")) {
         this.setCitadelEntityData(compoundNBT.getCompound("CitadelData"));
      }
   }

   @Override
   public CompoundNBT getCitadelEntityData() {
      return (CompoundNBT)this.dataManager.get(CITADEL_DATA);
   }

   @Override
   public void setCitadelEntityData(CompoundNBT nbt) {
      this.dataManager.set(CITADEL_DATA, nbt);
   }
}
