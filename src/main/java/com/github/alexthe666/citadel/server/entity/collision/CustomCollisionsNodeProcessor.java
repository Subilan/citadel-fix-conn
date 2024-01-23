package com.github.alexthe666.citadel.server.entity.collision;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IBlockReader;

public class CustomCollisionsNodeProcessor extends WalkNodeProcessor {
   public static PathNodeType func_237231_a_(IBlockReader p_237231_0_, Mutable p_237231_1_) {
      int i = p_237231_1_.getX();
      int j = p_237231_1_.getY();
      int k = p_237231_1_.getZ();
      PathNodeType pathnodetype = getNodes(p_237231_0_, p_237231_1_);
      if (pathnodetype == PathNodeType.OPEN && j >= 1) {
         PathNodeType pathnodetype1 = getNodes(p_237231_0_, p_237231_1_.setPos(i, j - 1, k));
         pathnodetype = pathnodetype1 != PathNodeType.WALKABLE
               && pathnodetype1 != PathNodeType.OPEN
               && pathnodetype1 != PathNodeType.WATER
               && pathnodetype1 != PathNodeType.LAVA
            ? PathNodeType.WALKABLE
            : PathNodeType.OPEN;
         if (pathnodetype1 == PathNodeType.DAMAGE_FIRE) {
            pathnodetype = PathNodeType.DAMAGE_FIRE;
         }

         if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
            pathnodetype = PathNodeType.DAMAGE_CACTUS;
         }

         if (pathnodetype1 == PathNodeType.DAMAGE_OTHER) {
            pathnodetype = PathNodeType.DAMAGE_OTHER;
         }

         if (pathnodetype1 == PathNodeType.STICKY_HONEY) {
            pathnodetype = PathNodeType.STICKY_HONEY;
         }
      }

      if (pathnodetype == PathNodeType.WALKABLE) {
         pathnodetype = getSurroundingDanger(p_237231_0_, p_237231_1_.setPos(i, j, k), pathnodetype);
      }

      return pathnodetype;
   }

   protected static PathNodeType getNodes(IBlockReader p_237238_0_, BlockPos p_237238_1_) {
      BlockState blockstate = p_237238_0_.getBlockState(p_237238_1_);
      PathNodeType type = blockstate.getAiPathNodeType(p_237238_0_, p_237238_1_);
      if (type != null) {
         return type;
      } else {
         Block block = blockstate.getBlock();
         Material material = blockstate.getMaterial();
         if (blockstate.isAir(p_237238_0_, p_237238_1_)) {
            return PathNodeType.OPEN;
         } else {
            return blockstate.getBlock() == Blocks.BAMBOO ? PathNodeType.OPEN : func_237238_b_(p_237238_0_, p_237238_1_);
         }
      }
   }

   public PathNodeType getFloorNodeType(IBlockReader blockaccessIn, int x, int y, int z) {
      return func_237231_a_(blockaccessIn, new Mutable(x, y, z));
   }

   protected PathNodeType refineNodeType(IBlockReader world, boolean b1, boolean b2, BlockPos pos, PathNodeType nodeType) {
      BlockState state = world.getBlockState(pos);
      return ((ICustomCollisions)this.entity).canPassThrough(pos, state, state.getCollisionShape(world, pos))
         ? PathNodeType.OPEN
         : super.refineNodeType(world, b1, b2, pos, nodeType);
   }
}
