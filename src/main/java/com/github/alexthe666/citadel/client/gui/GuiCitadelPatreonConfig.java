package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.CitadelPatreonRenderer;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;
import net.minecraftforge.fml.client.gui.widget.Slider.ISlider;

@OnlyIn(Dist.CLIENT)
public class GuiCitadelPatreonConfig extends SettingsScreen {
   private Slider distSlider;
   private Slider speedSlider;
   private Slider heightSlider;
   private Button changeButton;
   private final ISlider distSliderResponder;
   private final ISlider speedSliderResponder;
   private final ISlider heightSliderResponder;
   private float rotateDist;
   private float rotateSpeed;
   private float rotateHeight;
   private String followType = "citadel";

   public GuiCitadelPatreonConfig(Screen parentScreenIn, GameSettings gameSettingsIn) {
      super(parentScreenIn, gameSettingsIn, new TranslationTextComponent("citadel.gui.patreon_customization"));
      CompoundNBT tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
      float distance = tag.contains("CitadelRotateDistance") ? tag.getFloat("CitadelRotateDistance") : 2.0F;
      float speed = tag.contains("CitadelRotateSpeed") ? tag.getFloat("CitadelRotateSpeed") : 1.0F;
      float height = tag.contains("CitadelRotateHeight") ? tag.getFloat("CitadelRotateHeight") : 1.0F;
      this.rotateDist = roundTo(distance, 3);
      this.rotateSpeed = roundTo(speed, 3);
      this.rotateHeight = roundTo(height, 3);
      this.followType = tag.contains("CitadelFollowerType") ? tag.getString("CitadelFollowerType") : "citadel";
      this.distSliderResponder = new ISlider() {
         public void onChangeSliderValue(Slider slider) {
            GuiCitadelPatreonConfig.this.setSliderValue(0, (float)slider.sliderValue);
         }
      };
      this.speedSliderResponder = new ISlider() {
         public void onChangeSliderValue(Slider slider) {
            GuiCitadelPatreonConfig.this.setSliderValue(1, (float)slider.sliderValue);
         }
      };
      this.heightSliderResponder = new ISlider() {
         public void onChangeSliderValue(Slider slider) {
            GuiCitadelPatreonConfig.this.setSliderValue(2, (float)slider.sliderValue);
         }
      };
   }

   private void setSliderValue(int i, float sliderValue) {
      boolean flag = false;
      CompoundNBT tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
      if (i == 0) {
         this.rotateDist = roundTo(sliderValue * 5.0F, 3);
         tag.putFloat("CitadelRotateDistance", this.rotateDist);
         this.distSlider.dragging = false;
      } else if (i == 1) {
         this.rotateSpeed = roundTo(sliderValue * 5.0F, 3);
         tag.putFloat("CitadelRotateSpeed", this.rotateSpeed);
         this.speedSlider.dragging = false;
      } else {
         this.rotateHeight = roundTo(sliderValue * 2.0F, 3);
         tag.putFloat("CitadelRotateHeight", this.rotateHeight);
         this.heightSlider.dragging = false;
      }

      CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
      Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.getEntityId()));
   }

   public static float roundTo(float value, int places) {
      return value;
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(matrixStack);
      drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 20, 16777215);
      super.render(matrixStack, mouseX, mouseY, partialTicks);
   }

   protected void init() {
      super.init();
      int i = this.width / 2;
      int j = this.height / 6;
      this.addButton(new Button(i - 100, j + 120, 200, 20, DialogTexts.GUI_DONE, p_213079_1_ -> this.minecraft.displayGuiScreen(this.parentScreen)));
      this.addButton(
         this.distSlider = new Slider(
            i - 75 - 25,
            j + 30,
            150,
            20,
            new TranslationTextComponent("citadel.gui.orbit_dist").appendSibling(new StringTextComponent(": ")),
            new StringTextComponent(""),
            0.125,
            5.0,
            (double)this.rotateDist,
            true,
            true,
            p_214132_1_ -> {
            },
            this.distSliderResponder
         ) {
         }
      );
      this.addButton(new Button(i - 75 + 135, j + 30, 40, 20, new TranslationTextComponent("citadel.gui.reset"), p_213079_1_ -> {
         this.setSliderValue(0, 0.4F);
         this.distSlider.sliderValue = 0.4F;
         this.distSlider.updateSlider();
      }));
      this.addButton(
         this.speedSlider = new Slider(
            i - 75 - 25,
            j + 60,
            150,
            20,
            new TranslationTextComponent("citadel.gui.orbit_speed").appendSibling(new StringTextComponent(": ")),
            new StringTextComponent(""),
            0.0,
            5.0,
            (double)this.rotateSpeed,
            true,
            true,
            p_214132_1_ -> {
            },
            this.speedSliderResponder
         ) {
         }
      );
      this.addButton(new Button(i - 75 + 135, j + 60, 40, 20, new TranslationTextComponent("citadel.gui.reset"), p_213079_1_ -> {
         this.setSliderValue(1, 0.2F);
         this.speedSlider.sliderValue = 0.2F;
         this.speedSlider.updateSlider();
      }));
      this.addButton(
         this.heightSlider = new Slider(
            i - 75 - 25,
            j + 90,
            150,
            20,
            new TranslationTextComponent("citadel.gui.orbit_height").appendSibling(new StringTextComponent(": ")),
            new StringTextComponent(""),
            0.0,
            2.0,
            (double)this.rotateHeight,
            true,
            true,
            p_214132_1_ -> {
            },
            this.heightSliderResponder
         ) {
         }
      );
      this.addButton(new Button(i - 75 + 135, j + 90, 40, 20, new TranslationTextComponent("citadel.gui.reset"), p_213079_1_ -> {
         this.setSliderValue(2, 0.5F);
         this.heightSlider.sliderValue = 0.5;
         this.heightSlider.updateSlider();
      }));
      this.distSlider.precision = 1;
      this.heightSlider.precision = 2;
      this.speedSlider.precision = 2;
      this.distSlider.updateSlider();
      this.heightSlider.updateSlider();
      this.speedSlider.updateSlider();
      this.addButton(this.changeButton = new Button(i - 100, j, 200, 20, this.getTypeText(), p_213079_1_ -> {
         this.followType = CitadelPatreonRenderer.getIdOfNext(this.followType);
         CompoundNBT tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
         if (tag != null) {
            tag.putString("CitadelFollowerType", this.followType);
            CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
         }

         Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.getEntityId()));
         this.changeButton.setMessage(this.getTypeText());
      }));
   }

   private ITextComponent getTypeText() {
      return new TranslationTextComponent("citadel.gui.follower_type").appendSibling(new TranslationTextComponent("citadel.follower." + this.followType));
   }
}
