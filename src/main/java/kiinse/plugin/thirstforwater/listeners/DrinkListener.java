package kiinse.plugin.thirstforwater.listeners;

import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugin.thirstforwater.utilities.ThirstUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

public class DrinkListener implements Listener {

    @EventHandler
    public void drinkEvent(@NotNull PlayerItemConsumeEvent event) throws ThirstException {
        ThirstUtils.replenishThirstBottle(event, event.getItem().getItemMeta());
    }
}
