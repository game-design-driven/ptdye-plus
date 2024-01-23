package com.kikis.ptdyeplus.integration.jade;

import com.kikis.ptdyeplus.commands.stonecutter.KeyBinding;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PonderTooltipComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        String tryPonderKey = KeyBinding.TRY_PONDER_KEY.getKey().toString();
        // todo: figure out how to do translation with chat colour
        tooltip.add(Component.translatable(ChatFormatting.DARK_GRAY + "Press [%s" + ChatFormatting.DARK_GRAY + "] to ponder", ChatFormatting.GRAY + tryPonderKey.substring(tryPonderKey.length() - 1)));
    }


    @Override
    public ResourceLocation getUid() {
        return PonderTooltipPlugin.UID;
    }
}