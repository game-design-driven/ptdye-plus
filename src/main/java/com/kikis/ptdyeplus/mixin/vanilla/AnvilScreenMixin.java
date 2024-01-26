package com.kikis.ptdyeplus.mixin.vanilla;

import com.kikis.ptdyeplus.block.menu.AnvilCloneMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.ponder.PonderChapterRegistry;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// CLIENT
@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin<T extends ItemCombinerMenu> extends ItemCombinerScreen<T> {
    private static final String REPLACE_EMPTY = " ";

    @Inject(method = "renderLabels", at = @At("HEAD"), cancellable = true)
    private void dontRenderLabels(PoseStack pose, int n1, int n2, CallbackInfo ci) {
        if (menu instanceof AnvilCloneMenu) ci.cancel();
    }

    @Inject(method = "onNameChanged", at = @At("HEAD"), cancellable = true)
    private void defaultNameOnEmpty(String str, CallbackInfo ci) {
        if (!(menu instanceof AnvilCloneMenu cloneMenu && str.isEmpty())) return;

        var slot0 = cloneMenu.getSlot(0);
        if (slot0.hasItem() && slot0.getItem().hasCustomHoverName()) {
            cloneMenu.setItemName(REPLACE_EMPTY);
            minecraft.player.connection.send(new ServerboundRenameItemPacket(REPLACE_EMPTY));
        }
        ci.cancel();
    }

    // Ignored by mixin; required by java
    public AnvilScreenMixin(T p_98901_, Inventory p_98902_, Component p_98903_, ResourceLocation p_98904_) {
        super(p_98901_, p_98902_, p_98903_, p_98904_);
    }
}
