package com.gdd.ptdyeplus.client.renderer;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.entity.AnchorEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AnchorRenderer extends EntityRenderer<AnchorEntity> {
    public AnchorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AnchorEntity entity) {
        // We want to render nothing, return empty resource location
        return ResourceLocation.fromNamespaceAndPath(PTDyePlus.ID, "NULL");
    }
}
