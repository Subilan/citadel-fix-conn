package com.github.alexthe666.citadel.server.entity.datatracker;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

@Deprecated
public abstract class EntityProperties<T extends Entity> implements IEntityData<T> {
   private World world;
   private WeakReference<T> entity;
   private Set<PropertiesTracker<?>> trackers = Collections.newSetFromMap(new WeakHashMap<>());

   @Override
   public final void init(T entity, World world) {
      this.entity = new WeakReference(entity);
      this.world = world;
      this.init();
   }

   public Set<PropertiesTracker<?>> getTrackers() {
      return this.trackers;
   }

   public void sync() {
      this.trackers.forEach(PropertiesTracker::setReady);
   }

   public final World getWorld() {
      return this.world;
   }

   @Nullable
   public final T getEntity() {
      return (T)(this.entity == null ? null : this.entity.get());
   }

   public abstract void init();

   @Override
   public abstract String getID();

   public abstract Class<T> getEntityClass();

   public int getTrackingTime() {
      return -1;
   }

   public int getTrackingUpdateTime() {
      return 0;
   }

   public void saveTrackingSensitiveData(CompoundNBT compound) {
      this.saveNBTData(compound);
   }

   public void loadTrackingSensitiveData(CompoundNBT compound) {
      this.loadNBTData(compound);
   }

   public void onSync() {
   }

   public PropertiesTracker<T> createTracker(T entity) {
      PropertiesTracker<T> tracker = new PropertiesTracker<>(entity, this);
      this.trackers.add(tracker);
      return tracker;
   }
}
