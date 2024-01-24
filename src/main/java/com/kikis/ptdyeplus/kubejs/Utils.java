package com.kikis.ptdyeplus.kubejs;

import com.kikis.ptdyeplus.PtdyePlus;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlockEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.GameData;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Utils {

    /**
     *  Get key by key code
     *  @param keyCode example: "key.forward"
     *  @return the key or "key-not-found"
     */
    public String getKey(String keyCode) throws java.lang.IllegalStateException {
        int keyNumericalValue = 0;
        for (KeyMapping keyMapping : getKeyMappings()) {
            if( Objects.equals(keyMapping.getName(), keyCode)) {
                if ( Objects.equals("key.keyboard.unknown", keyMapping.getKey().getName()) ) {
                    break;
                }
                keyNumericalValue = keyMapping.getKey().getValue();
            }
        }

        if(keyNumericalValue != 0){
            int scancode = GLFW.glfwGetKeyScancode(keyNumericalValue);
            return GLFW.glfwGetKeyName(keyNumericalValue, scancode);
        }

        return "key-not-found";
    }

    private static ArrayList<KeyMapping> getKeyMappings() {
        ArrayList<KeyMapping> mappings = new ArrayList<>();

        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            // Don't include if keymapping is on the mouse
            if(keyMapping.getKey().getName().contains(".mouse.")){
                continue;
            }

            mappings.add(keyMapping);
        }

        return mappings;
    }

    /**
     * Creates a contraption nbt from given block positions
     * @param level The level of the given block positions are in
     * @param blockPositions The list of block positions to add to the contraption. In world space; i.e. not locale space
     * @param anchor The position the contraption should move around. In world space; i.e. not locale space
     * @return Tag for the created contraption
     */
    public CompoundTag createContraptionNBTFrom(Level level, Set<BlockPos> blockPositions, BlockPos anchor, AABB bounds) {
        CompoundTag contraptionNbt = new CompoundTag();
        contraptionNbt.putString("Type", "mounted");
        contraptionNbt.put("Blocks", createBlockCompound(level, blockPositions, anchor));
        contraptionNbt.put("Anchor", NbtUtils.writeBlockPos(new BlockPos(0, 0, 0)));
        contraptionNbt.put("BoundsFront", NBTHelper.writeAABB(bounds));
        NBTHelper.writeEnum(contraptionNbt, "RotationMode", CartAssemblerBlockEntity.CartMovementMode.ROTATION_LOCKED);

        // defaults
        contraptionNbt.put("Actors", new ListTag());
        contraptionNbt.putBoolean("BottomlessSupply", false);
        contraptionNbt.put("DisabledActors", new ListTag());
        contraptionNbt.put("FluidStorage", new ListTag());
        contraptionNbt.put("Interactors", new ListTag());
        contraptionNbt.put("Passengers", new ListTag());
        contraptionNbt.put("Seats", new ListTag());
        contraptionNbt.putBoolean("Stalled", false);
        contraptionNbt.put("Storage", new ListTag());
        contraptionNbt.put("SubContraptions", new ListTag());
        contraptionNbt.put("Superglue", new ListTag());
        return contraptionNbt;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static CompoundTag createBlockCompound(Level level, Set<BlockPos> blockPositions, BlockPos anchor) {
        HashMapPalette<BlockState> palette = new HashMapPalette<>(GameData.getBlockStateIDMap(), 16,(i, s) -> {
            throw new IllegalStateException("Palette Map index exceeded maximum");
        });
        ListTag blockList = new ListTag();

        for (BlockPos blockPos : blockPositions) {
            BlockState state = level.getBlockState(blockPos);
            int id = palette.idFor(state);
            CompoundTag c = new CompoundTag();
            c.putLong("Pos", (blockPos.subtract(anchor)).asLong());
            c.putInt("State", id);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity != null) {
                c.put("Data", blockEntity.saveWithoutMetadata());
            }
            blockList.add(c);
        }

        ListTag paletteNBT = new ListTag();
        for (BlockState blockState : palette.getEntries())
            paletteNBT.add(NbtUtils.writeBlockState(blockState));

        CompoundTag compound = new CompoundTag();
        compound.put("Palette", paletteNBT);
        compound.put("BlockList", blockList);

        return compound;
    }

    /**
     * Creates directory if it doesn't already exist
     * @param directoryPath Target directory to create
     */
    public void createDirectory(String directoryPath) {
        try {
            Files.createDirectory(Paths.get(directoryPath));
        } catch (IOException e) {
            PtdyePlus.LOGGER.warn("Could not create directory. Could it already exist? Directory: {}", directoryPath);
        }
    }

}
