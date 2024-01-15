package com.kikis.ptdyeplus.kubejs.bindings;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class KeyUtilJS {

    /**
     *  Get key by key code
     *  @param keyCode example: "key.forward"
     *  @return the key or "key-not-found"
     */
    public String getKey(String keyCode) throws java.lang.IllegalStateException {
        return getKeyMappings().getOrDefault(keyCode, "key-not-found");
    }

    private static Map<String, String> getKeyMappings() {
        return Arrays.stream(Minecraft.getInstance().options.keyMappings).collect(Collectors.toMap(KeyMapping::getName, km -> km.getKey().getDisplayName().getString()));
    }

}
