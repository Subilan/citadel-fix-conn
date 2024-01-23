package com.github.alexthe666.citadel.server.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

public class CustomArmorMaterial implements IArmorMaterial {
   private String name;
   private int durability;
   private int[] damageReduction;
   private int encantability;
   private SoundEvent sound;
   private float toughness;
   private Ingredient ingredient = null;
   public float knockbackResistance = 0.0F;

   public CustomArmorMaterial(
      String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness, float knockbackResistance
   ) {
      this.name = name;
      this.durability = durability;
      this.damageReduction = damageReduction;
      this.encantability = encantability;
      this.sound = sound;
      this.toughness = toughness;
      this.knockbackResistance = knockbackResistance;
   }

   public int getDurability(EquipmentSlotType slotIn) {
      return this.durability;
   }

   public int getDamageReductionAmount(EquipmentSlotType slotIn) {
      return this.damageReduction[slotIn.getIndex()];
   }

   public int getEnchantability() {
      return this.encantability;
   }

   public SoundEvent getSoundEvent() {
      return this.sound;
   }

   public Ingredient getRepairMaterial() {
      return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
   }

   public void setRepairMaterial(Ingredient ingredient) {
      this.ingredient = ingredient;
   }

   public String getName() {
      return this.name;
   }

   public float getToughness() {
      return this.toughness;
   }

   public float getKnockbackResistance() {
      return this.knockbackResistance;
   }
}
