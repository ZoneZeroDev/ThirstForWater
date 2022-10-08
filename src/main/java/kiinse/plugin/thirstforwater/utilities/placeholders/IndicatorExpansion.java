package kiinse.plugin.thirstforwater.utilities.placeholders;

import com.google.common.base.Strings;
import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.enums.Message;
import kiinse.plugin.thirstforwater.enums.ThirstReplace;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import kiinse.plugins.darkwaterapi.api.files.locale.PlayerLocales;
import kiinse.plugins.darkwaterapi.api.files.messages.Messages;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Level;

public class IndicatorExpansion extends PlaceholderExpansion {

    private final ThirstForWater thirstForWater;
    private final PlayerThirst playerThirst;
    private final Messages messages;
    private final PlayerLocales locales;
    private final YamlFile config;

    // %tfw_thirst%
    // %tfw_numeric%
    // %tfw_status%
    // %tfw_indicator%
    // %tfw_indicator_simple%

    public IndicatorExpansion(@NotNull ThirstForWater thirstForWater) {
        this.thirstForWater = thirstForWater;
        this.config = thirstForWater.getConfiguration();
        this.playerThirst = thirstForWater.getThirst();
        this.messages = thirstForWater.getMessages();
        locales = thirstForWater.getDarkWaterAPI().getPlayerLocales();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return thirstForWater.getDescription().getAuthors().get(0);
    }


    @Override
    public @NotNull String getIdentifier() {
        return "tfw";
    }

    @Override
    public @NotNull String getVersion() {
        return thirstForWater.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";
        double thirst = 0;
        try {
            thirst = playerThirst.getPlayerValue(player);
        } catch (ThirstException e) {
            thirstForWater.sendLog(Level.WARNING, e.getMessage());
        }
        return switch (identifier) {
            case "thirst" -> messages.getStringMessage(locales.getLocale(player), Message.PH_THIRST);
            case "numeric" -> numericFunc(thirst);
            case "status" -> statusFunc(player, thirst);
            case "indicator_simple" -> indicatorSimpleFunc(thirst);
            case "indicator" -> indicatorFunc(thirst);
            default -> null;
        };
    }

    private String numericFunc(double thirst) {
        if (thirst > 100.0) return "100";
        return String.valueOf(((Double) thirst).intValue());
    }

    private String statusFunc(Player player, double thirst) {
        if (thirst >= 100.0)
            return messages.getStringMessage(locales.getLocale(player), Message.PH_NO_THIRST);
        if (thirst < 100.0 && thirst >= 70.0)
            return messages.getStringMessage(locales.getLocale(player), Message.PH_LIGHT_THIRST);
        if (thirst < 70.0 && thirst >= 40.0)
            return messages.getStringMessage(locales.getLocale(player), Message.PH_MEDIUM_THIRST);
        if (thirst < 40.0 && thirst >= 10.0)
            return messages.getStringMessage(locales.getLocale(player), Message.PH_HARD_THIRST);
        if (thirst < 10.0)
            return messages.getStringMessage(locales.getLocale(player), Message.PH_CRITICAL_THIRST);
        return "NaN";
    }

    private String indicatorSimpleFunc(double thirst) {
        if (thirst > 100) {
            return ChatColor.DARK_BLUE + "----------";
        } else if (thirst <= 0) {
            return ChatColor.DARK_RED + "----------";
        } else if (thirst <= 19) {
            return getProgressBar((int) thirst, ChatColor.RED);
        }
        return getProgressBar((int) thirst, ChatColor.AQUA);
    }

    private String indicatorFunc(double thirst) {
        if (thirst > 100) {
            return DarkUtils.colorize(config.getString(Config.INDICATOR_FULL));
        } else if (thirst <= 0) {
            return DarkUtils.colorize(config.getString(Config.INDICATOR_EMPTY));
        }
        return getIndicator(((Double) thirst).intValue());
    }

    private String getProgressBar(int current, @NotNull ChatColor completedColor) {
        int progressBars = (int) (10 * ((float) current / 100));
        return Strings.repeat("" + completedColor + '-', progressBars)
               + Strings.repeat("" + ChatColor.DARK_GRAY + '-', 10 - progressBars);
    }

    private String getIndicator(int thirst) {
        return DarkUtils.colorize(DarkUtils.replaceWord(config.getString(Config.INDICATOR_FORMAT),
                                                        ThirstReplace.INDICATOR,
                                                        DarkUtils.getProgressBar(thirst,
                                                                                 100,
                                                                                 10,
                                                                                 Objects.requireNonNull(config.getString(Config.INDICATOR_CHAR_FIRST)),
                                                                                 Objects.requireNonNull(config.getString(Config.INDICATOR_CHAR_SECOND)))));
    }
}
