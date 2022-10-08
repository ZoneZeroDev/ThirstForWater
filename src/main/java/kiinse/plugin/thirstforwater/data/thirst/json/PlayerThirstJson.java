package kiinse.plugin.thirstforwater.data.thirst.json;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.enums.File;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.api.exceptions.JsonFileException;
import kiinse.plugins.darkwaterapi.api.files.filemanager.JsonFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerThirstJson implements PlayerThirst {

    private final JSONObject thirstJson;
    private final JsonFile json;
    private final ThirstForWater thirstForWater;

    public PlayerThirstJson(@NotNull ThirstForWater thirstForWater) throws ThirstException {
        try {
            this.thirstForWater = thirstForWater;
            this.json = new JsonFile(thirstForWater, File.DATA_JSON);
            thirstJson = json.getJsonFromFile();
        } catch (JsonFileException e) {
            throw new ThirstException(e);
        }
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return thirstJson.has(player.getUniqueId().toString());
    }

    @Override
    public @NotNull PlayerThirst putPlayer(@NotNull Player player, double value) {
        thirstJson.put(player.getUniqueId().toString(), value);
        thirstForWater.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' saved to json");
        return this;
    }

    @Override
    public @NotNull PlayerThirst updatePlayer(@NotNull Player player, double value) {
        thirstJson.remove(player.getUniqueId().toString());
        thirstJson.put(player.getUniqueId().toString(), value);
        return this;
    }

    @Override
    public double getPlayerValue(@NotNull Player player) throws ThirstException {
        if (thirstJson.has(player.getUniqueId().toString()))
            return thirstJson.getDouble(player.getUniqueId().toString());
        throw new ThirstException("Player not found");
    }

    @Override
    public double getPlayerValue(@NotNull UUID uuid) throws ThirstException {
        if (thirstJson.has(uuid.toString()))
            return thirstJson.getDouble(uuid.toString());
        throw new ThirstException("Player not found");
    }

    @Override
    public @NotNull PlayerThirst addToPlayer(@NotNull Player player, double value) throws ThirstException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.min((getPlayerValue(player) + value), 110.0));
            return this;
        }
        throw new ThirstException("Player not found");
    }

    @Override
    public @NotNull PlayerThirst removeFromPlayer(@NotNull Player player, double value) throws ThirstException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.max((getPlayerValue(player) - value), 0.0));
            return this;
        }
        throw new ThirstException("Player not found");
    }

    @Override
    public @NotNull PlayerThirst restorePlayer(@NotNull Player player) {
        updatePlayer(player, 110.0);
        thirstForWater.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' thirst restored");
        return this;
    }

    @Override
    public @NotNull PlayerThirst save() throws ThirstException {
        try {
            json.saveJsonToFile(thirstJson);
            thirstForWater.sendLog(Level.CONFIG, "JsonObject saved to file");
            return this;
        } catch (JsonFileException e) {
            throw new ThirstException(e);
        }
    }

}
