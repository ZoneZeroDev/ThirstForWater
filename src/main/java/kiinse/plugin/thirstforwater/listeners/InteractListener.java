package kiinse.plugin.thirstforwater.listeners;

import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugin.thirstforwater.utilities.ThirstUtils;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class InteractListener implements Listener {

    @EventHandler
    public void interactEvent(@NotNull PlayerInteractEvent event) throws ThirstException {
        var action = event.getAction();
        ThirstUtils.replenishThirstWaterBlock(event, action);
        ThirstUtils.restoreBottleByRain(event, action);
    }
}
