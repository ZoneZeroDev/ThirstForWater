package kiinse.plugin.thirstforwater.data.thirst.sql;

import kiinse.plugin.thirstforwater.data.thirst.sql.database.tables.Players;
import kiinse.plugin.thirstforwater.enums.Config;
import kiinse.plugins.darkwaterapi.api.DarkWaterJavaPlugin;
import kiinse.plugins.darkwaterapi.api.databases.Postgresql;
import kiinse.plugins.darkwaterapi.api.databases.SQLConnectionSettings;
import kiinse.plugins.darkwaterapi.api.files.filemanager.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ThirstSQL extends Postgresql {

    private final YamlFile config;

    public ThirstSQL(@NotNull DarkWaterJavaPlugin plugin) throws SQLException {
        super(plugin);
        this.config = plugin.getConfiguration();
    }

    @Override
    public @NotNull SQLConnectionSettings getSettings(@NotNull DarkWaterJavaPlugin darkWaterJavaPlugin) {
        return new SQLConnectionSettings()
                .setDbName(config.getString(Config.PG_DBNAME))
                .setHost(config.getString(Config.PG_HOST))
                .setLogin(config.getString(Config.PG_LOGIN))
                .setPassword(config.getString(Config.PG_PASSWORD));
    }

    @Override
    public void createDataBases(@NotNull DSLContext context) {
        context.createTableIfNotExists(Players.PLAYERS)
               .columns(Players.PLAYERS.ID)
               .columns(Players.PLAYERS.UUID)
               .columns(Players.PLAYERS.NAME)
               .columns(Players.PLAYERS.VALUE)
               .primaryKey(Players.PLAYERS.ID)
               .execute();
    }

    @Override
    public @NotNull Connection registerConnection(@NotNull SQLConnectionSettings settings) throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection(getURL(settings) + settings.getDbName(), getProperties(settings));
    }
}
