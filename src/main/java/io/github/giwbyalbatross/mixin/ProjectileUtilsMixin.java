package io.github.giwbyalbatross.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

@Mixin(ProjectileUtil.class)
class ProjectileUtilsMixin {

    //public static Collection<EntityHitResult> getManyEntityHitResult(Level level, Entity entity, Vec3 vec3, Vec3 vec32, AABB aABB, Predicate<Entity> predicate, float f, Block block, boolean bl)
    @Inject(at = @At("HEAD"), method = "getManyEntityHitResult", cancellable = true)
    static void getManyEntityHitResultOverride(
        Level level, Entity entity, Vec3 vec3, Vec3 vec32, AABB aabb,
        Predicate<Entity> predicate, float f, Block block, boolean includeSelf,
        CallbackInfoReturnable<Collection<EntityHitResult>> cir
    ) {
        @Nullable EntityHitResult ehr = ProjectileUtil.getEntityHitResult(level, entity, vec3, vec32, aabb, predicate, f);
        List<EntityHitResult> r = new ArrayList<EntityHitResult>();

        if (ehr != null)
            r.add(ehr);

        cir.setReturnValue(r);
    }
}
