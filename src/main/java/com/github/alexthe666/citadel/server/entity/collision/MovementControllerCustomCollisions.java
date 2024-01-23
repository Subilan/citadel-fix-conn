package com.github.alexthe666.citadel.server.entity.collision;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.controller.MovementController.Action;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;

public class MovementControllerCustomCollisions extends MovementController {
   public MovementControllerCustomCollisions(MobEntity mob) {
      super(mob);
   }

   public void tick() {
      if (this.action == Action.STRAFE) {
         float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
         float f1 = (float)this.speed * f;
         float f2 = this.moveForward;
         float f3 = this.moveStrafe;
         float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
         if (f4 < 1.0F) {
            f4 = 1.0F;
         }

         f4 = f1 / f4;
         f2 *= f4;
         f3 *= f4;
         float f5 = MathHelper.sin(this.mob.rotationYaw * (float) (Math.PI / 180.0));
         float f6 = MathHelper.cos(this.mob.rotationYaw * (float) (Math.PI / 180.0));
         float f7 = f2 * f6 - f3 * f5;
         float f8 = f3 * f6 + f2 * f5;
         if (!this.func_234024_b_(f7, f8)) {
            this.moveForward = 1.0F;
            this.moveStrafe = 0.0F;
         }

         this.mob.setAIMoveSpeed(f1);
         this.mob.setMoveForward(this.moveForward);
         this.mob.setMoveStrafing(this.moveStrafe);
         this.action = Action.WAIT;
      } else if (this.action == Action.MOVE_TO) {
         this.action = Action.WAIT;
         double d0 = this.posX - this.mob.getPosX();
         double d1 = this.posZ - this.mob.getPosZ();
         double d2 = this.posY - this.mob.getPosY();
         double d3 = d0 * d0 + d2 * d2 + d1 * d1;
         if (d3 < 2.5000003E-7F) {
            this.mob.setMoveForward(0.0F);
            return;
         }

         float f9 = (float)(MathHelper.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F;
         this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f9, 90.0F);
         this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
         BlockPos blockpos = this.mob.getPosition();
         BlockState blockstate = this.mob.world.getBlockState(blockpos);
         Block block = blockstate.getBlock();
         VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.world, blockpos);
         if ((!(this.mob instanceof ICustomCollisions) || !((ICustomCollisions)this.mob).canPassThrough(blockpos, blockstate, voxelshape))
            && (
               d2 > (double)this.mob.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getWidth())
                  || !voxelshape.isEmpty()
                     && this.mob.getPosY() < voxelshape.getEnd(Axis.Y) + (double)blockpos.getY()
                     && !block.isIn(BlockTags.DOORS)
                     && !block.isIn(BlockTags.FENCES)
            )) {
            this.mob.getJumpController().setJumping();
            this.action = Action.JUMPING;
         }
      } else if (this.action == Action.JUMPING) {
         this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
         if (this.mob.isOnGround()) {
            this.action = Action.WAIT;
         }
      } else {
         this.mob.setMoveForward(0.0F);
      }
   }

   private boolean func_234024_b_(float p_234024_1_, float p_234024_2_) {
      PathNavigator pathnavigator = this.mob.getNavigator();
      if (pathnavigator != null) {
         NodeProcessor nodeprocessor = pathnavigator.getNodeProcessor();
         if (nodeprocessor != null
            && nodeprocessor.getFloorNodeType(
                  this.mob.world,
                  MathHelper.floor(this.mob.getPosX() + (double)p_234024_1_),
                  MathHelper.floor(this.mob.getPosY()),
                  MathHelper.floor(this.mob.getPosZ() + (double)p_234024_2_)
               )
               != PathNodeType.WALKABLE) {
            return false;
         }
      }

      return true;
   }
}
