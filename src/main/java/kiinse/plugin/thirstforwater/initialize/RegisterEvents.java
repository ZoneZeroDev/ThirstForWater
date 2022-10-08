package kiinse.plugin.thirstforwater.initialize;

import kiinse.plugin.thirstforwater.listeners.*;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RegisterEvents {

    public RegisterEvents(@NotNull DarkWaterJavaPlugin plugin) {
        plugin.sendLog("Registering listeners...");
        plugin.getServer().getPluginManager().registerEvents(new DrinkListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InteractListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new OnJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), plugin);
        plugin.sendLog("Listeners registered");
    }
}
