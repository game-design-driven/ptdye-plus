package com.gdd.ptdyeplus.features;

import com.mojang.blaze3d.platform.InputConstants;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

public class Ponder {
    public static final KeyMapping KEY_PONDER = new KeyMapping(
        "key.ptdyeplus.ponder",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        "key.ptdyeplus.category"
    );

    // Progress State
    private static float internalProgress = 0f; // 0.0 to 1.0
    private static long lastFrameTime = System.currentTimeMillis();
    private static BlockPos lastHoveredPos = null;

    // Config
    private static final float SECONDS_TO_OPEN = 0.7f; // Time taken to open the Ponder
    private static final float DECAY_SPEED = 0.8f; // Progress lost per second
    private static final int BAR_LENGTH = 46; // This is the number that Create uses for its progress bar. Maybe a reference to tnt?
    private static final float CEILING = 0.90f; // The bar will look "full" when internalProgress hits 0.95

    private static final String FULL_BAR_STR = ("|").repeat(BAR_LENGTH);

    public static void onRenderTick(Minecraft mc, RenderGuiEvent.Post event) {
        if (mc.screen == null) {
            long currentTime = System.currentTimeMillis();
            float deltaTime = (currentTime - lastFrameTime) / 1000f;
            lastFrameTime = currentTime;

            updateProgressBar(mc, deltaTime);

            if (internalProgress > 0f) {
                renderProgressBar(mc, event);
            }
        } else {
            internalProgress = 0f;
        }
    }

    private static void updateProgressBar(Minecraft mc, float deltaTime) {
        if (mc.level == null || mc.player == null)
            return;

        Entity cameraEntity = mc.getCameraEntity();
        if (cameraEntity == null)
            return;

        boolean isHoldingKey = KEY_PONDER.isDown();
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
    }

    private static void renderProgressBar(Minecraft mc, RenderGuiEvent.Post event) {
        float bufferedProgress = Math.min(1.0f, internalProgress / CEILING);
        float visualProgress = (bufferedProgress < 0.5f)
            ? bufferedProgress
            : 0.5f + 2.0f * (float) Math.pow(bufferedProgress - 0.5f, 2);

        int filled = Math.max(0, Math.min(BAR_LENGTH, (int) (visualProgress * BAR_LENGTH)));

        Component barComponent = Component.empty()
            .append(Component.literal(FULL_BAR_STR.substring(0, filled)).withStyle(ChatFormatting.GRAY))
            .append(Component.literal(FULL_BAR_STR.substring(filled)).withStyle(ChatFormatting.DARK_GRAY));

        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();
        int y = screenHeight - 50;

        event.getGuiGraphics().drawCenteredString(mc.font, barComponent, screenWidth / 2, y, 0xFFFFFF);
    }
}
