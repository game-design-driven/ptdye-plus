package com.kikis.ptdyeplus.kubejs.bindings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;

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
        return Arrays.stream(Minecraft.getInstance().options.keyMappings).collect(Collectors.toMap(KeyMapping::getName, km -> getKeyRepresentation(km.getKey())));
    }

    /**
     * More or less copied from {@link InputConstants.Type#displayTextSupplier} for {@link InputConstants.Type#KEYSYM}
     */
    @SuppressWarnings("JavadocReference")
    private static String getKeyRepresentation(InputConstants.Key key) {
        var lang = Language.getInstance();
        var name = key.getName();
        if (lang.has(name)) return (lang.getOrDefault(name));
        // This will work *most* of the time, but really is just a bandaid fix.
        var split = name.split("\\.");
        return split[split.length - 1];
        // The following code doesn't work since GLFW must be called from the render thread.

        // var glfwKeyName = GLFW.glfwGetKeyName(key.getValue(), -1);
        // return glfwKeyName == null ? Language.getInstance().getOrDefault(key.getName()) : glfwKeyName;
    }
}
