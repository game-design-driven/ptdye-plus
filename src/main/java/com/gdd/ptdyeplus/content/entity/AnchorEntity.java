package com.gdd.ptdyeplus.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class AnchorEntity extends Entity {

    public AnchorEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
        this.blocksBuilding = false;
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

        move(MoverType.SELF, getDeltaMovement());

        if (!level().isClientSide) {
            checkInsideBlocks();
        }
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
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}
}
