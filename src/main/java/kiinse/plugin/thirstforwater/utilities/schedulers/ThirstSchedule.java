package kiinse.plugin.thirstforwater.utilities.schedulers;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.effects.EffectsImpl;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.data.timings.interfaces.Timing;
import kiinse.plugin.thirstforwater.data.worlds.enums.WorldsType;
import kiinse.plugin.thirstforwater.data.worlds.interfaces.Worlds;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugin.thirstforwater.exceptions.TimingException;
import kiinse.plugin.thirstforwater.exceptions.WorldsException;
import kiinse.plugin.thirstforwater.utilities.permissions.PermissionUtils;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.api.schedulers.Scheduler;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulerData;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@SchedulerData(
        name = "ThirstSchedule"
)
public class ThirstSchedule extends Scheduler {

    private final PermissionUtils permissions;
    private final YamlFile config;
    private final List<String> effects;
    private final ThirstForWater thirstForWater;
    private final Worlds worlds;
    private final Timing timing;
    private final PlayerThirst thirst;

    public ThirstSchedule(@NotNull ThirstForWater thirstForWater) {
        super(thirstForWater);
        this.thirstForWater = thirstForWater;
        this.permissions = new PermissionUtils(thirstForWater);
        this.config = thirstForWater.getConfiguration();
        this.effects = new EffectsImpl(thirstForWater).getEffects();
        this.thirst = thirstForWater.getThirst();
        this.worlds = thirstForWater.getWorlds();
        this.timing = thirstForWater.getTiming();
    }

    @Override
    public void run() {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (DarkPlayerUtils.isSurvivalAdventure(player)) {
                var playerWorld = player.getWorld();
                var uuid = player.getUniqueId();
                try {
                    if (DarkPlayerUtils.isWalking(player)) {
                        if (DarkPlayerUtils.isActing(player)) {
                            if (worlds.hasActionWorld(playerWorld)) {
                                if (player.isSprinting() || DarkPlayerUtils.isClimbing(player))
                                    timing.setActions(uuid, timing.getActions(uuid) + 2.0);
                                if (DarkPlayerUtils.isJumping(player) || DarkPlayerUtils.isInLava(player))
                                    timing.setActions(uuid, timing.getActions(uuid) + 2.0);
                                removeThirstWorld(player, WorldsType.ACTION, playerWorld);
                            }
                        } else {
                            if (worlds.hasWalkWorld(playerWorld))
                                timing.setWalk(uuid, timing.getWalk(uuid) + (player.isSneaking() ? 1.2 : 2.0));
                        }
                    } else {
                        if (worlds.hasWalkWorld(playerWorld))
                            timing.setWalk(uuid, timing.getWalk(uuid) + 0.04);
                    }
                    addEffects(player);
                    removeThirstWorld(player, WorldsType.WALK, playerWorld);
                    removeThirstPoison(player);
                } catch (Exception e) {
                    thirstForWater.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }

    private void removeThirstPoison(@NotNull Player player) throws ThirstException {
        if (DarkPlayerUtils.isPoisoned(player))
            thirst.removeFromPlayer(player, config.getDouble(Config.POISONING_REMOVE_SECONDS));
    }

    private void removeThirstWorld(@NotNull Player player, @NotNull WorldsType type, @NotNull World playerWorld)
            throws TimingException, WorldsException, ThirstException {
        if (worlds.hasWorld(type, playerWorld)) {
            var uuid = player.getUniqueId();
            if (timing.getTiming(type, uuid) >= worlds.getWorldValue(type, playerWorld)) {
                thirst.removeFromPlayer(player, permissions.removeThirst(player, type));
                timing.setTiming(type, uuid, 0.0);
            }
        }
    }

    private void addEffects(@NotNull Player player) throws ThirstException {
        if (((int) thirst.getPlayerValue(player)) <= config.getInt(Config.ADD_EFFECTS_THIRST) && config.getBoolean(Config.ADD_EFFECTS_ENABLE)) {
            for (var effect : effects) {
                try {
                    player.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), 50, 1, false, false));
                } catch (Exception e) {
                    thirstForWater.sendLog(Level.WARNING,
                                           "Error on giving player '&a" + DarkPlayerUtils.getPlayerName(player) + "&6' effect: &a" + e.getMessage());
                }
            }
        }
    }
}
