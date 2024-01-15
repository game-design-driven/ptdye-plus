package com.kikis.ptdyeplus.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public final class PtdyeEvents {
    public static EventGroup GROUP = EventGroup.of("PtdyeEvents");
    public static EventHandler SETTINGS = GROUP.server("settings", () -> CustomEvent.class);
}
