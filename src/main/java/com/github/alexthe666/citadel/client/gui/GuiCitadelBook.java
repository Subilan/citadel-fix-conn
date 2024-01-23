package com.github.alexthe666.citadel.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiCitadelBook extends GuiBasicBook {
   public GuiCitadelBook(ItemStack bookStack) {
      super(bookStack, new TranslationTextComponent("citadel_guide_book.title"));
   }

   @Override
   protected int getBindingColor() {
      return 6595195;
   }

   @Override
   public ResourceLocation getRootPage() {
      return new ResourceLocation("citadel:book/citadel_book/root.json");
   }

   @Override
   public String getTextFileDirectory() {
      return "citadel:book/citadel_book/";
   }
}
