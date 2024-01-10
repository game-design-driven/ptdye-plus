package com.kikis.ptdyeplus.block.entity;

import com.kikis.ptdyeplus.block.property.fullness;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class BaseContainerEntity extends BaseContainerBlockEntity {
    protected NonNullList<ItemStack> items;
    protected ContainerOpenersCounter openersCounter;

    public final int width;
    public final int height;
    public final int size;
    private final String translationKey;

    public BaseContainerEntity(BlockPos blockPos, BlockState blockState, int width, int height, BlockEntityType entityType, String translationKey) throws AssertionError {
        super(entityType, blockPos, blockState);
        // For Width and Height see BaseContainerEntity.createMenu
        this.width = width;
        if (width != 9)
            throw new AssertionError("Width of container must be 9");
        this.height = height;
        if (height < 1 || height > 6)
            throw new AssertionError("Height of container must be between 1 and 6");
        this.size = width * height;
        this.translationKey = translationKey;
        this.items = NonNullList.withSize(this.size, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
                BaseContainerEntity.this.playSound(SoundEvents.BARREL_OPEN);
            }

            @Override
            protected void onClose(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
                BaseContainerEntity.this.playSound(SoundEvents.BARREL_CLOSE);
            }

            @Override
            protected void openerCountChanged(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, int i, int i1) { }

            @Override
            protected boolean isOwnContainer(@NotNull Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu) player.containerMenu).getContainer();
                    return container == BaseContainerEntity.this;
                }
                return false;
            }
        };
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable(this.translationKey);
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        MenuType<ChestMenu> type = switch (this.height) {
            case 1 -> MenuType.GENERIC_9x1;
            default -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            case 6 -> MenuType.GENERIC_9x6;
        };
        return new ChestMenu(type, i, inventory, this, this.height);
    }

    @Override
    public int getContainerSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.items, slot, amount);
        if (stack.isEmpty())
            this.setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack itemStack) {
        this.items.set(slot, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= Math.pow(30, 2);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.items);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, this.items);
    }

    void playSound(SoundEvent soundEvent) {
        double x = this.worldPosition.getX() + 0.5;
        double y = this.worldPosition.getY() + 0.5;
        double z = this.worldPosition.getZ() + 0.5;
        if (this.level != null)
            this.level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(@NotNull Player player) {
        if (!this.remove && !player.isSpectator() && this.level != null)
            this.openersCounter.incrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
    }

    @Override
    public void stopOpen(@NotNull Player player) {
        if (!this.remove && !player.isSpectator() && this.level != null)
            this.openersCounter.decrementOpeners(player, this.level, this.getBlockPos(), this.getBlockState());
    }

    public void recheckOpen() {
        if(!this.remove && this.level != null) {
            this.openersCounter.recheckOpeners(this.level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.updateBlockState();
    }

    public void updateBlockState() {
        double currentSlotsUsed = IntStream.range(0, this.size).mapToObj(i -> this.items.get(i)).filter(itemStack -> {
            if(itemStack.isEmpty())
                return false;
            return ((double) itemStack.getCount() / itemStack.getMaxStackSize()) >= 0.8;
        }).count();
        int state = (int) ((currentSlotsUsed / (double) this.size) * (double) fullness.MAX);
        if (state > fullness.MAX)
            state = fullness.MAX;
        if(this.level != null)
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(fullness.PROPERTY, state), 3);
    }

    public int getItemCount(@Nullable ItemStack filter) {
        Map<@Nullable Item, Integer> itemCounter = new HashMap<>();
        for (int i = 0; i < this.getContainerSize(); i++) {
            Item item = this.items.get(i).getItem();
            if (itemCounter.containsKey(item)) {
                itemCounter.put(item, itemCounter.get(item) + 1);
            } else {
                itemCounter.put(item, 1);
            }
        }
        if (filter != null)
            return itemCounter.getOrDefault(filter.getItem(), 0);
        int size = itemCounter.size();
        return itemCounter.containsKey(ItemStack.EMPTY.getItem()) ? size - 1 : size;
    }
}
