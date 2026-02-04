package com.gdd.ptdyeplus.content.entity;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AnchorEntity extends Entity {

    private static final EntityDataAccessor<Boolean> FROZEN = SynchedEntityData.defineId(AnchorEntity.class, EntityDataSerializers.BOOLEAN);

    private Vec3 frozenPosition = Vec3.ZERO;

    public AnchorEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
        this.blocksBuilding = false;
    }

    public void freeze() {
        this.frozenPosition = new Vec3(getX(), getY(), getZ());
        this.entityData.set(FROZEN, true);
        this.setDeltaMovement(Vec3.ZERO);
    }

    public void unfreeze() {
        this.entityData.set(FROZEN, false);
    }

    public boolean isFrozen() {
        return this.entityData.get(FROZEN);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0d;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction movefunction) {
        if (hasPassenger(passenger)) {
            movefunction.accept(passenger, getX(), getY(), getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (isFrozen()) {
            setDeltaMovement(Vec3.ZERO);
            if (!level().isClientSide) {
                double distSq = this.position().distanceToSqr(frozenPosition);
                if (distSq > 1e-4) {
                    teleportTo(frozenPosition.x, frozenPosition.y, frozenPosition.z);
                }
            }
        } else {
            move(MoverType.SELF, getDeltaMovement());

            if (!level().isClientSide) {
                checkInsideBlocks();
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps, boolean teleport) {
        if (isFrozen()) {
            super.lerpTo(x, y, z, yRot, xRot, 0, true);
            this.setPos(x, y, z);
            this.setRot(yRot, xRot);
            return;
        }
        super.lerpTo(x, y, z, yRot, xRot, steps, teleport);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplayer, DamageSource source) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FROZEN, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        boolean isFrozen = nbt.getBoolean("IsFrozen");
        this.entityData.set(FROZEN, isFrozen);
        if (isFrozen) {
            frozenPosition = VecHelper.readNBT(nbt.getList("FrozenPosition", CompoundTag.TAG_DOUBLE));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        boolean isFrozen = isFrozen();
        nbt.putBoolean("IsFrozen", isFrozen);
        if (isFrozen) {
            nbt.put("FrozenPosition", VecHelper.writeNBT(frozenPosition));
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {}

    @Override
    public boolean canCollideWith(Entity entity) {
        return super.canCollideWith(entity);
    }
}
