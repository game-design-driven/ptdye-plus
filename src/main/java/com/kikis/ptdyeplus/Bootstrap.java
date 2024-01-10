package com.kikis.ptdyeplus;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod("ptdyeplus")
public class Bootstrap {
    public Bootstrap() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PtdyePlus::new);
    }
}