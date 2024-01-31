package com.kikis.ptdyeplus.jade;

import com.kikis.ptdyeplus.commands.OpenStonecutter;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PonderTooltipComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final Lazy<KeyMapping> KEY_TRY_PONDER = Lazy.of(() -> new KeyMapping(
            "key.ptdyeplus.try_ponder",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.ptdyeplus.category"
    ));

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(KEY_TRY_PONDER.get());
    }

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        String tryPonderKey = KEY_TRY_PONDER.get().getKey().toString();
        // todo: figure out how to do translation with chat colour
        tooltip.add(Component.translatable(ChatFormatting.DARK_GRAY + "Press [%s" + ChatFormatting.DARK_GRAY + "] to ponder", ChatFormatting.GRAY + tryPonderKey.substring(tryPonderKey.length() - 1)));
    }

    @Override
    public ResourceLocation getUid() {
        return PonderTooltipPlugin.UID;
    }
}