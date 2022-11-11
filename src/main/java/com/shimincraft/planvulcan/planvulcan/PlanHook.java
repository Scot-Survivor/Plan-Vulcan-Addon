package com.shimincraft.planvulcan.planvulcan;

import com.djrapitops.plan.capability.CapabilityService;
import com.djrapitops.plan.extension.ExtensionService;
import me.frep.vulcan.api.VulcanAPI;

import static org.bukkit.Bukkit.getLogger;

public class PlanHook {
    private final VulcanAPI vulcanAPI;
    private final VulcanStorage storage;

    public PlanHook(VulcanAPI vulcanAPI, VulcanStorage storage) {
        this.vulcanAPI = vulcanAPI;
        this.storage = storage;
    }

    public void hookIntoPlan() {
        if (!areAllCapabilitiesAvailable()) {
            getLogger().warning("PlanVulcan may not be compatible with your Plan version.");
        }
        registerDataExtension();
    }

    private boolean areAllCapabilitiesAvailable() {
        CapabilityService capabilities = CapabilityService.getInstance();
        return capabilities.hasCapability("DATA_EXTENSION_VALUES");
    }

    private void registerDataExtension() {
        try {
            ExtensionService.getInstance().register(new VulcanData(storage));
        } catch (IllegalStateException planIsNotEnabled) {
            // Plan is not enabled, handle exception
        }
    }
}
