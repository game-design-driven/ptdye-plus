package com.kikis.ptdyeplus.integration;

import com.simibubi.create.content.contraptions.actors.AttachedActorBlock;
import com.simibubi.create.content.fluids.drain.ItemDrainBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class PonderTooltipPlugin implements IWailaPlugin {

    public static final String ID = "pondertooltip";
    public static final ResourceLocation UID = new ResourceLocation(ID, "text");

    @Override
    public void registerClient(IWailaClientRegistration registration) {

        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, KineticBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, AttachedActorBlock.class);

        // Fluid Manipulators
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, FluidPipeBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, DirectionalAxisKineticBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, DirectionalKineticBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, FaceAttachedHorizontalDirectionalBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, HorizontalKineticBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, ItemDrainBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, SpoutBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, WrenchableDirectionalBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, FluidTankBlock.class);
        registration.registerBlockComponent(PonderTooltipComponentProvider.INSTANCE, FluidTankBlock.class);

        // todo: find way to get the class of every block with a ponder
        // todo: finish registering all blocks with ponder

    }
}