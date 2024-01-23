package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityProperties;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PropertiesMessage {
   private String propertyID;
   private CompoundNBT compound;
   private int entityID;

   public PropertiesMessage(String propertyID, CompoundNBT compound, int entityID) {
      this.propertyID = propertyID;
      this.compound = compound;
      this.entityID = entityID;
   }

   public PropertiesMessage(EntityProperties<?> properties, Entity entity) {
      this.propertyID = properties.getID();
      CompoundNBT compound = new CompoundNBT();
      properties.saveTrackingSensitiveData(compound);
      this.compound = compound;
      this.entityID = entity.getEntityId();
   }

   public static void write(PropertiesMessage message, PacketBuffer packetBuffer) {
      PacketBufferUtils.writeUTF8String(packetBuffer, message.propertyID);
      PacketBufferUtils.writeTag(packetBuffer, message.compound);
      packetBuffer.writeInt(message.entityID);
   }

   public static PropertiesMessage read(PacketBuffer packetBuffer) {
      return new PropertiesMessage(PacketBufferUtils.readUTF8String(packetBuffer), PacketBufferUtils.readTag(packetBuffer), packetBuffer.readInt());
   }

   public static class Handler {
      public static void handle(PropertiesMessage message, Supplier<Context> context) {
         ((Context)context.get()).setPacketHandled(true);
         ((Context)context.get()).enqueueWork(() -> {
            if (((Context)context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
               Citadel.PROXY.handlePropertiesPacket(message.propertyID, message.compound, message.entityID);
            } else {
               Entity e = ((Context)context.get()).getSender().world.getEntityByID(message.entityID);
               if (e instanceof LivingEntity && message.propertyID.equals("CitadelPatreonConfig")) {
                  CitadelEntityData.setCitadelTag((LivingEntity)e, message.compound);
               }
            }
         });
      }
   }
}
