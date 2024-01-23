package com.github.alexthe666.citadel.client.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@OnlyIn(Dist.CLIENT)
@HasResult
public class EventGetOutlineColor extends Event {
   private Entity entityIn;
   private int color;

   public EventGetOutlineColor(Entity entityIn, int color) {
      this.entityIn = entityIn;
      this.color = color;
   }

   public Entity getEntityIn() {
      return this.entityIn;
   }

   public void setEntityIn(Entity entityIn) {
      this.entityIn = entityIn;
   }

   public int getColor() {
      return this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }
}
