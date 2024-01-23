package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class AnimationMessage {
   private int entityID;
   private int index;

   public AnimationMessage(int entityID, int index) {
      this.entityID = entityID;
      this.index = index;
   }

   public static AnimationMessage read(PacketBuffer buf) {
      return new AnimationMessage(buf.readInt(), buf.readInt());
   }

   public static void write(AnimationMessage message, PacketBuffer buf) {
      buf.writeInt(message.entityID);
      buf.writeInt(message.index);
   }

   public static class Handler {
      public static void handle(AnimationMessage message, Supplier<Context> context) {
         Citadel.PROXY.handleAnimationPacket(message.entityID, message.index);
         ((Context)context.get()).setPacketHandled(true);
      }
   }
}
