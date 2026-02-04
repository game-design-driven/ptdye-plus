package com.gdd.ptdyeplus.content.contraptions;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.init.Packet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class IndependentContraptionEntity extends AbstractContraptionEntity {

    protected Vec3 pivotPoint = Vec3.ZERO;
    protected Vec3 rotationVec = Vec3.ZERO;
    protected Vec3 prevRotationVec = Vec3.ZERO;
    protected Vec3 rotationDelta = Vec3.ZERO;
    protected Vec3 targetRotation = Vec3.ZERO;
    protected Vec3 rotationSpeed = Vec3.ZERO; // Degrees per tick per axis

    public IndependentContraptionEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public Vec3 getRotationVec(float partialTicks) {
        return new Vec3(
            AngleHelper.angleLerp(partialTicks, (float) prevRotationVec.x, (float) rotationVec.x),
            AngleHelper.angleLerp(partialTicks, (float) prevRotationVec.y, (float) rotationVec.y),
            AngleHelper.angleLerp(partialTicks, (float) prevRotationVec.z, (float) rotationVec.z)
        );
    }

    public void setTargetRotation(Vec3 target) {
        this.targetRotation = target;
        sendControlPacket();
    }

    public void setRotationSpeed(Vec3 speed) {
        this.rotationSpeed = speed;
        sendControlPacket();
    }

    public void setGoal(Vec3 target, float time) {
        targetRotation = target;

        if (time <= 0) {
            this.rotationVec = target;
            this.prevRotationVec = target;
            this.rotationSpeed = Vec3.ZERO;
        } else {
            rotationSpeed = new Vec3(
                Math.abs(target.x - rotationVec.x) / time,
                Math.abs(target.y - rotationVec.y) / time,
                Math.abs(target.z - rotationVec.z) / time
            );
        }

        PTDyePlus.LOGGER.info("Set goal to {}, in {} ticks", target, time);

        sendControlPacket();
    }

    protected void setTargetAndSpeed(Vec3 target, Vec3 speed) {
        this.targetRotation = target;
        this.rotationSpeed = speed;
    }

    public boolean isAtGoal() {
        return rotationVec.closerThan(targetRotation, 1e-3);
    }

    protected void onReachedGoal() {
        rotationVec = targetRotation;
        rotationSpeed = rotationDelta = Vec3.ZERO;

        sendControlPacket();
    }

    protected void sendControlPacket() {
        if (!level().isClientSide) {
            Packet.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
                new IndependentContraptionControlPacket(this));
        }
    }

    protected double advanceAngle(double current, double target, double speed) {
        if (speed <= 0)
            return current;
        double diff = target - current;
        if (Math.abs(diff) < speed)
            return target;
        return current + Math.signum(diff) * speed;
    }

    // Create Framework Implementation

    @Override
    protected void readAdditional(CompoundTag compound, boolean spawnPacket) {
        super.readAdditional(compound, spawnPacket);

        if (compound.contains("PivotPoint"))
            pivotPoint = VecHelper.readNBT(compound.getList("PivotPoint", CompoundTag.TAG_DOUBLE));
        if (compound.contains("FreeRotation"))
            rotationVec = VecHelper.readNBT(compound.getList("FreeRotation", CompoundTag.TAG_DOUBLE));
        if (compound.contains("TargetRotation"))
            targetRotation = VecHelper.readNBT(compound.getList("TargetRotation", CompoundTag.TAG_DOUBLE));
        if (compound.contains("RotationSpeed"))
            rotationSpeed = VecHelper.readNBT(compound.getList("RotationSpeed", CompoundTag.TAG_DOUBLE));
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        super.writeAdditional(compound, spawnPacket);
        compound.remove("ControllerRelative");

        compound.put("PivotPoint", VecHelper.writeNBT(pivotPoint));
        compound.put("FreeRotation", VecHelper.writeNBT(rotationVec));
        compound.put("TargetRotation", VecHelper.writeNBT(targetRotation));
        compound.put("RotationSpeed", VecHelper.writeNBT(rotationSpeed));
    }

    @Override
    public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
        Vec3 rotation = getRotationVec(partialTicks);
        Vec3 adjustedPivot = pivotPoint.subtract(0.5, 0.5, 0.5);
        Vec3 result = localPos.subtract(adjustedPivot);

        // YZX rotation order
        result = VecHelper.rotate(result, rotation.y, net.minecraft.core.Direction.Axis.Y);
        result = VecHelper.rotate(result, rotation.z, net.minecraft.core.Direction.Axis.Z);
        result = VecHelper.rotate(result, rotation.x, net.minecraft.core.Direction.Axis.X);

        return result.add(adjustedPivot);
    }

    @Override
    public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
        Vec3 rotation = getRotationVec(partialTicks);
        Vec3 adjustedPivot = pivotPoint.subtract(0.5, 0.5, 0.5);
        Vec3 result = localPos.subtract(adjustedPivot);

        // Reverse YZX rotation order
        result = VecHelper.rotate(result, -rotation.x, net.minecraft.core.Direction.Axis.X);
        result = VecHelper.rotate(result, -rotation.z, net.minecraft.core.Direction.Axis.Z);
        result = VecHelper.rotate(result, -rotation.y, net.minecraft.core.Direction.Axis.Y);

        return result.add(adjustedPivot);
    }

    @Override
    public ContraptionRotationState getRotationState() {
        ContraptionRotationState crs = new ContraptionRotationState();
        crs.xRotation = (float) rotationVec.x;
        crs.yRotation = (float) rotationVec.y;
        crs.zRotation = (float) rotationVec.z;
        return crs;
    }

    @Override
    public void teleportTo(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double x, double y, double z, float yw, float pt, int inc, boolean t) {
    }

    @Override
    protected void tickContraption() {
        // Todo: make this enabled, to see pivot point when working on contraptions
//        if (level().isClientSide) {
//            Vec3 worldPivot = getAnchorVec().add(pivotPoint);
//            level().addParticle(
//                net.minecraft.core.particles.ParticleTypes.FLAME,
//                worldPivot.x, worldPivot.y, worldPivot.z,
//                0, 0, 0
//            );
//        }

        tickActors();

        boolean isMoving = rotationSpeed.lengthSqr() > 1e-6;
        if (!isMoving) {
            rotationDelta = Vec3.ZERO;
            return;
        }

        prevRotationVec = rotationVec;

        rotationVec = new Vec3(
            advanceAngle(rotationVec.x, targetRotation.x, rotationSpeed.x),
            advanceAngle(rotationVec.y, targetRotation.y, rotationSpeed.y),
            advanceAngle(rotationVec.z, targetRotation.z, rotationSpeed.z)
        );

        rotationDelta = rotationVec.subtract(prevRotationVec);
        reapplyPosition();

        if (isAtGoal()) {
            onReachedGoal();
            return;
        }

        // Sync
        if (level().getGameTime() % 40 == 0) {
            sendControlPacket();
        }
    }

    @Override
    protected StructureTransform makeStructureTransform() {
        BlockPos pos = BlockPos.containing(getAnchorVec().add(0.0, 0.5, 0.5));

        return new IndependentStructureTransform(pos,
            this.pivotPoint,
            (float) rotationVec.x,
            (float) rotationVec.y,
            (float) rotationVec.z
        );
    }


    @Override
    protected float getStalledAngle() {
        return (float) rotationVec.y;
    }

    // This contraption cannot be stalled, as it isn't part of create's power system.
    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {
        Vec3 interpolatedRotation = getRotationVec(partialTicks);

        TransformStack.of(matrixStack)
            .nudge(getId());

        matrixStack.translate(-0.5, 0.0, -0.5);

        matrixStack.translate(pivotPoint.x, pivotPoint.y, pivotPoint.z);

        // YZX rotation order
        matrixStack.mulPose(Axis.YP.rotationDegrees((float) interpolatedRotation.y));
        matrixStack.mulPose(Axis.ZP.rotationDegrees((float) interpolatedRotation.z));
        matrixStack.mulPose(Axis.XP.rotationDegrees((float) interpolatedRotation.x));

        matrixStack.translate(-pivotPoint.x, -pivotPoint.y, -pivotPoint.z);
    }

    // Riding & Lifecycle

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);

        Packet.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new IndependentContraptionControlPacket(this));
    }

    @Override
    public void stopRiding() {
        if (!level().isClientSide && isAlive())
            disassemble();
        super.stopRiding();
    }

    @Override
    public Vec3 getAnchorVec() {
        return super.getAnchorVec().add(-0.5, 0.0, -0.5);
    }

    public static class IndependentContraptionControlPacket {

        int entityID;
        Vec3 currentRotation;
        Vec3 targetRotation;
        Vec3 rotationSpeed;
        Vec3 pivotPoint;

        public IndependentContraptionControlPacket(IndependentContraptionEntity entity) {
            this.entityID = entity.getId();
            this.currentRotation = entity.rotationVec;
            this.targetRotation = entity.targetRotation;
            this.rotationSpeed = entity.rotationSpeed;
            this.pivotPoint = entity.pivotPoint;
        }

        public IndependentContraptionControlPacket(FriendlyByteBuf buffer) {
            this.entityID = buffer.readInt();
            this.currentRotation = readVec3(buffer);
            this.targetRotation = readVec3(buffer);
            this.rotationSpeed = readVec3(buffer);
            this.pivotPoint = readVec3(buffer);
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(entityID);
            writeVec3(buffer, currentRotation);
            writeVec3(buffer, targetRotation);
            writeVec3(buffer, rotationSpeed);
            writeVec3(buffer, pivotPoint);
        }

        public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level != null && level.getEntity(entityID) instanceof IndependentContraptionEntity ice) {
                    ice.rotationVec = ice.prevRotationVec = this.currentRotation;
                    ice.pivotPoint = this.pivotPoint;
                    ice.targetRotation = this.targetRotation;
                    ice.rotationSpeed = this.rotationSpeed;
                }
            });
            return true;
        }

        private void writeVec3(FriendlyByteBuf buffer, Vec3 vec) {
            buffer.writeDouble(vec.x);
            buffer.writeDouble(vec.y);
            buffer.writeDouble(vec.z);
        }

        private Vec3 readVec3(FriendlyByteBuf buffer) {
            return new Vec3(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
            );
        }
    }
}
