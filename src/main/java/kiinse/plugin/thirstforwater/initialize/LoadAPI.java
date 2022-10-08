package kiinse.plugin.thirstforwater.initialize;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.utilities.placeholders.IndicatorExpansion;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LoadAPI {

    public LoadAPI(@NotNull ThirstForWater thirstForWater) {
        thirstForWater.sendLog("Registering PlaceHolderAPI...");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            thirstForWater.sendLog(Level.INFO, "PlaceHolderAPI not found");
        } else {
            var expansion = new IndicatorExpansion(thirstForWater);
            if (!expansion.isRegistered()) expansion.register();
            thirstForWater.sendLog("PlaceHolderAPI registered");
        }
    }
}
