package kiinse.plugin.thirstforwater;

import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.data.thirst.json.PlayerThirstJson;
import kiinse.plugin.thirstforwater.data.thirst.sql.PlayerThirstSql;
import kiinse.plugin.thirstforwater.data.thirst.sql.ThirstSQL;
import kiinse.plugin.thirstforwater.data.timings.TimingImpl;
import kiinse.plugin.thirstforwater.data.timings.interfaces.Timing;
import kiinse.plugin.thirstforwater.data.worlds.WorldsImpl;
import kiinse.plugin.thirstforwater.data.worlds.interfaces.Worlds;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugin.thirstforwater.initialize.LoadAPI;
import kiinse.plugin.thirstforwater.initialize.RegisterEvents;
import kiinse.plugin.thirstforwater.recipes.CreateRecipes;
import kiinse.plugin.thirstforwater.recipes.Recipes;
import kiinse.plugin.thirstforwater.recipes.RemoveRecipes;
import kiinse.plugin.thirstforwater.recipes.interfaces.ThirstRecipes;
import kiinse.plugin.thirstforwater.utilities.schedulers.DamageSchedule;
import kiinse.plugin.thirstforwater.utilities.schedulers.ThirstSchedule;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.exceptions.VersioningException;
import kiinse.plugins.darkwaterapi.api.indicators.Indicator;
import kiinse.plugins.darkwaterapi.api.indicators.IndicatorManager;
import kiinse.plugins.darkwaterapi.api.schedulers.Scheduler;
import kiinse.plugins.darkwaterapi.api.schedulers.SchedulersManager;
import kiinse.plugins.darkwaterapi.api.utilities.TaskType;
import kiinse.plugins.darkwaterapi.core.utilities.DarkUtils;
import kiinse.plugins.darkwaterapi.core.utilities.DarkVersionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

public final class ThirstForWater extends DarkWaterJavaPlugin {

    private static ThirstForWater instance;
    private Indicator indicator;
    private Scheduler damageScheduler;
    private Scheduler thirstScheduler;
    private SchedulersManager schedulersManager;
    private IndicatorManager indicatorManager;
    private ThirstSQL thirstSQL;
    private PlayerThirst thirst;
    private Worlds worlds;
    private Timing timing;
    private ThirstRecipes recipes;

    public static @NotNull ThirstForWater getInstance() {
        return instance;
    }

    @Override
    public void onStart() throws Exception {
        instance = this;
        var darkWater = getDarkWaterAPI();
        indicatorManager = darkWater.getIndicatorManager();
        schedulersManager = darkWater.getSchedulersManager();
        if (getConfiguration().getBoolean(Config.PG_ENABLED)) {
            thirstSQL = new ThirstSQL(this);
            thirst = new PlayerThirstSql(this);
        } else thirst = new PlayerThirstJson(this);
        timing = new TimingImpl();
        worlds = new WorldsImpl(this);
        recipes = new Recipes(this);
        new LoadAPI(this);
        new CreateRecipes(this);
        new RegisterEvents(this);
        indicator = Indicator.valueOf(this, "%tfw_indicator%", 0);
        if (getConfiguration().getBoolean(Config.INDICATOR_ACTIONBAR))
            indicatorManager.register(this, indicator);
        damageScheduler = new DamageSchedule(this);
        thirstScheduler = new ThirstSchedule(this);
        schedulersManager.register(damageScheduler);
        schedulersManager.register(thirstScheduler);
        checkForUpdates();
    }

    @Override
    public void onStop() throws Exception {
        thirst.save();
        indicatorManager.removeIndicator(indicator);
        schedulersManager.unregister(damageScheduler);
        schedulersManager.unregister(thirstScheduler);
        new RemoveRecipes(this);
    }

    private void checkForUpdates() {
        DarkUtils.runTask(TaskType.ASYNC, this, () -> {
            if (!getConfiguration().getBoolean(Config.DISABLE_VERSION_CHECK)) {
                try {
                    var latest = DarkVersionUtils.getLatestGithubVersion("https://github.com/NubilumDev/ThirstForWater");
                    if (!latest.isGreaterThan(DarkVersionUtils.getPluginVersion(this))) {
                        sendLog("Latest version of ThirstForWater installed, no new versions found <3");
                        return;
                    }
                    var reader = new BufferedReader(new InputStreamReader(
                            Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version-message.txt"))));
                    var builder = new StringBuilder("\n");
                    while (reader.ready())
                        builder.append(reader.readLine()).append("\n");
                    sendConsole(DarkUtils.replaceWord(builder.toString(), new String[]{
                            "{NEW_VERSION}:" + latest.getOriginalValue(),
                            "{CURRENT_VERSION}:" + getDescription().getVersion()
                    }));
                } catch (IOException | VersioningException e) {
                    sendLog(Level.WARNING, "Error while checking ThirstForWater version! Message: " + e.getMessage());
                }
            }
        });
    }

    public @NotNull PlayerThirst getThirst() {return thirst;}

    public @NotNull ThirstSQL getThirstSQL() {
        return thirstSQL;
    }

    public @NotNull Timing getTiming() {
        return timing;
    }

    public @NotNull Worlds getWorlds() {
        return worlds;
    }

    public @NotNull ThirstRecipes getRecipes() {
        return recipes;
    }
}

