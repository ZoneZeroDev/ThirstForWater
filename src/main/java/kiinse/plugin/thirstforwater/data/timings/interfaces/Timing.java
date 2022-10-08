package kiinse.plugin.thirstforwater.data.timings.interfaces;

import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.exceptions.TimingException;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Timing {

    boolean hasActionsUUID(@NotNull UUID uuid);

    boolean hasWalkUUID(@NotNull UUID uuid);

    double getActions(@NotNull UUID uuid) throws TimingException;

    double getWalk(@NotNull UUID uuid) throws TimingException;

    double getTiming(@NotNull WorldsType type, @NotNull UUID uuid) throws TimingException;

    void setActions(@NotNull UUID uuid, double value);

    void setWalk(@NotNull UUID uuid, double value);

    void setTiming(@NotNull WorldsType type, @NotNull UUID uuid, double value);

    void addTimingPlayer(@NotNull UUID uuid);
}
