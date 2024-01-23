package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.CitadelItemstackRenderer;
import com.github.alexthe666.citadel.client.CitadelPatreonRenderer;
import com.github.alexthe666.citadel.client.event.EventGetOutlineColor;
import com.github.alexthe666.citadel.client.gui.GuiCitadelBook;
import com.github.alexthe666.citadel.client.gui.GuiCitadelPatreonConfig;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.citadel.client.patreon.SpaceStationPatreonRenderer;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityDataHandler;
import com.github.alexthe666.citadel.server.entity.datatracker.EntityProperties;
import com.github.alexthe666.citadel.server.entity.datatracker.IEntityData;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.io.IOException;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Properties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   bus = Bus.MOD,
   value = {Dist.CLIENT}
)
public class ClientProxy extends ServerProxy {
   public static TabulaModel CITADEL_MODEL;
   private static final ResourceLocation CITADEL_TEXTURE = new ResourceLocation("citadel", "textures/patreon/citadel_model.png");
   private static final ResourceLocation CITADEL_TEXTURE_RED = new ResourceLocation("citadel", "textures/patreon/citadel_model_red.png");
   private static final ResourceLocation CITADEL_TEXTURE_GRAY = new ResourceLocation("citadel", "textures/patreon/citadel_model_gray.png");

   @Override
   public void onPreInit() {
      try {
         CITADEL_MODEL = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/citadel/models/citadel_model"));
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      CitadelPatreonRenderer.register("citadel", new SpaceStationPatreonRenderer(CITADEL_TEXTURE));
      CitadelPatreonRenderer.register("citadel_red", new SpaceStationPatreonRenderer(CITADEL_TEXTURE_RED));
      CitadelPatreonRenderer.register("citadel_gray", new SpaceStationPatreonRenderer(CITADEL_TEXTURE_GRAY));
   }

   @SubscribeEvent
   public void openCustomizeSkinScreen(InitGuiEvent event) {
      if (event.getGui() instanceof CustomizeSkinScreen && Minecraft.getInstance().player != null) {
         try {
            String username = Minecraft.getInstance().player.getName().getUnformattedComponentText();
            if (Citadel.PATREONS.contains(username)) {
               event.addWidget(
                  new Button(
                     event.getGui().width / 2 - 100,
                     event.getGui().height / 6 + 150,
                     200,
                     20,
                     new TranslationTextComponent("citadel.gui.patreon_rewards_option").mergeStyle(TextFormatting.GREEN),
                     p_213080_2_ -> Minecraft.getInstance().displayGuiScreen(new GuiCitadelPatreonConfig(event.getGui(), Minecraft.getInstance().gameSettings))
                  )
               );
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   @SubscribeEvent
   public void playerRender(Post event) {
      MatrixStack matrixStackIn = event.getMatrixStack();
      String username = event.getPlayer().getName().getUnformattedComponentText();
      if (event.getPlayer().isWearing(PlayerModelPart.CAPE)) {
         if (Citadel.PATREONS.contains(username)) {
            CompoundNBT tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
            String rendererName = tag.contains("CitadelFollowerType") ? tag.getString("CitadelFollowerType") : "citadel";
            if (!rendererName.equals("none")) {
               CitadelPatreonRenderer renderer = CitadelPatreonRenderer.get(rendererName);
               if (renderer != null) {
                  float distance = tag.contains("CitadelRotateDistance") ? tag.getFloat("CitadelRotateDistance") : 2.0F;
                  float speed = tag.contains("CitadelRotateSpeed") ? tag.getFloat("CitadelRotateSpeed") : 1.0F;
                  float height = tag.contains("CitadelRotateHeight") ? tag.getFloat("CitadelRotateHeight") : 1.0F;
                  renderer.render(
                     matrixStackIn, event.getBuffers(), event.getLight(), event.getPartialRenderTick(), event.getEntityLiving(), distance, speed, height
                  );
               }
            }
         }
      }
   }

   @Override
   public void handleAnimationPacket(int entityId, int index) {
      PlayerEntity player = Minecraft.getInstance().player;
      if (player != null) {
         IAnimatedEntity entity = (IAnimatedEntity)player.world.getEntityByID(entityId);
         if (entity != null) {
            if (index == -1) {
               entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            } else {
               entity.setAnimation(entity.getAnimations()[index]);
            }

            entity.setAnimationTick(0);
         }
      }
   }

   @Override
   public void handlePropertiesPacket(String propertyID, CompoundNBT compound, int entityID) {
      if (compound != null) {
         PlayerEntity player = Minecraft.getInstance().player;
         Entity entity = player.world.getEntityByID(entityID);
         if (propertyID.equals("CitadelPatreonConfig") && entity instanceof LivingEntity) {
            CitadelEntityData.setCitadelTag((LivingEntity)entity, compound);
         } else if (entity != null) {
            IEntityData<?> extendedProperties = EntityDataHandler.INSTANCE.getEntityData(entity, propertyID);
            if (extendedProperties instanceof EntityProperties) {
               EntityProperties<?> properties = (EntityProperties)extendedProperties;
               properties.loadTrackingSensitiveData(compound);
               properties.onSync();
            }
         }
      }
   }

   @Override
   public Properties setupISTER(Properties group) {
      return group.setISTER(ClientProxy::getTEISR);
   }

   @OnlyIn(Dist.CLIENT)
   public static Callable<ItemStackTileEntityRenderer> getTEISR() {
      return CitadelItemstackRenderer::new;
   }

   @Override
   public void openBookGUI(ItemStack book) {
      Minecraft.getInstance().displayGuiScreen(new GuiCitadelBook(book));
   }

   @SubscribeEvent
   public void outlineColorTest(EventGetOutlineColor event) {
   }
}
