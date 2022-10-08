package kiinse.plugin.thirstforwater.utilities.schedulers;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.api.schedulers.Scheduler;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulerData;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@SchedulerData(
        name = "DamageSchedule"
)
public class DamageSchedule extends Scheduler {

    private final ThirstForWater thirstForWater;
    private final PlayerThirst thirst;

    public DamageSchedule(@NotNull ThirstForWater thirstForWater) {
        super(thirstForWater);
        this.thirstForWater = thirstForWater;
        this.thirst = thirstForWater.getThirst();
    }

    @Override
    public void run() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (DarkPlayerUtils.isSurvivalAdventure(player)) {
                try {
                    damagePlayer(player);
                } catch (ThirstException e) {
                    thirstForWater.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }

    private void damagePlayer(@NotNull Player player) throws ThirstException {
        if (thirst.getPlayerValue(player) <= 0.0 && player.getHealth() > 0.0)
            player.damage(thirstForWater.getConfiguration().getInt(Config.DAMAGE));
    }
}
