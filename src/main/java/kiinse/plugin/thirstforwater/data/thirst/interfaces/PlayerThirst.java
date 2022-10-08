package kiinse.plugin.thirstforwater.data.thirst.interfaces;

import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface PlayerThirst {

    boolean hasPlayer(@NotNull Player player);

    @NotNull PlayerThirst putPlayer(@NotNull Player player, double value);

    @NotNull PlayerThirst updatePlayer(@NotNull Player player, double value);

    double getPlayerValue(@NotNull Player player) throws ThirstException;

    double getPlayerValue(@NotNull UUID uuid) throws ThirstException;

    @NotNull PlayerThirst addToPlayer(@NotNull Player player, double value) throws ThirstException;

    @NotNull PlayerThirst removeFromPlayer(@NotNull Player player, double value) throws ThirstException;

    @NotNull PlayerThirst restorePlayer(@NotNull Player player);

    @NotNull PlayerThirst save() throws ThirstException;

}
