package com.github.alexthe666.citadel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Properties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   bus = Bus.MOD
)
public class ServerProxy {
   public void onPreInit() {
   }

   public void handleAnimationPacket(int entityId, int index) {
   }

   @SubscribeEvent
   public static void onItemsRegistry(Register<Item> registry) {
      registry.getRegistry().registerAll(new Item[]{Citadel.DEBUG_ITEM, Citadel.CITADEL_BOOK, Citadel.EFFECT_ITEM, Citadel.FANCY_ITEM});
   }

   public void handlePropertiesPacket(String propertyID, CompoundNBT compound, int entityID) {
   }

   public Properties setupISTER(Properties group) {
      return group;
   }

   public void openBookGUI(ItemStack book) {
   }
}
