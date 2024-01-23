package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.client.gui.data.EntityLinkData;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityLinkButton extends Button {
   private static Map<String, Entity> renderedEntites = new HashMap<>();
   private EntityLinkData data;
   private GuiBasicBook bookGUI;
   private EntityLinkButton.EnttyRenderWindow window = new EntityLinkButton.EnttyRenderWindow();

   public EntityLinkButton(GuiBasicBook bookGUI, EntityLinkData linkData, int k, int l, IPressable o) {
      super(k + linkData.getX() - 12, l + linkData.getY(), (int)(24.0 * linkData.getScale()), (int)(24.0 * linkData.getScale()), new StringTextComponent(""), o);
      this.data = linkData;
      this.bookGUI = bookGUI;
   }

   public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      int lvt_5_1_ = 0;
      int lvt_6_1_ = 30;
      float f = (float)this.data.getScale();
      Minecraft.getInstance().getTextureManager().bindTexture(this.bookGUI.getBookWidgetTexture());
      matrixStack.push();
      RenderSystem.pushMatrix();
      RenderSystem.translatef((float)this.x, (float)this.y, 0.0F);
      RenderSystem.scalef(f, f, 1.0F);
      this.drawBtn(false, matrixStack, 0, 0, lvt_5_1_, lvt_6_1_, 24, 24);
      Entity model = null;
      EntityType type = (EntityType)ForgeRegistries.ENTITIES.getValue(new ResourceLocation(this.data.getEntity()));
      if (type != null) {
         model = (Entity)renderedEntites.putIfAbsent(this.data.getEntity(), type.create(Minecraft.getInstance().world));
      }

      RenderSystem.pushMatrix();
      if (model != null) {
         this.window.renderEntityWindow(matrixStack, model, (float)this.data.getEntityScale(), this.data.getOffset_x(), this.data.getOffset_y(), 2, 2, 22, 22);
      }

      RenderSystem.popMatrix();
      RenderSystem.depthFunc(515);
      RenderSystem.disableDepthTest();
      byte var14;
      if (this.isHovered()) {
         this.bookGUI.setEntityTooltip(this.data.getHoverText());
         var14 = 48;
      } else {
         var14 = 24;
      }

      int color = this.bookGUI.getWidgetColor();
      int r = (color & 0xFF0000) >> 16;
      int g = (color & 0xFF00) >> 8;
      int b = color & 0xFF;
      BookBlit.setRGB(r, g, b, 255);
      Minecraft.getInstance().getTextureManager().bindTexture(this.bookGUI.getBookWidgetTexture());
      this.drawBtn(!this.isHovered(), matrixStack, 0, 0, var14, lvt_6_1_, 24, 24);
      RenderSystem.popMatrix();
      matrixStack.pop();
   }

   public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
   }

   public void drawBtn(
      boolean color, MatrixStack p_238474_1_, int p_238474_2_, int p_238474_3_, int p_238474_4_, int p_238474_5_, int p_238474_6_, int p_238474_7_
   ) {
      if (color) {
         BookBlit.func_238464_a_(
            p_238474_1_, p_238474_2_, p_238474_3_, this.getBlitOffset(), (float)p_238474_4_, (float)p_238474_5_, p_238474_6_, p_238474_7_, 256, 256
         );
      } else {
         blit(p_238474_1_, p_238474_2_, p_238474_3_, this.getBlitOffset(), (float)p_238474_4_, (float)p_238474_5_, p_238474_6_, p_238474_7_, 256, 256);
      }
   }

   private class EnttyRenderWindow extends AbstractGui {
      private EnttyRenderWindow() {
      }

      public void renderEntityWindow(
         MatrixStack matrixStack, Entity toRender, float renderScale, float offsetX, float offsetY, int minX, int minY, int maxX, int maxY
      ) {
         matrixStack.push();
         matrixStack.translate(0.0, 0.0, -1.0);
         RenderSystem.pushMatrix();
         RenderSystem.enableDepthTest();
         RenderSystem.translatef(0.0F, 0.0F, 950.0F);
         RenderSystem.colorMask(false, false, false, false);
         fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
         RenderSystem.colorMask(true, true, true, true);
         RenderSystem.translatef(0.0F, 0.0F, -950.0F);
         RenderSystem.depthFunc(518);
         fill(matrixStack, 22, 22, 2, 2, -16777216);
         RenderSystem.depthFunc(515);
         Minecraft.getInstance().getTextureManager().bindTexture(EntityLinkButton.this.bookGUI.getBookWidgetTexture());
         blit(matrixStack, 0, 0, 0.0F, 30.0F, 24, 24, 256, 256);
         if (toRender != null) {
            toRender.ticksExisted = Minecraft.getInstance().player.ticksExisted;
            GuiBasicBook.drawEntityOnScreen((int)(12.0F + offsetX), (int)(24.0F + offsetY), 10.0F * renderScale, false, 30.0, -130.0, 0.0, 0.0F, 0.0F, toRender);
         }

         RenderSystem.depthFunc(518);
         RenderSystem.translatef(0.0F, 0.0F, -950.0F);
         RenderSystem.colorMask(false, false, false, false);
         fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
         RenderSystem.colorMask(true, true, true, true);
         RenderSystem.translatef(0.0F, 0.0F, 950.0F);
         RenderSystem.depthFunc(515);
         RenderSystem.popMatrix();
         matrixStack.pop();
      }
   }
}
