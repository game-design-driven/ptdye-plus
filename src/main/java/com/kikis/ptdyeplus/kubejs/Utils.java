package com.kikis.ptdyeplus.kubejs;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Utils {

    /*
     * Reads the options.txt file in .minecraft directory and returns in a JSON format
     *
     * @returns String in JSON format
     * @throws IOException
     */
    public String getOptions() throws IOException {

        // todo: check if file exists
        BufferedReader reader = new BufferedReader(new FileReader("options.txt"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{"); // Start json

        String line = null;
        String ls = System.lineSeparator();

        while ((line = reader.readLine()) != null) {

            line = line.replaceAll("\"", "'");

            String builtLine = "";
            String[] split = line.split(":");


            if(split.length < 2){
                continue;
            }

            // key
            builtLine += '"';
            builtLine += split[0];
            builtLine += '"';
            builtLine += ':';

            // value
            builtLine += '"';
            builtLine += split[1];
            builtLine += '"';
            builtLine += ',';

            stringBuilder.append(builtLine);
            stringBuilder.append(ls);
        }

        // delete line separator and comma
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");

        reader.close();

        return stringBuilder.toString();
    }

    private void message(String message){
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable(message));
    }

    /*
        Get key by key code

        Key codes not working:
        key.smoothCamera
        key.spectatorOutlines


        List of key codes:
        key.forward
        key.left
        key.back
        key.right
        key.jump
        key.sneak
        key.sprint
        key.drop
        key.inventory
        key.chat
        key.playerlist
        key.command
        key.socialInteractions
        key.screenshot
        key.togglePerspective
        key.fullscreen
        key.spectatorOutlines
        key.swapOffhand
        key.saveToolbarActivator
        key.loadToolbarActivator
        key.advancements
        key.hotbar.1
        key.hotbar.2
        key.hotbar.3
        key.hotbar.4
        key.hotbar.5
        key.hotbar.6
        key.hotbar.7
        key.hotbar.8
        key.hotbar.9
        Try open ponder on targeted block
        key.jade.config
        key.jade.show_overlay
        key.jade.toggle_liquid
        key.jade.narrate
        key.jade.show_details
        create.keyinfo.toolmenu
        create.keyinfo.toolbelt
     */
    public String getKey(String keyCode) throws java.lang.IllegalStateException {

        int keyNumericalValue = 0;
        for (KeyMapping keyMapping : getKeyMappings()) {
            if( Objects.equals(keyMapping.getName(), keyCode) ) {
                keyNumericalValue = keyMapping.getKey().getValue();
            }
        }

        if(keyNumericalValue != 0){
            int scancode = GLFW.glfwGetKeyScancode(keyNumericalValue);
            return "{\"key\": \"" + GLFW.glfwGetKeyName(keyNumericalValue, scancode) + "\"}";
        }

        return "{\"error\": \"key not found\"}";
    }

    private void generateKeyCodesFile() throws IOException {
        ArrayList<String> keymappings = new ArrayList<>();
        for (KeyMapping keyMapping : getKeyMappings()) {
            keymappings.add(keyMapping.getName());
        }
        FileWriter fileWriter = new FileWriter("keyMappings.txt");
        for (String str : keymappings) {
            fileWriter.write(str + System.lineSeparator());
        }
        fileWriter.close();
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
