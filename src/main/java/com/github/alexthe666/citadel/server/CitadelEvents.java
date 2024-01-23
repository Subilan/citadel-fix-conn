package com.github.alexthe666.citadel.server;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.config.ServerConfig;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityDataCapabilityImplementation;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityDataHandler;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityProperties;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityPropertiesHandler;
import com.github.alexthe666.citadel.server.entity.datatracker.IEntityData;
import com.github.alexthe666.citadel.server.entity.datatracker.PropertiesTracker;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.entity.player.PlayerEvent.StopTracking;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitadelEvents {
   private int updateTimer;

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
      event.addCapability(new ResourceLocation("citadel", "extended_entity_data_citadel"), new ICapabilitySerializable() {
         private final LazyOptional<IEntityData> holder = LazyOptional.of(() -> new EntityDataCapabilityImplementation());

         public INBT serializeNBT() {
            Capability<IEntityData> capability = Citadel.ENTITY_DATA_CAPABILITY;
            IEntityData instance = (IEntityData)capability.getDefaultInstance();
            instance.init((Entity)event.getObject(), ((Entity)event.getObject()).getEntityWorld(), false);
            return capability.getStorage().writeNBT(capability, instance, null);
         }

         public void deserializeNBT(INBT nbt) {
            Capability<IEntityData> capability = Citadel.ENTITY_DATA_CAPABILITY;
            IEntityData instance = (IEntityData)capability.getDefaultInstance();
            instance.init((Entity)event.getObject(), ((Entity)event.getObject()).getEntityWorld(), true);
            capability.getStorage().readNBT(capability, instance, null, nbt);
         }

         public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
            return capability == Citadel.ENTITY_DATA_CAPABILITY ? Citadel.ENTITY_DATA_CAPABILITY.orEmpty(capability, this.holder).cast() : LazyOptional.empty();
         }
      });
   }

   @SubscribeEvent
   public void onEntityDestroyed(EntityLeaveWorldEvent event) {
      if (!(event.getEntity() instanceof PlayerEntity)) {
         EntityDataHandler.INSTANCE.stopTracking(event.getEntity());
      }
   }

   @SubscribeEvent
   public void onEntityConstructing(EntityConstructing event) {
      if (ServerConfig.citadelEntityTrack) {
         boolean cached = EntityPropertiesHandler.INSTANCE.hasEntityInCache(event.getEntity().getClass());
         List<String> entityPropertiesIDCache = !cached ? new ArrayList<>() : null;
         EntityPropertiesHandler.INSTANCE
            .getRegisteredProperties()
            .filter(propEntry -> propEntry.getKey().isAssignableFrom(event.getEntity().getClass()))
            .forEach(propEntry -> {
               for(Class<? extends EntityProperties> propClass : propEntry.getValue()) {
                  try {
                     Constructor<? extends EntityProperties> constructor = propClass.getConstructor();
                     EntityProperties prop = constructor.newInstance();
                     String propID = prop.getID();
                     EntityDataHandler.INSTANCE.registerExtendedEntityData(event.getEntity(), prop);
                     if (!cached) {
                        entityPropertiesIDCache.add(propID);
                     }
                  } catch (Exception var9) {
                     var9.printStackTrace();
                  }
               }
            });
         if (!cached) {
            EntityPropertiesHandler.INSTANCE.addEntityToCache(event.getEntity().getClass(), entityPropertiesIDCache);
         }
      }
   }

   @SubscribeEvent
   public void onEntityUpdate(LivingUpdateEvent event) {
      if (!event.getEntity().world.isRemote && event.getEntity() instanceof ServerPlayerEntity) {
         ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
         List<PropertiesTracker<?>> trackers = EntityPropertiesHandler.INSTANCE.getEntityTrackers(player);
         if (trackers != null && trackers.size() > 0) {
            boolean hasPlayer = false;

            for(PropertiesTracker tracker : trackers) {
               if (hasPlayer = tracker.getEntity() == player) {
                  break;
               }
            }

            if (!hasPlayer) {
               EntityPropertiesHandler.INSTANCE.addTracker(player, player);
            }

            for(PropertiesTracker<?> tracker : trackers) {
               tracker.updateTracker();
               if (tracker.isTrackerReady()) {
                  tracker.onSync();
                  PropertiesMessage message = new PropertiesMessage(tracker.getProperties(), tracker.getEntity());
                  Citadel.sendNonLocal(message, player);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onEntityUpdateDebug(LivingUpdateEvent event) {
   }

   @SubscribeEvent
   public void onJoinWorld(EntityJoinWorldEvent event) {
      if (!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity) {
         ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
         EntityPropertiesHandler.INSTANCE.addTracker(player, player);
      }
   }

   @SubscribeEvent
   public void onEntityStartTracking(StartTracking event) {
      if (event.getPlayer() instanceof ServerPlayerEntity) {
         EntityPropertiesHandler.INSTANCE.addTracker((ServerPlayerEntity)event.getPlayer(), event.getTarget());
      }
   }

   @SubscribeEvent
   public void onEntityStopTracking(StopTracking event) {
      if (event.getPlayer() instanceof ServerPlayerEntity) {
         EntityPropertiesHandler.INSTANCE.removeTracker((ServerPlayerEntity)event.getPlayer(), event.getTarget());
      }
   }

   @SubscribeEvent
   public void onServerTickEvent(ServerTickEvent event) {
      if (event.phase == Phase.END && ServerConfig.citadelEntityTrack) {
         ++this.updateTimer;
         if (this.updateTimer > 20) {
            this.updateTimer = 0;
            Iterator<Entry<ServerPlayerEntity, List<PropertiesTracker<?>>>> iterator = EntityPropertiesHandler.INSTANCE.getTrackerIterator();

            while(iterator.hasNext()) {
               Entry<ServerPlayerEntity, List<PropertiesTracker<?>>> trackerEntry = iterator.next();
               ServerPlayerEntity player = (ServerPlayerEntity)trackerEntry.getKey();
               ServerWorld playerWorld = (ServerWorld)player.world;
               if (player != null && !player.removed && playerWorld != null) {
                  Iterator<PropertiesTracker<?>> it = trackerEntry.getValue().iterator();

                  while(it.hasNext()) {
                     PropertiesTracker tracker = it.next();
                     Entity entity = tracker.getEntity();
                     ServerWorld entityWorld = (ServerWorld)player.world;
                     if (entity == null || entity.removed || entityWorld == null) {
                        it.remove();
                        tracker.removeTracker();
                     }
                  }
               } else {
                  iterator.remove();
                  trackerEntry.getValue().forEach(PropertiesTracker::removeTracker);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public void onLoadBiome(BiomeLoadingEvent event) {
      float probability = (float)ServerConfig.chunkGenSpawnModifierVal * event.getSpawns().getProbability();
      event.getSpawns().withCreatureSpawnProbability(probability);
   }

   @SubscribeEvent
   public void onPlayerClone(Clone event) {
      if (event.getOriginal() != null && CitadelEntityData.getCitadelTag(event.getOriginal()) != null) {
         CitadelEntityData.setCitadelTag(event.getEntityLiving(), CitadelEntityData.getCitadelTag(event.getOriginal()));
      }
   }
}
