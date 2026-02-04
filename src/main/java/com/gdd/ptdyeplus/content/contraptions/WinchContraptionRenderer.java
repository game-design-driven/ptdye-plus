package com.gdd.ptdyeplus.content.contraptions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

public class WinchContraptionRenderer extends ContraptionEntityRenderer<WinchContraptionEntity> {
    public WinchContraptionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WinchContraptionEntity winch, float entityYaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
        super.render(winch, entityYaw, partialTicks, ms, buffers, light);

        Vec3 tether = winch.getTetherOffset(partialTicks);

        if (!winch.isWinchIdle() || tether.lengthSqr() > 1e-4) {
            renderRope(winch, tether, partialTicks, ms, buffers, light);
        }

        Entity payload = winch.getPayloadEntity();
        if (payload != null) {
            Vec3 smoothPos = winch.getAnchorVec().add(winch.getWinchOriginOffset()).add(tether);
            payload.setPos(smoothPos);
            payload.xo = smoothPos.x;
            payload.yo = smoothPos.y;
            payload.zo = smoothPos.z;
        }
    }

    private void renderRope(WinchContraptionEntity entity, Vec3 tether, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
        double tetherLength = Math.abs(tether.y);

        if (tetherLength < 1e-3) {
            return; // early exit
        }

        double startPadding = entity.ropeInsertOffset;

        ms.pushPose();

        Vec3 origin = entity.getWinchOriginOffset();
        ms.translate(origin.x, origin.y, origin.z);
        ms.translate(-0.5d, 1.0d + startPadding, -0.5d); // shift Rope to centre of contraption

        BlockState ropeState = AllBlocks.ROPE.getDefaultState();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        int segments = Math.max(0, Mth.ceil(tetherLength + startPadding) - 1);
        for (int i = 0; i < segments; i++) {
            dispatcher.renderSingleBlock(ropeState, ms, buffers, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            ms.translate(0.0f, -1.0f, 0.0f);
        }

        ms.popPose();
    }
}
