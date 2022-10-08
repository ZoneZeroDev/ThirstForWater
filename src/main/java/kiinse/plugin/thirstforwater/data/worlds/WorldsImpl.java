package kiinse.plugin.thirstforwater.data.worlds;

import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.data.worlds.interfaces.Worlds;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.enums.File;
import kiinse.plugin.thirstforwater.exceptions.WorldsException;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.logging.Level;

public class WorldsImpl extends YamlFile implements Worlds {

    private final DarkWaterJavaPlugin plugin;
    private final HashMap<String, Integer> walkWorlds = new HashMap<>();
    private final HashMap<String, Integer> actionWorlds = new HashMap<>();

    public WorldsImpl(@NotNull DarkWaterJavaPlugin plugin) throws WorldsException {
        super(plugin, File.WORLDS_YML);
        this.plugin = plugin;
        load();
    }

    @Override
    public void load() throws WorldsException {
        walkWorlds.clear();
        actionWorlds.clear();
        var walkWorldsList = getStringList(Config.WORLD_WALK);
        var actionWorldsList = getStringList(Config.WORLD_ACTION);
        if (walkWorldsList.isEmpty()) {
            throw new IllegalArgumentException("walkWorlds list is empty!");
        } else {
            for (var rawData : walkWorldsList) {
                var raw = rawData.split(":");
                walkWorlds.put(raw[0], Integer.valueOf(raw[1]));
                plugin.sendLog(Level.CONFIG, "Walk world: " + raw[0] + " | Value: " + Double.valueOf(raw[1]));
            }
            plugin.sendLog("Walk worlds hashmap loaded");
        }

        if (actionWorldsList.isEmpty()) {
            throw new WorldsException("actionWorlds list is empty!");
        } else {
            for (var rawData : actionWorldsList) {
                var raw = rawData.split(":");
                actionWorlds.put(raw[0], Integer.valueOf(raw[1]));
                plugin.sendLog(Level.CONFIG, "Action world: " + raw[0] + " | Value: " + Double.valueOf(raw[1]));
            }
            plugin.sendLog("Action worlds hashmap loaded");
        }
    }

    @Override
    public boolean hasWorld(@NotNull WorldsType type, @NotNull World world) {
        if (type == WorldsType.ACTION) return hasActionWorld(world);
        return hasWalkWorld(world);
    }

    @Override
    public boolean hasWalkWorld(@NotNull World world) {
        return walkWorlds.containsKey(world.getName());
    }

    @Override
    public boolean hasActionWorld(@NotNull World world) {
        return actionWorlds.containsKey(world.getName());
    }

    @Override
    public int getWorldValue(@NotNull WorldsType type, @NotNull World world) throws WorldsException {
        if (type == WorldsType.ACTION) return getActionWorldValue(world);
        return getWalkWorldValue(world);
    }

    @Override
    public int getWalkWorldValue(@NotNull World world) throws WorldsException {
        if (hasWalkWorld(world)) return walkWorlds.get(world.getName());
        throw new WorldsException("World not found");
    }

    @Override
    public int getActionWorldValue(@NotNull World world) throws WorldsException {
        if (hasActionWorld(world)) return actionWorlds.get(world.getName());
        throw new WorldsException("World not found");
    }
}
