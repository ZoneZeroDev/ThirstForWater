package kiinse.plugin.thirstforwater.listeners;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class PlayerRespawnListener implements Listener {

    private final ThirstForWater thirstForWater = ThirstForWater.getInstance();
    private final PlayerThirst thirst = thirstForWater.getThirst();

    @EventHandler
    public void respawnEvent(@NotNull PlayerRespawnEvent event) throws ThirstException {
        var player = event.getPlayer();
        thirst.restorePlayer(player);
        thirstForWater.sendLog(Level.CONFIG,
                               "Player '&d" + DarkPlayerUtils.getPlayerName(event.getPlayer()) + "&6' died. His thirst now is &d" + thirst.getPlayerValue(
                                       player));
    }
}
