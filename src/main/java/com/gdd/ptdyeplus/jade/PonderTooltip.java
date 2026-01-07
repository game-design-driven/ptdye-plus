package com.gdd.ptdyeplus.jade;

import com.gdd.ptdyeplus.features.Ponder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@SuppressWarnings("unused")
public final class PonderTooltip {
    public static final String ID = "pondertooltip";

    @WailaPlugin
    public static class Plugin implements IWailaPlugin {
        public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ID, "text");

        @Override
        public void registerClient(IWailaClientRegistration registration) {
            registration.registerBlockComponent(ComponentProvider.INSTANCE, Block.class);
        }
    }

    public enum ComponentProvider implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            // This function is ran for every block; early exit as soon as possible
            ResourceLocation blockID = ForgeRegistries.BLOCKS.getKey(accessor.getBlock());

            if (!PonderIndex.getSceneAccess().doScenesExistForId(blockID))
                return;

            Component keyComponent = Ponder.KEY_PONDER.getTranslatedKeyMessage()
                .copy()
                .withStyle(ChatFormatting.GRAY);

            tooltip.add(Component.translatable("ptdyeplus.pondertooltip.text", keyComponent)
                .withStyle(ChatFormatting.DARK_GRAY));
        }

        @Override
        public ResourceLocation getUid() {
            return Plugin.UID;
        }
    }
}
