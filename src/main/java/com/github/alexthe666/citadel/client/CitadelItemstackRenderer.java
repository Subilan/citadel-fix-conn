package com.github.alexthe666.citadel.client;

import com.github.alexthe666.citadel.Citadel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;

public class CitadelItemstackRenderer extends ItemStackTileEntityRenderer {
   public void func_239207_a_(
      ItemStack stack, TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay
   ) {
      float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
      float ticksExisted = (float)Util.milliTime() / 50.0F + partialTicks;
      if (stack.getItem() == Citadel.FANCY_ITEM) {
         Random random = new Random();
         boolean animateAnyways = false;
         ItemStack toRender = null;
         if (stack.getTag() != null && stack.getTag().contains("DisplayItem")) {
            String id = stack.getTag().getString("DisplayItem");
            toRender = new ItemStack((IItemProvider)Registry.ITEM.getOrDefault(new ResourceLocation(id)));
            if (stack.getTag().contains("DisplayItemNBT")) {
               try {
                  toRender.setTag(stack.getTag().getCompound("DisplayItemNBT"));
               } catch (Exception var16) {
                  toRender = new ItemStack(Items.BARRIER);
               }
            }
         }

         if (toRender == null) {
            animateAnyways = true;
            toRender = new ItemStack(Items.BARRIER);
         }

         matrixStack.push();
         matrixStack.translate(0.5, 0.5, 0.5);
         if (stack.getTag() != null && stack.getTag().contains("DisplayShake") && stack.getTag().getBoolean("DisplayShake")) {
            matrixStack.translate(
               (double)((random.nextFloat() - 0.5F) * 0.1F), (double)((random.nextFloat() - 0.5F) * 0.1F), (double)((random.nextFloat() - 0.5F) * 0.1F)
            );
         }

         if (animateAnyways || stack.getTag() != null && stack.getTag().contains("DisplayBob") && stack.getTag().getBoolean("DisplayBob")) {
            matrixStack.translate(0.0, (double)(0.05F + 0.1F * MathHelper.sin(0.3F * ticksExisted)), 0.0);
         }

         if (stack.getTag() != null && stack.getTag().contains("DisplaySpin") && stack.getTag().getBoolean("DisplaySpin")) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(6.0F * ticksExisted));
         }

         if (animateAnyways || stack.getTag() != null && stack.getTag().contains("DisplayZoom") && stack.getTag().getBoolean("DisplayZoom")) {
            float scale = (float)(1.0 + 0.15F * (Math.sin((double)(ticksExisted * 0.3F)) + 1.0));
            matrixStack.scale(scale, scale, scale);
         }

         if (stack.getTag() != null && stack.getTag().contains("DisplayScale") && stack.getTag().getFloat("DisplayScale") != 1.0F) {
            float scale = stack.getTag().getFloat("DisplayScale");
            matrixStack.scale(scale, scale, scale);
         }

         Minecraft.getInstance().getItemRenderer().renderItem(toRender, transformType, combinedLight, combinedOverlay, matrixStack, buffer);
         matrixStack.pop();
      }

      if (stack.getItem() == Citadel.EFFECT_ITEM) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.enableAlphaTest();
         RenderSystem.enableDepthTest();
         Effect effect;
         if (stack.getTag() != null && stack.getTag().contains("DisplayEffect")) {
            String id = stack.getTag().getString("DisplayEffect");
            effect = (Effect)Registry.EFFECTS.getOrDefault(new ResourceLocation(id));
         } else {
            int size = Registry.EFFECTS.keySet().size();
            int time = (int)(Util.milliTime() / 500L);
            effect = (Effect)Registry.EFFECTS.getByValue(time % size);
            if (effect == null) {
               effect = Effects.SPEED;
            }
         }

         if (effect == null) {
            effect = Effects.SPEED;
         }

         PotionSpriteUploader potionspriteuploader = Minecraft.getInstance().getPotionSpriteUploader();
         matrixStack.push();
         matrixStack.translate(0.0, 0.0, 0.5);
         TextureAtlasSprite sprite = potionspriteuploader.getSprite(effect);
         Minecraft.getInstance().getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
         Matrix4f mx = matrixStack.getLast().getMatrix();
         int br = 255;
         bufferbuilder.pos(mx, 1.0F, 1.0F, 0.0F).tex(sprite.getMaxU(), sprite.getMinV()).color(br, br, br, 255).lightmap(combinedLight).endVertex();
         bufferbuilder.pos(mx, 0.0F, 1.0F, 0.0F).tex(sprite.getMinU(), sprite.getMinV()).color(br, br, br, 255).lightmap(combinedLight).endVertex();
         bufferbuilder.pos(mx, 0.0F, 0.0F, 0.0F).tex(sprite.getMinU(), sprite.getMaxV()).color(br, br, br, 255).lightmap(combinedLight).endVertex();
         bufferbuilder.pos(mx, 1.0F, 0.0F, 0.0F).tex(sprite.getMaxU(), sprite.getMaxV()).color(br, br, br, 255).lightmap(combinedLight).endVertex();
         tessellator.draw();
         matrixStack.pop();
      }
   }
}
