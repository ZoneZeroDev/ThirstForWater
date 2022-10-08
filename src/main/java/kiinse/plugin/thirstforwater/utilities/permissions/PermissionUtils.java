package kiinse.plugin.thirstforwater.utilities.permissions;

import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.enums.Permission;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionUtils {

    private final YamlFile config;

    public PermissionUtils(@NotNull DarkWaterJavaPlugin plugin) {
        this.config = plugin.getConfiguration();
    }

    public @NotNull Double removeThirst(@NotNull Player player, @NotNull WorldsType type) {
        if (type == WorldsType.ACTION) return getConsumption(player, config.getDouble(Config.REMOVE_THIRST_ACTION));
        return getConsumption(player, config.getDouble(Config.REMOVE_THIRST_WALK));
    }

    private @NotNull Double getConsumption(@NotNull Player player, double consumption) {
        if (DarkPlayerUtils.hasPermission(player, Permission.TFW_CONSUMPTION_VIP))
            return consumption - (consumption * config.getDouble(Config.PERMISSION_CONSUMPTION_VIP));
        if (DarkPlayerUtils.hasPermission(player, Permission.TFW_CONSUMPTION_PREMIUM))
            return consumption - (consumption * config.getDouble(Config.PERMISSION_CONSUMPTION_PREMIUM));
        if (DarkPlayerUtils.hasPermission(player, Permission.TFW_CONSUMPTION_DELUXE))
            return consumption - (consumption * config.getDouble(Config.PERMISSION_CONSUMPTION_DELUXE));
        return consumption;
    }
}
