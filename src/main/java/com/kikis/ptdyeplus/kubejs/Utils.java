package com.kikis.ptdyeplus.kubejs;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.Objects;

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

}
