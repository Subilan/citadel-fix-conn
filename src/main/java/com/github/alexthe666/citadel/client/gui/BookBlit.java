package com.github.alexthe666.citadel.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

public class BookBlit {
   private static int r = 255;
   private static int g = 255;
   private static int b = 255;
   private static int a = 255;

   public static void func_238467_a_(MatrixStack p_238467_0_, int p_238467_1_, int p_238467_2_, int p_238467_3_, int p_238467_4_, int p_238467_5_) {
      func_238460_a_(p_238467_0_.getLast().getMatrix(), p_238467_1_, p_238467_2_, p_238467_3_, p_238467_4_, p_238467_5_);
   }

   private static void func_238460_a_(Matrix4f p_238460_0_, int p_238460_1_, int p_238460_2_, int p_238460_3_, int p_238460_4_, int p_238460_5_) {
      if (p_238460_1_ < p_238460_3_) {
         int lvt_6_2_ = p_238460_1_;
         p_238460_1_ = p_238460_3_;
         p_238460_3_ = lvt_6_2_;
      }

      if (p_238460_2_ < p_238460_4_) {
         int lvt_6_2_ = p_238460_2_;
         p_238460_2_ = p_238460_4_;
         p_238460_4_ = lvt_6_2_;
      }

      float lvt_6_3_ = (float)(p_238460_5_ >> 24 & 0xFF) / 255.0F;
      float lvt_7_1_ = (float)(p_238460_5_ >> 16 & 0xFF) / 255.0F;
      float lvt_8_1_ = (float)(p_238460_5_ >> 8 & 0xFF) / 255.0F;
      float lvt_9_1_ = (float)(p_238460_5_ & 0xFF) / 255.0F;
      BufferBuilder lvt_10_1_ = Tessellator.getInstance().getBuffer();
      RenderSystem.enableBlend();
      RenderSystem.disableTexture();
      RenderSystem.defaultBlendFunc();
      lvt_10_1_.begin(7, DefaultVertexFormats.POSITION_COLOR);
      lvt_10_1_.pos(p_238460_0_, (float)p_238460_1_, (float)p_238460_4_, 0.0F).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
      lvt_10_1_.pos(p_238460_0_, (float)p_238460_3_, (float)p_238460_4_, 0.0F).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
      lvt_10_1_.pos(p_238460_0_, (float)p_238460_3_, (float)p_238460_2_, 0.0F).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
      lvt_10_1_.pos(p_238460_0_, (float)p_238460_1_, (float)p_238460_2_, 0.0F).color(lvt_7_1_, lvt_8_1_, lvt_9_1_, lvt_6_3_).endVertex();
      lvt_10_1_.finishDrawing();
      WorldVertexBufferUploader.draw(lvt_10_1_);
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
   }

   protected static void func_238462_a_(
      Matrix4f p_238462_0_,
      BufferBuilder p_238462_1_,
      int p_238462_2_,
      int p_238462_3_,
      int p_238462_4_,
      int p_238462_5_,
      int p_238462_6_,
      int p_238462_7_,
      int p_238462_8_
   ) {
      float lvt_9_1_ = (float)(p_238462_7_ >> 24 & 0xFF) / 255.0F;
      float lvt_10_1_ = (float)(p_238462_7_ >> 16 & 0xFF) / 255.0F;
      float lvt_11_1_ = (float)(p_238462_7_ >> 8 & 0xFF) / 255.0F;
      float lvt_12_1_ = (float)(p_238462_7_ & 0xFF) / 255.0F;
      float lvt_13_1_ = (float)(p_238462_8_ >> 24 & 0xFF) / 255.0F;
      float lvt_14_1_ = (float)(p_238462_8_ >> 16 & 0xFF) / 255.0F;
      float lvt_15_1_ = (float)(p_238462_8_ >> 8 & 0xFF) / 255.0F;
      float lvt_16_1_ = (float)(p_238462_8_ & 0xFF) / 255.0F;
      p_238462_1_.pos(p_238462_0_, (float)p_238462_4_, (float)p_238462_3_, (float)p_238462_6_).color(lvt_10_1_, lvt_11_1_, lvt_12_1_, lvt_9_1_).endVertex();
      p_238462_1_.pos(p_238462_0_, (float)p_238462_2_, (float)p_238462_3_, (float)p_238462_6_).color(lvt_10_1_, lvt_11_1_, lvt_12_1_, lvt_9_1_).endVertex();
      p_238462_1_.pos(p_238462_0_, (float)p_238462_2_, (float)p_238462_5_, (float)p_238462_6_).color(lvt_14_1_, lvt_15_1_, lvt_16_1_, lvt_13_1_).endVertex();
      p_238462_1_.pos(p_238462_0_, (float)p_238462_4_, (float)p_238462_5_, (float)p_238462_6_).color(lvt_14_1_, lvt_15_1_, lvt_16_1_, lvt_13_1_).endVertex();
   }

   public static void func_238464_a_(
      MatrixStack p_238464_0_,
      int p_238464_1_,
      int p_238464_2_,
      int p_238464_3_,
      float p_238464_4_,
      float p_238464_5_,
      int p_238464_6_,
      int p_238464_7_,
      int p_238464_8_,
      int p_238464_9_
   ) {
      func_238469_a_(
         p_238464_0_,
         p_238464_1_,
         p_238464_1_ + p_238464_6_,
         p_238464_2_,
         p_238464_2_ + p_238464_7_,
         p_238464_3_,
         p_238464_6_,
         p_238464_7_,
         p_238464_4_,
         p_238464_5_,
         p_238464_9_,
         p_238464_8_
      );
   }

   public static void func_238466_a_(
      MatrixStack p_238466_0_,
      int p_238466_1_,
      int p_238466_2_,
      int p_238466_3_,
      int p_238466_4_,
      float p_238466_5_,
      float p_238466_6_,
      int p_238466_7_,
      int p_238466_8_,
      int p_238466_9_,
      int p_238466_10_
   ) {
      func_238469_a_(
         p_238466_0_,
         p_238466_1_,
         p_238466_1_ + p_238466_3_,
         p_238466_2_,
         p_238466_2_ + p_238466_4_,
         0,
         p_238466_7_,
         p_238466_8_,
         p_238466_5_,
         p_238466_6_,
         p_238466_9_,
         p_238466_10_
      );
   }

   public static void func_238463_a_(
      MatrixStack p_238463_0_,
      int p_238463_1_,
      int p_238463_2_,
      float p_238463_3_,
      float p_238463_4_,
      int p_238463_5_,
      int p_238463_6_,
      int p_238463_7_,
      int p_238463_8_
   ) {
      func_238466_a_(
         p_238463_0_, p_238463_1_, p_238463_2_, p_238463_5_, p_238463_6_, p_238463_3_, p_238463_4_, p_238463_5_, p_238463_6_, p_238463_7_, p_238463_8_
      );
   }

   private static void func_238469_a_(
      MatrixStack p_238469_0_,
      int p_238469_1_,
      int p_238469_2_,
      int p_238469_3_,
      int p_238469_4_,
      int p_238469_5_,
      int p_238469_6_,
      int p_238469_7_,
      float p_238469_8_,
      float p_238469_9_,
      int p_238469_10_,
      int p_238469_11_
   ) {
      func_238461_a_(
         p_238469_0_.getLast().getMatrix(),
         p_238469_1_,
         p_238469_2_,
         p_238469_3_,
         p_238469_4_,
         p_238469_5_,
         (p_238469_8_ + 0.0F) / (float)p_238469_10_,
         (p_238469_8_ + (float)p_238469_6_) / (float)p_238469_10_,
         (p_238469_9_ + 0.0F) / (float)p_238469_11_,
         (p_238469_9_ + (float)p_238469_7_) / (float)p_238469_11_
      );
   }

   private static void func_238461_a_(
      Matrix4f p_238461_0_,
      int p_238461_1_,
      int p_238461_2_,
      int p_238461_3_,
      int p_238461_4_,
      int p_238461_5_,
      float p_238461_6_,
      float p_238461_7_,
      float p_238461_8_,
      float p_238461_9_
   ) {
      BufferBuilder lvt_10_1_ = Tessellator.getInstance().getBuffer();
      lvt_10_1_.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
      lvt_10_1_.pos(p_238461_0_, (float)p_238461_1_, (float)p_238461_4_, (float)p_238461_5_).color(r, g, b, a).tex(p_238461_6_, p_238461_9_).endVertex();
      lvt_10_1_.pos(p_238461_0_, (float)p_238461_2_, (float)p_238461_4_, (float)p_238461_5_).color(r, g, b, a).tex(p_238461_7_, p_238461_9_).endVertex();
      lvt_10_1_.pos(p_238461_0_, (float)p_238461_2_, (float)p_238461_3_, (float)p_238461_5_).color(r, g, b, a).tex(p_238461_7_, p_238461_8_).endVertex();
      lvt_10_1_.pos(p_238461_0_, (float)p_238461_1_, (float)p_238461_3_, (float)p_238461_5_).color(r, g, b, a).tex(p_238461_6_, p_238461_8_).endVertex();
      lvt_10_1_.finishDrawing();
      RenderSystem.enableAlphaTest();
      WorldVertexBufferUploader.draw(lvt_10_1_);
   }

   public static void setRGB(int r, int g, int b, int a) {
      BookBlit.r = r;
      BookBlit.g = g;
      BookBlit.b = b;
      BookBlit.a = a;
   }

   public void func_238459_a_(int p_238459_1_, int p_238459_2_, BiConsumer<Integer, Integer> p_238459_3_) {
      RenderSystem.blendFuncSeparate(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      p_238459_3_.accept(p_238459_1_ + 1, p_238459_2_);
      p_238459_3_.accept(p_238459_1_ - 1, p_238459_2_);
      p_238459_3_.accept(p_238459_1_, p_238459_2_ + 1);
      p_238459_3_.accept(p_238459_1_, p_238459_2_ - 1);
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      p_238459_3_.accept(p_238459_1_, p_238459_2_);
   }
}
