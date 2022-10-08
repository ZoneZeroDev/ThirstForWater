package kiinse.plugin.thirstforwater.data.timings;

import kiinse.plugin.thirstforwater.data.timings.interfaces.Timing;
import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.exceptions.TimingException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class TimingImpl implements Timing {

    private static final HashMap<UUID, Double> actions = new HashMap<>();
    private static final HashMap<UUID, Double> walk = new HashMap<>();

    @Override
    public boolean hasActionsUUID(@NotNull UUID uuid) {
        return actions.containsKey(uuid);
    }

    @Override
    public boolean hasWalkUUID(@NotNull UUID uuid) {
        return walk.containsKey(uuid);
    }

    @Override
    public double getActions(@NotNull UUID uuid) throws TimingException {
        if (actions.containsKey(uuid)) return actions.get(uuid);
        throw new TimingException("Player UUID not found");
    }

    @Override
    public double getWalk(@NotNull UUID uuid) throws TimingException {
        if (walk.containsKey(uuid)) return walk.get(uuid);
        throw new TimingException("Player UUID not found");
    }

    @Override
    public double getTiming(@NotNull WorldsType type, @NotNull UUID uuid) throws TimingException {
        if (type == WorldsType.ACTION) return getActions(uuid);
        return getWalk(uuid);
    }

    @Override
    public void setActions(@NotNull UUID uuid, double value) {
        if (!actions.containsKey(uuid)) {
            actions.put(uuid, value);
            return;
        }
        actions.replace(uuid, value);
    }

    @Override
    public void setWalk(@NotNull UUID uuid, double value) {
        if (!walk.containsKey(uuid)) {
            walk.put(uuid, value);
            return;
        }
        walk.replace(uuid, value);
    }

    @Override
    public void setTiming(@NotNull WorldsType type, @NotNull UUID uuid, double value) {
        if (type == WorldsType.ACTION) {
            setActions(uuid, value);
            return;
        }
        setWalk(uuid, value);
    }

    @Override
    public void addTimingPlayer(@NotNull UUID uuid) {
        if (!hasWalkUUID(uuid)) {
            setWalk(uuid, 0.0);
        }
        if (!hasActionsUUID(uuid)) {
            setActions(uuid, 0.0);
        }
    }

}
