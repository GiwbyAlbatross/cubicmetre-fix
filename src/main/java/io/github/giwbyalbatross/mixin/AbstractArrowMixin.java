package io.github.giwbyalbatross.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(at = @At("HEAD"), method = "findHitEntities", cancellable = true)
	private void findHitEntitiesOverride(final Vec3 from, final Vec3 to, CallbackInfoReturnable<Collection<EntityHitResult>> info) {
		EntityHitResult ehr =  ProjectileUtil.getEntityHitResult(
        	 this.level(), this, from, to, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
      	);
		List<EntityHitResult> r = new ArrayList<EntityHitResult>();

		r.add(ehr);

		info.setReturnValue(r);
	}
}
