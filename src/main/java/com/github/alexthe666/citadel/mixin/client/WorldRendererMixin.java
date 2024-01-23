package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldRenderer.class})
public class WorldRendererMixin {
   @Redirect(
      method = {"Lnet/minecraft/client/renderer/WorldRenderer;updateCameraAndRender(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/Entity;getTeamColor()I"
)
   )
   private int citadel_getTeamColor(Entity entity) {
      EventGetOutlineColor event = new EventGetOutlineColor(entity, entity.getTeamColor());
      MinecraftForge.EVENT_BUS.post(event);
      int color = entity.getTeamColor();
      if (event.getResult() == Result.ALLOW) {
         color = event.getColor();
      }

      return color;
   }
}
