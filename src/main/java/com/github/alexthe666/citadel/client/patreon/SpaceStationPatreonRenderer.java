package com.github.alexthe666.citadel.client.patreon;

import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.CitadelPatreonRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SpaceStationPatreonRenderer extends CitadelPatreonRenderer {
   private ResourceLocation texture;

   public SpaceStationPatreonRenderer(ResourceLocation texture) {
      this.texture = texture;
   }

   @Override
   public void render(
      MatrixStack matrixStackIn,
      IRenderTypeBuffer buffer,
      int light,
      float partialTick,
      LivingEntity entity,
      float distanceIn,
      float rotateSpeed,
      float rotateHeight
   ) {
      IVertexBuilder textureBuilder = buffer.getBuffer(RenderType.getEntityCutoutNoCull(this.texture));
      float tick = (float)entity.ticksExisted + partialTick;
      float bob = (float)(Math.sin((double)(tick * 0.1F)) * 1.0 * 0.05F - 0.05F);
      float scale = 0.4F;
      float rotation = MathHelper.wrapDegrees(tick * rotateSpeed % 360.0F);
      matrixStackIn.push();
      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
      matrixStackIn.translate(0.0, (double)(entity.getHeight() + bob + (rotateHeight - 1.0F)), (double)(entity.getWidth() * distanceIn));
      matrixStackIn.push();
      matrixStackIn.rotate(Vector3f.XP.rotationDegrees(75.0F));
      matrixStackIn.scale(scale, scale, scale);
      matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation * 10.0F));
      ClientProxy.CITADEL_MODEL.resetToDefaultPose();
      ClientProxy.CITADEL_MODEL.render(matrixStackIn, textureBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      matrixStackIn.pop();
      matrixStackIn.pop();
   }
}
