package com.kikis.ptdyeplus.kubejs;

import com.kikis.ptdyeplus.OpenStonecutter;
import com.kikis.ptdyeplus.PtdyePlus;
import com.kikis.ptdyeplus.integration.PonderTooltipComponentProvider;
import com.kikis.ptdyeplus.integration.PonderTooltipPlugin;
import com.kikis.ptdyeplus.util.EntityContainerLevelAccess;
import com.kikis.ptdyeplus.util.KeyBinding;
import com.kikis.ptdyeplus.util.MinecraftMenuBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class PtdyeJS extends KubeJSPlugin {

    static PtdyeJS $this;
    EventGroup GROUP = EventGroup.of("PtdyeEvents");
    EventHandler SETTINGS = GROUP.server("settings", () -> CustomEvent.class);


    @Override
    public void init() {
        $this = this;
    }

    public static PtdyeJS getInstance() {
        return $this;
    }

    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }

//    @Override
//    public void onServerReload() {
//        SETTINGS.post(new CustomEvent());
//        PtdyeJS.getInstance().SETTINGS.post(new SettingsEventJS());
//    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("Ptdye", BaseBindings.class);
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