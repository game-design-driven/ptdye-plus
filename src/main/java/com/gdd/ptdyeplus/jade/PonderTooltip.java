package com.gdd.ptdyeplus.jade;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterBlock;
import com.simibubi.create.content.contraptions.actors.plough.PloughBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.contraptions.bearing.ClockworkBearingBlock;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlock;
import com.simibubi.create.content.contraptions.chassis.AbstractChassisBlock;
import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.elevator.ElevatorPulleyBlock;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;
import com.simibubi.create.content.decoration.TrainTrapdoorBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlock;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlock;
import com.simibubi.create.content.kinetics.turntable.TurntableBlock;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlock;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlock;
import com.simibubi.create.content.logistics.chute.ChuteBlock;
import com.simibubi.create.content.logistics.chute.SmartChuteBlock;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.EjectorBlock;
import com.simibubi.create.content.logistics.funnel.AndesiteFunnelBlock;
import com.simibubi.create.content.logistics.funnel.BrassFunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlock;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.content.redstone.diodes.PoweredLatchBlock;
import com.simibubi.create.content.redstone.diodes.ToggleLatchBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.redstone.smartObserver.SmartObserverBlock;
import com.simibubi.create.content.redstone.thresholdSwitch.ThresholdSwitchBlock;
import com.simibubi.create.content.trains.display.FlapDisplayBlock;
import com.simibubi.create.content.trains.observer.TrackObserverBlock;
import com.simibubi.create.content.trains.signal.SignalBlock;
import com.simibubi.create.content.trains.station.StationBlock;
import com.simibubi.create.content.trains.track.TrackBlock;
import net.createmod.catnip.gui.ScreenOpener;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public class PonderTooltip {
    public static final String ID = "pondertooltip";

    // Progress State
    private static float internalProgress = 0f; // 0.0 to 1.0
    private static long lastFrameTime = System.currentTimeMillis();
    private static BlockPos lastHoveredPos = null;

    // Config
    private static final float SECONDS_TO_OPEN = 0.7f; // Time taken to open the Ponder
    private static final float DECAY_SPEED = 0.8f; // Progress lost per second
    private static final int BAR_LENGTH = 46; // This is the number that Create uses for its progress bar. Maybe a reference to tnt?

    private static final String FULL_BAR_STR = ("|").repeat(BAR_LENGTH);

    @WailaPlugin
    public static class Plugin implements IWailaPlugin {
        public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ID, "text");

        @Override
        public void registerClient(IWailaClientRegistration registration) {
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ShaftBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, CogWheelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, GearshiftBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ChainDriveBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SequencedGearshiftBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SpeedControllerBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, HandCrankBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, WaterWheelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, LargeWaterWheelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, WindmillBearingBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SteamEngineBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ValveHandleBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, MillstoneBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, TurntableBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, EncasedFanBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, MechanicalPressBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, MechanicalMixerBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, MechanicalCrafterBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, DrillBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SawBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, DeployerBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PumpBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ArmBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, MechanicalPistonBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PulleyBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ElevatorPulleyBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, BearingBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, GantryCarriageBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, GantryShaftBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ClockworkBearingBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, FlapDisplayBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, CrushingWheelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, FluidPipeBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PumpBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, FluidValveBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SmartFluidPipeBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, HosePulleyBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ItemDrainBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SpoutBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PortableStorageInterfaceBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, FluidTankBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ChuteBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SmartChuteBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ItemVaultBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, DepotBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, EjectorBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, AndesiteFunnelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, BrassFunnelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, BeltTunnelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, BrassTunnelBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SmartObserverBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ThresholdSwitchBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, NixieTubeBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, RedstoneContactBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, AnalogLeverBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, RedstoneLinkBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, BrassDiodeBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PoweredLatchBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ToggleLatchBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, RoseQuartzLampBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, CartAssemblerBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, StationBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, HarvesterBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, PloughBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SeatBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, ControlsBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, AbstractChassisBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, StickerBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, TrackBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SignalBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, TrackObserverBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, SlidingDoorBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, TrainTrapdoorBlock.class);
            registration.registerBlockComponent(ComponentProvider.INSTANCE, CasingBlock.class);
        }
    }

    public enum ComponentProvider implements IBlockComponentProvider {
        INSTANCE;

        public static final Lazy<KeyMapping> KEY_PONDER = Lazy.of(() -> new KeyMapping(
            "key.ptdyeplus.ponder",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.ptdyeplus.category"
        ));

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            String ponderKey = KEY_PONDER.get().getKey().toString();
            String keyName = ponderKey.substring(ponderKey.length() - 1).toUpperCase();
            Component keyComponent = Component.literal(keyName)
                .withStyle(ChatFormatting.GRAY);

            tooltip.add(Component.translatable("ptdyeplus.pondertooltip.text", keyComponent)
                .withStyle(ChatFormatting.DARK_GRAY));
        }

        @Override
        public ResourceLocation getUid() {
            return Plugin.UID;
        }
    }

    public static void onRenderTick(Minecraft mc, RenderGuiEvent.Post event) {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastFrameTime) / 1000f;
        lastFrameTime = currentTime;

        if (mc.level == null || mc.player == null)
            return;

        Entity cameraEntity = mc.getCameraEntity();
        if (cameraEntity == null)
            return;

        boolean isHoldingKey = ComponentProvider.KEY_PONDER.get().isDown();
        boolean isFilling = false;

        if (isHoldingKey) {
            HitResult hit = cameraEntity.pick(20.0D, 0.0F, false);
            if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
                BlockPos currentPos = blockHit.getBlockPos();

                if (!currentPos.equals(lastHoveredPos)) {
                    internalProgress = 0f;
                    lastHoveredPos = currentPos;
                }

                BlockState state = mc.level.getBlockState(currentPos);
                ResourceLocation blockID = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                boolean hasPonder = PonderIndex.getSceneAccess().doScenesExistForId(blockID);
                if (hasPonder) {
                    isFilling = true;
                    internalProgress += deltaTime / SECONDS_TO_OPEN;

                    if (internalProgress >= 1.0f) {
                        internalProgress = 0f;

                        ScreenOpener.transitionTo(PonderUI.of(blockID));
                    }
                }
            }
        }

        if (!isFilling) {
            internalProgress -= deltaTime * DECAY_SPEED;
        }

        internalProgress = Math.max(0f, Math.min(1f, internalProgress));

        if (internalProgress > 0f) {
            renderProgressBar(mc, event, internalProgress);
        }
    }

    private static void renderProgressBar(Minecraft mc, RenderGuiEvent.Post event, float progress) {
        float visual = (progress < 0.5f) ? progress : 0.5f + 2.0f * (float) Math.pow(progress - 0.5f, 2);

        int filled = Math.max(0, Math.min(BAR_LENGTH, (int) (visual * BAR_LENGTH)));

        Component barComponent = Component.empty()
            .append(Component.literal(FULL_BAR_STR.substring(0, filled)).withStyle(ChatFormatting.GRAY))
            .append(Component.literal(FULL_BAR_STR.substring(filled)).withStyle(ChatFormatting.DARK_GRAY));

        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();
        int y = screenHeight - 50;

        event.getGuiGraphics().drawCenteredString(mc.font, barComponent, screenWidth / 2, y, 0xFFFFFF);
    }
}
