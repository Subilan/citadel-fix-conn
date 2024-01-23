package com.github.alexthe666.citadel.item;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemCitadelBook extends Item {
   public ItemCitadelBook(Properties properties) {
      super(properties);
   }

   public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
      ItemStack itemStackIn = playerIn.getHeldItem(handIn);
      if (worldIn.isRemote) {
         Citadel.PROXY.openBookGUI(itemStackIn);
      }

      return new ActionResult(ActionResultType.PASS, itemStackIn);
   }
}
