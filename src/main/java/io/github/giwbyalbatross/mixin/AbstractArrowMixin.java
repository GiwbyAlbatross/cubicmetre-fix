package io.github.giwbyalbatross.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends AbstractArrow {
	public AbstractArrowMixin(
      final EntityType<? extends AbstractArrow> type,
      final LivingEntity mob,
      final Level level,
      final ItemStack pickupItemStack,
      final @Nullable ItemStack firedFromWeapon
   ) {
	  // solely to stop VSCode reporting errors which likely would never surface
      super(type, mob.getX(), mob.getEyeY() - 0.1F, mob.getZ(), level, pickupItemStack, firedFromWeapon);
      this.setOwner(mob);
   }

	/*
	// TURNS OUT NOT TO WORK 
	@Inject(at = @At("HEAD"), method = "findHitEntities", cancellable = true)
	private void findHitEntitiesOverride(final Vec3 from, final Vec3 to, CallbackInfoReturnable<Collection<EntityHitResult>> info) {
		EntityHitResult ehr =  ProjectileUtil.getEntityHitResult(
        	 this.level(), this, from, to, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
      	);
		List<EntityHitResult> r = new ArrayList<EntityHitResult>();

		r.add(ehr);

		info.setReturnValue(r);
	}
	*/
	/*
	// also TURNS OUT NOT TO WORK RELIABLY
	@Inject(at = @At("HEAD"), method="stepMoveAndHit", cancellable = true)
	private void stepMoveAndHitOverride(BlockHitResult blockHitResult, CallbackInfo cir) {
		* 
		 * Perhaps the dumbest wait I could have solved this: literally copy-pasting decompiled 1.21.10
		 * code into the mixin for 1.21.11 because 1.21.10 definitely works.
		 *
		while (this.isAlive()) {
         Vec3 vec3 = this.position();
         EntityHitResult entityHitResult = this.findHitEntity(vec3, blockHitResult.getLocation());
         Vec3 vec32 = ((HitResult)Objects.requireNonNullElse(entityHitResult, blockHitResult)).getLocation();
         this.setPos(vec32);
         this.applyEffectsFromBlocks(vec3, vec32);
         if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
            this.handlePortal();
         }

         if (entityHitResult == null) {
            if (this.isAlive() && blockHitResult.getType() != Type.MISS) {
               this.hitTargetOrDeflectSelf(blockHitResult);
               this.needsSync = true;
            }
            break;
         } else if (this.isAlive() && !this.noPhysics) {
            ProjectileDeflection projectileDeflection = this.hitTargetOrDeflectSelf(entityHitResult);
            this.needsSync = true;
            if (this.getPierceLevel() > 0 && projectileDeflection == ProjectileDeflection.NONE) {
               continue;
            }
            break;
         }
      }
	}
	*/
}
