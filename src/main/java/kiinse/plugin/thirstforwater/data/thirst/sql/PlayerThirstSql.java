package kiinse.plugin.thirstforwater.data.thirst.sql;

import kiinse.plugin.thirstforwater.ThirstForWater;
import kiinse.plugin.thirstforwater.data.thirst.interfaces.PlayerThirst;
import kiinse.plugin.thirstforwater.data.thirst.sql.database.tables.Players;
import kiinse.plugin.thirstforwater.exceptions.ThirstException;
import kiinse.plugins.darkwaterapi.core.utilities.DarkPlayerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerThirstSql implements PlayerThirst {

    private final Connection connection;
    private final DSLContext context;
    private final ThirstForWater thirstForWater;

    public PlayerThirstSql(ThirstForWater thirstForWater) {
        this.thirstForWater = thirstForWater;
        context = thirstForWater.getThirstSQL().getContext();
        connection = thirstForWater.getThirstSQL().getConnection();
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return context.select(Players.PLAYERS.fields())
                      .from(Players.PLAYERS)
                      .where(Players.PLAYERS.UUID.equal(player.getUniqueId()))
                      .fetch().isNotEmpty();
    }

    @Override
    public @NotNull PlayerThirst putPlayer(@NotNull Player player, double value) {
        var newRecord = context.newRecord(Players.PLAYERS);
        newRecord.set(Players.PLAYERS.UUID, player.getUniqueId());
        newRecord.set(Players.PLAYERS.NAME, DarkPlayerUtils.getPlayerName(player));
        newRecord.set(Players.PLAYERS.VALUE, value);
        newRecord.store();
        thirstForWater.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' saved to database");
        return this;
    }

    @Override
    public @NotNull PlayerThirst updatePlayer(@NotNull Player player, double value) {
        context.update(Players.PLAYERS)
               .set(Players.PLAYERS.VALUE, value)
               .where(Players.PLAYERS.UUID.eq(player.getUniqueId()))
               .execute();
        return this;
    }

    @Override
    public double getPlayerValue(@NotNull Player player) {
        return context.select(Players.PLAYERS.VALUE)
                      .from(Players.PLAYERS)
                      .where(Players.PLAYERS.UUID.equal(player.getUniqueId()))
                      .fetch().get(0).component1();
    }

    @Override
    public double getPlayerValue(@NotNull UUID uuid) {
        return context.select(Players.PLAYERS.VALUE)
                      .from(Players.PLAYERS)
                      .where(Players.PLAYERS.UUID.equal(uuid))
                      .fetch().get(0).component1();
    }

    @Override
    public @NotNull PlayerThirst addToPlayer(@NotNull Player player, double value) throws ThirstException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.min((getPlayerValue(player) + value), 110.0));
            return this;
        }
        throw new ThirstException("Player not found");
    }

    @Override
    public @NotNull PlayerThirst removeFromPlayer(@NotNull Player player, double value) throws ThirstException {
        if (hasPlayer(player)) {
            updatePlayer(player, Math.max((getPlayerValue(player) - value), 0.0));
            return this;
        }
        throw new ThirstException("Player not found");
    }

    @Override
    public @NotNull PlayerThirst restorePlayer(@NotNull Player player) {
        updatePlayer(player, 110.0);
        thirstForWater.sendLog(Level.CONFIG, "Player '" + player.getUniqueId() + "' thirst restored");
        return this;
    }

    @Override
    public @NotNull PlayerThirst save() throws ThirstException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                thirstForWater.sendLog(Level.CONFIG, "Postgresql connection closed");
            }
            return this;
        } catch (SQLException e) {
            throw new ThirstException(e);
        }
    }
}
