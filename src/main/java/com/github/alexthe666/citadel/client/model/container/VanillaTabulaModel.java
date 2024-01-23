package com.github.alexthe666.citadel.client.model.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VanillaTabulaModel implements IUnbakedModel {
   private TabulaModelContainer model;
   private RenderMaterial particle;
   private Collection<RenderMaterial> textures;
   private ImmutableMap<TransformType, TransformationMatrix> transforms;

   public VanillaTabulaModel(
      TabulaModelContainer model, RenderMaterial particle, ImmutableList<RenderMaterial> textures, ImmutableMap<TransformType, TransformationMatrix> transforms
   ) {
      this.model = model;
      this.particle = particle;
      this.textures = textures;
      this.transforms = transforms;
   }

   public Collection<ResourceLocation> getDependencies() {
      return ImmutableList.of();
   }

   public Collection<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
      return this.textures;
   }

   @Nullable
   public IBakedModel bakeModel(
      ModelBakery modelBakery, Function<RenderMaterial, TextureAtlasSprite> function, IModelTransform iModelTransform, ResourceLocation resourceLocation
   ) {
      return null;
   }
}
