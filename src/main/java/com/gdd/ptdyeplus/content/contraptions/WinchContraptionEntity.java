package com.gdd.ptdyeplus.content.contraptions;

import com.gdd.ptdyeplus.init.Packet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class WinchContraptionEntity extends AbstractContraptionEntity {

    public enum WinchState {
        IDLE,
        LOWERING,
        RAISING
    }

    // Global Config
    protected static int maxDropDistance = 64; // Max height from contraption to drop payload

    // Per Contraption
    protected Vec3 winchOriginOffset = new Vec3(0.5d, 0.0d, 0.5d);
    protected double winchSpeed = 0.5d; // Blocks per tick
    protected double heightAboveGround = 0.0d;
    protected double ropeInsertOffset = 1.0d; // How deep into the winch the rope starts
    protected Vec3 tetherOffset = Vec3.ZERO;
    protected Vec3 prevTetherOffset = Vec3.ZERO;
    protected WinchState winchState = WinchState.IDLE;
    protected UUID payloadUUID;
    protected Entity cachedPayloadEntity;

    public WinchContraptionEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public void setWinchOrigin(Vec3 localPos) {
        this.winchOriginOffset = localPos.add(0.5d, 0.0d, 0.5d);
        sendControlPacket();
    }

    public void setPayload(Entity entity) {
        if (entity == null) {
            this.payloadUUID = null;
            this.cachedPayloadEntity = null;
            return;
        }
        this.payloadUUID = entity.getUUID();
        this.cachedPayloadEntity = entity;

        sendControlPacket();
    }

    public Entity disconnectPayload() {
        if (this.payloadUUID == null)
            return null;

        Entity payload = getPayloadEntity();
        if (payload != null) {
            Vec3 anchor = getAnchorVec();
            Vec3 origin = anchor.add(winchOriginOffset);
            Vec3 finalPos = origin.add(tetherOffset);

            payload.setPos(finalPos);
            payload.setDeltaMovement(Vec3.ZERO);
        }

        this.payloadUUID = null;
        this.cachedPayloadEntity = null;
        this.sendControlPacket();

        return payload;
    }

    public void setRopeInsertOffset(double ropeInsertOffset) {
        this.ropeInsertOffset = ropeInsertOffset;
        sendControlPacket();
    }

    public void lowerToSurface(double speed, double heightAboveGround) {
        this.winchSpeed = Math.abs(speed);
        this.heightAboveGround = heightAboveGround;
        this.winchState = WinchState.LOWERING;
        sendControlPacket();
    }

    public void retract(double speed) {
        this.winchSpeed = Math.abs(speed);
        this.winchState = WinchState.RAISING;
        sendControlPacket();
    }

    public boolean isWinchIdle() {
        return getWinchState() == WinchState.IDLE;
    }

    public WinchState getWinchState() {
        return winchState;
    }

    public Entity getPayloadEntity() {
        if (cachedPayloadEntity != null && cachedPayloadEntity.isAlive()) {
            return cachedPayloadEntity;
        }
        if (payloadUUID != null) {
            if (level() instanceof net.minecraft.server.level.ServerLevel sl) {
                cachedPayloadEntity = sl.getEntity(payloadUUID);
            } else {
                cachedPayloadEntity = level().getEntities(this, getBoundingBoxForCulling().inflate(64),
                    e -> e.getUUID().equals(payloadUUID)).stream().findFirst().orElse(null);
            }
        }
        return cachedPayloadEntity;
    }

    // Core logic

    @Override
    protected void tickContraption() {
        prevTetherOffset = tetherOffset;

        // Movement
        if (winchState == WinchState.LOWERING) {
            tetherOffset = tetherOffset.add(0, -winchSpeed, 0);
        } else if (winchState == WinchState.RAISING) {
            tetherOffset = tetherOffset.add(0, winchSpeed, 0);
            if (tetherOffset.y > 0) {
                tetherOffset = new Vec3(tetherOffset.x, 0, tetherOffset.y);
            }
        }

        if (winchState != WinchState.IDLE) {
            setDeltaMovement(0.0, 0.0, 0.0);
        }

        if (tetherOffset.y > 0) tetherOffset = Vec3.ZERO;

        Entity payload = getPayloadEntity();
        if (!level().isClientSide) {
            Vec3 originWorld = getAnchorVec().add(winchOriginOffset);

            // Server side control & physics
            if (winchState == WinchState.LOWERING) {
                double surfaceY = getSurfaceBelow(level(), originWorld);
                if (originWorld.y - surfaceY > maxDropDistance) {
                    surfaceY = originWorld.y - maxDropDistance;
                }

                double targetLocalY = (surfaceY + heightAboveGround) - originWorld.y;

                if (tetherOffset.y <= targetLocalY) {
                    tetherOffset = new Vec3(0, targetLocalY, 0);
                    winchState = WinchState.IDLE;
                    sendControlPacket();
                }
            } else if (winchState == WinchState.RAISING) {
                if (tetherOffset.y >= 0) {
                    tetherOffset = Vec3.ZERO;
                    winchState = WinchState.IDLE;
                    sendControlPacket();
                }
            }
            if (payload != null) {
                Vec3 finalPos = originWorld.add(tetherOffset);
                payload.setPos(finalPos);
                payload.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().expandTowards(0, -maxDropDistance, 0).inflate(2);
    }

    private static double getSurfaceBelow(Level level, Vec3 start) {
        BlockPos centerPos = BlockPos.containing(start);
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        for (int yOffset = 0; yOffset < maxDropDistance; yOffset++) {
            int currentY = centerPos.getY() - yOffset;
            // Check 3x3 area at this Y
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    checkPos.set(centerPos.getX() + x, currentY, centerPos.getZ() + z);
                    if (!level.getBlockState(checkPos).getCollisionShape(level, checkPos).isEmpty()) {
                        return currentY + 1.0d;
                    }
                }
            }
        }
        return start.y - maxDropDistance;
    }

    @OnlyIn(Dist.CLIENT)
    public Vec3 getTetherOffset(float partialTicks) {
        return new Vec3(
            Mth.lerp(partialTicks, prevTetherOffset.x, tetherOffset.x),
            Mth.lerp(partialTicks, prevTetherOffset.y, tetherOffset.y),
            Mth.lerp(partialTicks, prevTetherOffset.z, tetherOffset.z)
        );
    }

    public Vec3 getWinchOriginOffset() {
        return winchOriginOffset;
    }

    // Persistence & Networking

    @Override
    protected void readAdditional(CompoundTag compound, boolean spawnPacket) {
        super.readAdditional(compound, spawnPacket);
        if (compound.contains("WinchOriginOffset"))
            winchOriginOffset = VecHelper.readNBT(compound.getList("WinchOriginOffset", CompoundTag.TAG_DOUBLE));
        if (compound.contains("WinchSpeed"))
            winchSpeed = compound.getDouble("WinchSpeed");
        if (compound.contains("HeightAboveGround"))
            heightAboveGround = compound.getDouble("HeightAboveGround");
        if (compound.contains("RopeInsertOffset"))
            ropeInsertOffset = compound.getDouble("RopeInsertOffset");
        if (compound.contains("TetherOffset"))
            tetherOffset = VecHelper.readNBT(compound.getList("TetherOffset", CompoundTag.TAG_DOUBLE));
        if (compound.contains("WinchState"))
            winchState = WinchState.values()[compound.getInt("WinchState")];
        if (compound.contains("PayloadUUID"))
            payloadUUID = compound.getUUID("PayloadUUID");

        prevTetherOffset = tetherOffset;
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        super.writeAdditional(compound, spawnPacket);
        compound.put("WinchOriginOffset", VecHelper.writeNBT(winchOriginOffset));
        compound.putDouble("WinchSpeed", winchSpeed);
        compound.putDouble("HeightAboveGround", heightAboveGround);
        compound.putDouble("RopeInsertOffset", ropeInsertOffset);
        compound.put("TetherOffset", VecHelper.writeNBT(tetherOffset));
        compound.putInt("WinchState", winchState.ordinal());
        if (payloadUUID != null)
            compound.putUUID("PayloadUUID", payloadUUID);
    }

    protected void sendControlPacket() {
        if (!level().isClientSide) {
            Packet.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
                new WinchContraptionControlPacket(this));
        }
    }

    // Required boilerplate

    @Override
    public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    protected StructureTransform makeStructureTransform() {
        return new StructureTransform(BlockPos.containing(getAnchorVec()), Direction.Axis.Y, Rotation.NONE, Mirror.NONE);
    }

    @Override
    protected float getStalledAngle() {
        return 0;
    }

    // This contraption cannot be stalled, as it isn't part of create's power system.
    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {}

    @Override
    public ContraptionRotationState getRotationState() {
        return new ContraptionRotationState();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {}

    @Override
    public boolean canCollideWith(Entity entity) {
        if (entity.getUUID().equals(this.payloadUUID)) {
            return false;
        }
        return super.canCollideWith(entity);
    }

    public static class WinchContraptionControlPacket {
        int entityId;
        double winchSpeed;
        double ropeInsertOffset;
        Vec3 tetherOffset;
        Vec3 winchOrigin;
        int stateOrdinal;
        UUID payloadUUID;

        public WinchContraptionControlPacket(WinchContraptionEntity entity) {
            this.entityId = entity.getId();
            this.winchSpeed = entity.winchSpeed;
            this.ropeInsertOffset = entity.ropeInsertOffset;
            this.tetherOffset = entity.tetherOffset;
            this.winchOrigin = entity.winchOriginOffset;
            this.stateOrdinal = entity.winchState.ordinal();
            this.payloadUUID = entity.payloadUUID;
        }

        public WinchContraptionControlPacket(FriendlyByteBuf buffer) {
            this.entityId = buffer.readInt();
            this.winchSpeed = buffer.readDouble();
            this.ropeInsertOffset = buffer.readDouble();
            this.tetherOffset = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            this.winchOrigin = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            this.stateOrdinal = buffer.readInt();
            if (buffer.readBoolean()) {
                this.payloadUUID = buffer.readUUID();
            }
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
            buffer.writeDouble(winchSpeed);
            buffer.writeDouble(ropeInsertOffset);
            buffer.writeDouble(tetherOffset.x);
            buffer.writeDouble(tetherOffset.y);
            buffer.writeDouble(tetherOffset.z);
            buffer.writeDouble(winchOrigin.x);
            buffer.writeDouble(winchOrigin.y);
            buffer.writeDouble(winchOrigin.z);
            buffer.writeInt(stateOrdinal);
            boolean hasPayload = this.payloadUUID != null;
            buffer.writeBoolean(hasPayload);
            if (hasPayload) {
                buffer.writeUUID(payloadUUID);
            }
        }

        public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level != null && level.getEntity(entityId) instanceof WinchContraptionEntity wce) {
                    wce.winchSpeed = this.winchSpeed;
                    wce.ropeInsertOffset = this.ropeInsertOffset;
                    wce.prevTetherOffset = wce.tetherOffset;
                    wce.tetherOffset = this.tetherOffset;
                    wce.winchOriginOffset = this.winchOrigin;
                    wce.winchState = WinchState.values()[this.stateOrdinal];
                    Entity oldPayload = wce.getPayloadEntity();
                    if (this.payloadUUID != null) {
                        if (!Objects.equals(wce.payloadUUID, this.payloadUUID) && oldPayload != null) {
                            wce.payloadUUID = this.payloadUUID;
                            wce.cachedPayloadEntity = null;
                        }
                    } else {
                        wce.payloadUUID = null;
                        wce.cachedPayloadEntity = null; // release cached value
                    }
                }
            });
            return true;
        }
    }
}
