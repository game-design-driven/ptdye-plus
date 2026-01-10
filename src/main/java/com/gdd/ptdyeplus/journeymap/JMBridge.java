package com.gdd.ptdyeplus.journeymap;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.features.territories.client.TerritoryNetworkHandler;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.event.ClientEvent;

@ClientPlugin
@SuppressWarnings("unused")
public class JMBridge implements IClientPlugin {
    private static IClientAPI jmAPI = null;

    public static IClientAPI getAPI() {
        return jmAPI;
    }

    @Override
    public void initialize(IClientAPI jmAPI) {
        this.jmAPI = jmAPI;

        TerritoryNetworkHandler.onAPIReady(jmAPI);
    }

    @Override
    public String getModId() {
        return PTDyePlus.ID;
    }

    @Override
    public void onEvent(ClientEvent clientEvent) {}
}
