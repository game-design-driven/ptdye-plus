package com.kikis.ptdyeplus.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_PTDYE = "Create: Prepare to Dye";
    public static final String KEY_TRY_PONDER = "Try open ponder on targeted block";

    public static final KeyMapping TRY_PONDER_KEY = new KeyMapping(KEY_TRY_PONDER, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_PTDYE);
}