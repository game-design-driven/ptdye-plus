package com.kikis.ptdyeplus.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class PtdyeJS extends KubeJSPlugin {
    static PtdyeJS $this;
    EventGroup GROUP = EventGroup.of("PtdyeEvents");

    @Override
    public void init() {
        $this = this;
    }

    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("Ptdye", BaseBindings.class);
    }
}