package kiinse.plugin.thirstforwater.data.worlds.interfaces;

import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.exceptions.WorldsException;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface Worlds {

    void load() throws WorldsException;

    boolean hasWorld(@NotNull WorldsType type, @NotNull World world);

    boolean hasWalkWorld(@NotNull World world);

    boolean hasActionWorld(@NotNull World world);

    int getWorldValue(@NotNull WorldsType type, @NotNull World world) throws WorldsException;

    int getWalkWorldValue(@NotNull World world) throws WorldsException;

    int getActionWorldValue(@NotNull World world) throws WorldsException;
}
