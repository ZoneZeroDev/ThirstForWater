package kiinse.plugin.thirstforwater.listeners;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.data.timings.interfaces.Timing;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugin.thirstforwater.utilities.ThirstUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class OnJoinListener implements Listener {

    private final ThirstForWater thirstForWater = ThirstForWater.getInstance();
    private final PlayerThirst thirst = thirstForWater.getThirst();
    private final Timing timing = thirstForWater.getTiming();

    @EventHandler
    public void joinEvent(@NotNull PlayerJoinEvent event) throws ThirstException {
        var player = event.getPlayer();
        timing.addTimingPlayer(player.getUniqueId());
        if (!thirst.hasPlayer(player)) thirst.putPlayer(player, 110.0);
        if (!player.hasPlayedBefore()) ThirstUtils.giveItemsAtFirstJoin(player);

        thirstForWater.sendLog(Level.CONFIG,
                               "Player " +
                               DarkPlayerUtils.getPlayerName(player) +
                               " joined. His thirst is " +
                               thirst.getPlayerValue(player));
    }
}
