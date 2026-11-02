package me.mafl1x.mxgrant.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mafl1x.mxgrant.config.Settings;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Database {

    private final HikariDataSource dataSource;
    private final boolean isSQLite;

    // Simple in-memory cache: key = "uuid:group", value = count
    private final Map<String, Integer> countCache = new ConcurrentHashMap<>();

    public Database(Settings settings, File dataFolder) {
        this.isSQLite = settings.database.type.equalsIgnoreCase("sqlite");
        HikariConfig config = new HikariConfig();

        if (isSQLite) {
            File dbFile = new File(dataFolder, settings.database.sqlite.file);
            config.setDriverClassName("org.sqlite.JDBC");
            config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            config.setMaximumPoolSize(1);
            config.addDataSourceProperty("journal_mode", "WAL");
        } else {
            Settings.MySQLConfig my = settings.database.mysql;
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + my.host + ":" + my.port + "/" + my.database
                    + "?useSSL=false&autoReconnect=true&characterEncoding=utf8");
            config.setUsername(my.user);
            config.setPassword(my.password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
        }

        config.setPoolName("MXGrant-Pool");
        this.dataSource = new HikariDataSource(config);
    }

    public void createTable() {
        String sql = isSQLite
                ? "CREATE TABLE IF NOT EXISTS grantapi (player TEXT, group_name TEXT)"
                : "CREATE TABLE IF NOT EXISTS grantapi (player VARCHAR(255), group_name VARCHAR(255))";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGroupGiveCount(UUID uuid, String group) {
        String cacheKey = uuid + ":" + group;
        if (countCache.containsKey(cacheKey)) {
            return countCache.get(cacheKey);
        }
        String qry = "SELECT count(*) AS count FROM grantapi WHERE player=? AND group_name=?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(qry)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, group);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    countCache.put(cacheKey, count);
                    return count;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addPlayerGroup(UUID uuid, String group) {
        String sql = "INSERT INTO grantapi (player, group_name) VALUES (?,?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, group);
            ps.execute();
            // Invalidate cache
            countCache.remove(uuid + ":" + group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
