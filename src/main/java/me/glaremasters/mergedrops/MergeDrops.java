package me.glaremasters.mergedrops;

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
        Bukkit.getPluginManager().registerEvents(new Listener(), this);
    }

    public void onDisable() {

    }

}
