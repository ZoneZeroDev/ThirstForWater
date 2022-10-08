package kiinse.plugin.thirstforwater.utilities;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugin.thirstforwater.recipes.interfaces.ThirstRecipes;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.logging.Level;

public class ThirstUtils {

    private static final ThirstForWater thirstForWater = ThirstForWater.getInstance();
    private static final YamlFile config = thirstForWater.getConfiguration();
    private static final ThirstRecipes recipes = thirstForWater.getRecipes();
    private static final PlayerThirst thirst = thirstForWater.getThirst();
    private static final Random random = new Random();

    private ThirstUtils() {}

    public static void replenishThirstBottle(@NotNull PlayerItemConsumeEvent event, @Nullable ItemMeta meta) throws ThirstException {
        if (meta != null) {
            var player = event.getPlayer();
            if (event.getItem().hasItemMeta() && Bukkit.getItemFactory().equals(meta, recipes.getCleanWater().getItemMeta())) {
                thirst.addToPlayer(player, config.getDouble(Config.RECOVERY_BOTTLE_CLEAN));
            } else if (meta instanceof PotionMeta && ((PotionMeta) meta).getBasePotionData().getType() == PotionType.WATER) {
                if (random.nextInt(101) <= config.getDouble(Config.POISONING_BOTTLE_CHANCE)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (config.getInt(Config.POISONING_DURATION) * 20), 1));
                }
                thirst.addToPlayer(player, config.getDouble(Config.RECOVERY_BOTTLE_DEFAULT));
            }
        }
    }

    public static void replenishThirstWaterBlock(@NotNull PlayerInteractEvent event, @NotNull Action action) throws ThirstException {
        var player = event.getPlayer();
        if (event.getItem() == null && DarkUtils.isClickAction(action) && player.isSneaking()) {
            for (Block block : player.getLineOfSight(null, 4)) {
                if (block.getType() == Material.WATER) {
                    if (random.nextInt(101) <= config.getDouble(Config.POISONING_BLOCK_CHANCE)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (config.getInt(Config.POISONING_DURATION) * 20), 1));
                    }
                    thirst.addToPlayer(player, config.getDouble(Config.RECOVERY_BLOCK));
                    return;
                }
            }
        }
    }

    public static void restoreBottleByRain(@NotNull PlayerInteractEvent event, @NotNull Action action) {
        var player = event.getPlayer();
        var item = event.getItem();
        if (item != null && item.getType() == Material.GLASS_BOTTLE && DarkUtils.isClickAction(action) && player.isSneaking() && DarkPlayerUtils.isInRain(
                player)) {
            if (item.getAmount() == 1) {
                player.getInventory().remove(item);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
            player.updateInventory();
            player.getInventory().addItem(recipes.getWaterBottle());
        }
    }

    public static void giveItemsAtFirstJoin(@NotNull Player player) {
        if (config.getBoolean(Config.FIRST_JOIN_GIVE_BOTTLE)) {
            var amount = config.getInt(Config.FIRST_JOIN_AMOUNT_BOTTLE);
            int i;
            for (i = 0; i < amount; i++) {
                player.getInventory().addItem(recipes.getWaterBottle());
            }
            thirstForWater.sendLog(Level.CONFIG,
                                   "Player '&d" + DarkPlayerUtils.getPlayerName(player) + "&6' received '&d" + i + "&6' bottles on the first join.");
        }
        if (config.getBoolean(Config.FIRST_JOIN_GIVE_FILTER)) {
            var amount = config.getInt(Config.FIRST_JOIN_AMOUNT_FILTER);
            int i;
            for (i = 0; i < amount; i++) {
                player.getInventory().addItem(recipes.getFilter());
            }
            thirstForWater.sendLog("Player '&d" + DarkPlayerUtils.getPlayerName(player) + "&6' received '&d" + i + "&a' filters on the first join.");
        }
    }
}
