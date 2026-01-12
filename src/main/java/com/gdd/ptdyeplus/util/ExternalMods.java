package com.gdd.ptdyeplus.util;

import net.minecraftforge.fml.ModList;

public class ExternalMods {
    // This class is used to check if optional mods are present
    // Where `mandatory = false` within `resources/META-INF/mods.toml`

    public static final String JADE_ID = "jade";
    public static final String JOURNEYMAP_ID = "journeymap";

    public static boolean isModPresent(String id) {
        return ModList.get().isLoaded(id);
    }

    public static boolean isJadePresent() {
        return isModPresent(JADE_ID);
    }

    public static boolean isJourneyMapPresent() {
        return isModPresent(JOURNEYMAP_ID);
    }
}
