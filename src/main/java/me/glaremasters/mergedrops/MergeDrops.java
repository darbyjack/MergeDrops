package me.glaremasters.mergedrops;

import me.glaremasters.mergedrops.events.MobDeathEvent;
import me.glaremasters.mergedrops.updater.SpigotUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by GlareMasters on 12/26/2017.
 */
public class MergeDrops extends JavaPlugin {

    private static MergeDrops instance;

    public static MergeDrops getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new MobDeathEvent(), this);

        if (getConfig().getBoolean("updater.check")) {
            SpigotUpdater updater = new SpigotUpdater(this, 52881);
            try {
                if (updater.checkForUpdates()) {
                    getLogger()
                            .warning("You appear to be running a version other than our latest stable release."
                                    + " You can download our newest version at: " + updater
                                    .getResourceURL());
                } else {
                    getLogger().warning("You are currently the latest version of the plugin! - " + getDescription().getVersion());
                }
            } catch (Exception e) {
                getLogger().info("Could not check for updates! Stacktrace:");
                e.printStackTrace();
            }
        }

    }

    public void onDisable() {

    }

}
