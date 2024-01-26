package com.kikis.ptdyeplus.kubejs;

import com.kikis.ptdyeplus.kubejs.bindings.PtdyeBindings;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class PtdyeJS extends KubeJSPlugin {
    static PtdyeJS $this;

    @Override
    public void init() {
        $this = this;
    }

    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("Ptdye", PtdyeBindings.class);
    }
}