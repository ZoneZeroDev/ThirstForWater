package kiinse.plugin.thirstforwater.listeners;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.enums.Message;
import kiinse.plugin.thirstforwater.enums.ThirstReplace;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.api.files.messages.MessagesUtils;
import kiinse.plugins.darkwaterapi.core.files.messages.DarkMessagesUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

    private final ThirstForWater thirstForWater = ThirstForWater.getInstance();
    private final MessagesUtils messagesUtils = new DarkMessagesUtils(thirstForWater);
    private final PlayerThirst thirst = thirstForWater.getThirst();

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) throws ThirstException {
        var player = event.getEntity();
        if (thirst.getPlayerValue(player) <= 0.0) {
            event.setDeathMessage("");
            messagesUtils.sendMessageToAllWithReplace(Message.DEATH_MESSAGE, ThirstReplace.PLAYER, DarkPlayerUtils.getPlayerName(player));
        }
    }
}
