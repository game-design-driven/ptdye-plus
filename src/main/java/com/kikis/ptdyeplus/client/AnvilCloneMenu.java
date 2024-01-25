package com.kikis.ptdyeplus.client;

import com.kikis.ptdyeplus.block.AnvilCloneBlock;
import com.kikis.ptdyeplus.mixin.vanilla.AnvilMenuAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.apache.commons.lang3.StringUtils;

public class AnvilCloneMenu extends AnvilMenu {
    private final DataSlot cost_ref;
    private final AnvilMenuAccessor accessor;

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.getBlock() instanceof AnvilCloneBlock;
    }

    @Override
    protected boolean mayPickup(Player player, boolean bool) {
        return true;
    }

    // Basically cloned from super.
    @Override
    protected void onTake(Player player, ItemStack out) {
        float breakChance = ForgeHooks.onAnvilRepair(player, out, inputSlots.getItem(0), inputSlots.getItem(1));

        // This syntax differs because I subjectively think it's better.
        // Could just as easily be changed back with no effect.
        removeItems: {
            this.inputSlots.setItem(0, ItemStack.EMPTY);
            if (this.repairItemCountCost > 0) {
                var secondItem = this.inputSlots.getItem(1);
                if (!secondItem.isEmpty() && secondItem.getCount() > this.repairItemCountCost) {
                    secondItem.shrink(this.repairItemCountCost);
                    this.inputSlots.setItem(1, secondItem);
                    break removeItems;
                }
            }
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost_ref.set(0);
        this.repairItemCountCost = 0; // maybe unneeded, super doesn't do this, but I think it makes sense to do here.

        // same situation as above, but with a return instead of a named block.
        this.access.execute((level, pos) -> {
            var state = level.getBlockState(pos);
            if (AnvilCloneBlock.TAKES_DAMAGE && (!player.getAbilities().instabuild && isValidBlock(state) && player.getRandom().nextFloat() < breakChance)) {
                var damagedState = AnvilCloneBlock.damage(state);
                if (damagedState == null) {
                    level.removeBlock(pos, false);
                    return;
                } else {
                    level.setBlock(pos, damagedState, 2);
                }
            }
            level.levelEvent(1030, pos, 0);
        });
    }

    @Override
    public void createResult() {
        var first = this.inputSlots.getItem(0);

        // Set cost to 0. Literally should never matter, but better safe than sorry.
        this.cost_ref.set(0);
        this.repairItemCountCost = 0;

        // Check that we actually have an item
        if (first.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }
        var second = this.inputSlots.getItem(1);

        // ForgeHook to cancel things, allow it to try to not hurt other functionality.
        if (!ForgeHooks.onAnvilChange(this, first, second, resultSlots, accessor._getItemName(), first.getBaseRepairCost() + (second.isEmpty() ? 0 : second.getBaseRepairCost()), player)) return;

        // subject to change if anvil ever wants to be used for more then just repairing. Logic is implemented in onTake anyway.
        if (!second.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        var result = first.copy();

        // renaming bit
        var itemName = this.accessor._getItemName();
        if (StringUtils.isBlank(itemName)) {
            if (first.hasCustomHoverName()) result.resetHoverName();
        } else if (!itemName.equals(first.getHoverName().getString())) {
            result.setHoverName(Component.literal(itemName));
        }

        // cleanup
        this.resultSlots.setItem(0, result);
        this.broadcastChanges();
    }

    // For some reason it did not work until I explicitly overrode it with a call to super
    @Override
    public void setItemName(String str) {
        super.setItemName(str);
    }

    // Copied from super instead of calling it so changes to constructors only need to be made in one place
    public AnvilCloneMenu(int n, Inventory inv) { this(n, inv, ContainerLevelAccess.NULL); }
    public AnvilCloneMenu(int p_39008_, Inventory p_39009_, ContainerLevelAccess p_39010_) {
        super(p_39008_, p_39009_, p_39010_);
        accessor = (AnvilMenuAccessor) this;
        cost_ref = accessor._getCost();
    }
}
