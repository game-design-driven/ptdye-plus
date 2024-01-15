package com.kikis.ptdyeplus.kubejs;

import com.kikis.ptdyeplus.OpenStonecutter;
import com.kikis.ptdyeplus.PtdyePlus;
import com.kikis.ptdyeplus.integration.PonderTooltipComponentProvider;
import com.kikis.ptdyeplus.integration.PonderTooltipPlugin;
import com.kikis.ptdyeplus.kubejs.bindings.PtdyeBindings;
import com.kikis.ptdyeplus.kubejs.events.CustomEvent;
import com.kikis.ptdyeplus.kubejs.events.PtdyeEvents;
import com.kikis.ptdyeplus.stonecutter.EntityContainerLevelAccess;
import com.kikis.ptdyeplus.stonecutter.KeyBinding;
import com.kikis.ptdyeplus.stonecutter.MinecraftMenuBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class PtdyeJS extends KubeJSPlugin {
    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void registerEvents() {
        PtdyeEvents.GROUP.register();
    }

//    @Override
//    public void onServerReload() {
//        PtdyeEvents.SETTINGS.post(new CustomEvent());
//        PtdyeEvents.SETTINGS.post(new SettingsEventJS());
//    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("Ptdye", PtdyeBindings.class);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.deny(PtdyePlus.class);
        filter.deny(CustomEvent.class);
        filter.deny(OpenStonecutter.class);
        filter.deny(EntityContainerLevelAccess.class);
        filter.deny(KeyBinding.class);
        filter.deny(MinecraftMenuBuilder.class);
        filter.deny(PonderTooltipComponentProvider.class);
        filter.deny(PonderTooltipPlugin.class);
    }
}