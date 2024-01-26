package com.kikis.ptdyeplus.commands.stonecutter;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public interface MinecraftMenuBuilder {
	AbstractContainerMenu create(int id, Inventory inventory, ContainerLevelAccess access);
}