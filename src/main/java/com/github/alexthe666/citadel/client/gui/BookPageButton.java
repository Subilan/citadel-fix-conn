package com.github.alexthe666.citadel.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BookPageButton extends Button {
   private final boolean isForward;
   private final boolean playTurnSound;
   private GuiBasicBook bookGUI;

   public BookPageButton(GuiBasicBook bookGUI, int p_i51079_1_, int p_i51079_2_, boolean p_i51079_3_, IPressable p_i51079_4_, boolean p_i51079_5_) {
      super(p_i51079_1_, p_i51079_2_, 23, 13, StringTextComponent.EMPTY, p_i51079_4_);
      this.isForward = p_i51079_3_;
      this.playTurnSound = p_i51079_5_;
      this.bookGUI = bookGUI;
   }

   public void renderWidget(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      int lvt_5_1_ = 0;
      int lvt_6_1_ = 0;
      if (this.isHovered()) {
         lvt_5_1_ += 23;
      }

      if (!this.isForward) {
         lvt_6_1_ += 13;
      }

      int color = this.bookGUI.getWidgetColor();
      int r = (color & 0xFF0000) >> 16;
      int g = (color & 0xFF00) >> 8;
      int b = color & 0xFF;
      BookBlit.setRGB(r, g, b, 255);
      Minecraft.getInstance().getTextureManager().bindTexture(this.bookGUI.getBookWidgetTexture());
      this.drawNextArrow(p_230431_1_, this.x, this.y, lvt_5_1_, lvt_6_1_, 18, 12);
   }

   public void drawNextArrow(MatrixStack p_238474_1_, int p_238474_2_, int p_238474_3_, int p_238474_4_, int p_238474_5_, int p_238474_6_, int p_238474_7_) {
      if (this.isHovered()) {
         BookBlit.func_238464_a_(
            p_238474_1_, p_238474_2_, p_238474_3_, this.getBlitOffset(), (float)p_238474_4_, (float)p_238474_5_, p_238474_6_, p_238474_7_, 256, 256
         );
      } else {
         blit(p_238474_1_, p_238474_2_, p_238474_3_, this.getBlitOffset(), (float)p_238474_4_, (float)p_238474_5_, p_238474_6_, p_238474_7_, 256, 256);
      }
   }

   public void playDownSound(SoundHandler p_230988_1_) {
      if (this.playTurnSound) {
         p_230988_1_.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
      }
   }
}
