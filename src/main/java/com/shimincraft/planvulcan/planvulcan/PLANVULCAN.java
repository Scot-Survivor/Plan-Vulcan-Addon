package com.shimincraft.planvulcan.planvulcan;

import me.frep.vulcan.api.VulcanAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PLANVULCAN extends JavaPlugin {
    private VulcanAPI vulcanAPI;
    private VulcanStorage storage;

    private Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        vulcanAPI = VulcanAPI.Factory.getApi();
        storage = new VulcanStorage();
        plugin = Bukkit.getPluginManager().getPlugin("vulcan-plan-addon");
        try {
            new PlanHook(vulcanAPI, storage).hookIntoPlan();
            getLogger().info("PlanVulcan is alive");
        } catch (NoClassDefFoundError planIsNotInstalled) {
            // Plan is not installed
        }
        this.getServer().getPluginManager().registerEvents(new VulcanListener(storage, plugin), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
