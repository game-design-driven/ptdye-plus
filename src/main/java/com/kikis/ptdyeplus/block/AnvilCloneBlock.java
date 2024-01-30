package com.kikis.ptdyeplus.block;

import com.kikis.ptdyeplus.block.menu.AnvilCloneMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class AnvilCloneBlock extends AnvilBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("ptdyeplus.container.anvilClone");
    public static final boolean TAKES_DAMAGE = false;

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((n, inv, player) -> new AnvilCloneMenu(n, inv, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    public static BlockState damage(BlockState state) {
        throw new NotImplementedException("AnvilCloneBlock shouldn't take damage.");
    }

    public AnvilCloneBlock(Properties p_48777_) { super(p_48777_); }
}
