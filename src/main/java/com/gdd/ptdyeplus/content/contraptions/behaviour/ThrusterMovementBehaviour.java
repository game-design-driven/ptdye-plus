package com.gdd.ptdyeplus.content.contraptions.behaviour;

import com.gdd.ptdyeplus.content.block.ThrusterBlock;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;


public class ThrusterMovementBehaviour implements MovementBehaviour {

    private static class ThrusterState {
        public Vec3 lastPosition;
        public Vec3 lastExhaustDirection;

        public ThrusterState(Vec3 position, Vec3 direction) {
            this.lastPosition = position;
            this.lastExhaustDirection = direction;
        }
    }

    @Override
    public void tick(MovementContext context) {
        if (!context.world.isClientSide) return;

        Direction localFacing = context.state.getValue(ThrusterBlock.FACING);
        Vec3 localDirVec = new Vec3(localFacing.getStepX(), localFacing.getStepY(), localFacing.getStepZ());

        // Offset relative to block center
        Vec3 localNozzle = new Vec3(0.0d, 0.15d, 0.0d).add(localDirVec.scale(0.45d));

        Vec3 currentExhaustDirection = context.rotation.apply(localDirVec);
        Vec3 rotatedNozzleOffset = context.rotation.apply(localNozzle);

        Vec3 nozzlePosition = context.position.add(rotatedNozzleOffset);

        RandomSource random = context.world.random;

        if (random.nextFloat() < 0.2f) // Extra gate keeping, as this method is called a lot more often than animate tick
            ThrusterBlock.playSound(context.world, nozzlePosition, random);

        if (!(context.temporaryData instanceof ThrusterState state)) {
            context.temporaryData = new ThrusterState(nozzlePosition, currentExhaustDirection);
            return; // skip first tick
        }

        // Checked after playSound if we should spawn particles
        if (random.nextFloat() > 0.90f)
            return;

        Vec3 totalInertia = nozzlePosition.subtract(state.lastPosition);
        state.lastPosition = nozzlePosition;
        state.lastExhaustDirection = currentExhaustDirection;

        float speed = (float) totalInertia.length();
        float threshold = 0.15f + (float) (Math.sqrt(speed) * 0.60f);
        if (random.nextFloat() > Math.min(threshold, 0.90f))
            return; // spawn particles based on speed

        ThrusterBlock.spawnThrusterParticles(
            context.world,
            nozzlePosition,
            currentExhaustDirection,
            totalInertia,
            random
        );
    }
}
