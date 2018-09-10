package me.glaremasters.mergedrops;

import me.glaremasters.mergedrops.events.MobDeathEvent;
import me.glaremasters.mergedrops.updater.SpigotUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mergedrops extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new MobDeathEvent(this), this);
        if (getConfig().getBoolean("updater.check")) {
            SpigotUpdater updater = new SpigotUpdater(this, 52881);
            try {
                if (updater.checkForUpdates()) {
                    getLogger()
                            .warning(
                                    "You appear to be running a version other than our latest stable release."
                                            + " You can download our newest version at: " + updater
                                            .getResourceURL());
                } else {
                    getLogger().warning("You are currently the latest version of the plugin! - "
                            + getDescription().getVersion());
                }
            } catch (Exception e) {
                getLogger().info("Could not check for updates! Stacktrace:");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
