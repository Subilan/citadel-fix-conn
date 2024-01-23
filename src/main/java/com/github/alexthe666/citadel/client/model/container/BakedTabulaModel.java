package com.github.alexthe666.citadel.client.model.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.TransformationMatrix;

public class BakedTabulaModel implements IBakedModel {
   private ImmutableList<BakedQuad> quads;
   private TextureAtlasSprite particle;
   private ImmutableMap<TransformType, TransformationMatrix> transforms;

   public BakedTabulaModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<TransformType, TransformationMatrix> transforms) {
      this.quads = quads;
      this.particle = particle;
      this.transforms = transforms;
   }

   public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
      return this.quads;
   }

   public boolean isAmbientOcclusion() {
      return true;
   }

   public boolean isGui3d() {
      return false;
   }

   public boolean isSideLit() {
      return false;
   }

   public boolean isBuiltInRenderer() {
      return false;
   }

   public TextureAtlasSprite getParticleTexture() {
      return this.particle;
   }

   public ItemCameraTransforms getItemCameraTransforms() {
      return ItemCameraTransforms.DEFAULT;
   }

   public ItemOverrideList getOverrides() {
      return ItemOverrideList.EMPTY;
   }
}
