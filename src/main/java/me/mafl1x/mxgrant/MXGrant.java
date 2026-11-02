package me.mafl1x.mxgrant;

import de.exlll.configlib.YamlConfigurations;
import me.mafl1x.mxgrant.api.GrantAPI;
import me.mafl1x.mxgrant.api.IGrantAPI;
import me.mafl1x.mxgrant.api.LuckPermsAPI;
import me.mafl1x.mxgrant.api.VaultAPI;
import me.mafl1x.mxgrant.command.GrantCommand;
import me.mafl1x.mxgrant.config.Messages;
import me.mafl1x.mxgrant.config.Settings;
import me.mafl1x.mxgrant.config.inv.GrantInv;
import me.mafl1x.mxgrant.database.Database;
import me.mafl1x.mxgrant.handler.Handler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;

public final class MXGrant extends JavaPlugin {

    private static MXGrant instance;

    private Messages messages;
    private Settings settings;
    private GrantInv grantInv;
    private Database database;
    private IGrantAPI grantAPI;

    public static MXGrant getInstance() { return instance; }
    public Messages getMessages() { return messages; }
    public Settings getSettings() { return settings; }
    public GrantInv getGrantInv() { return grantInv; }
    public Database getDatabase() { return database; }
    public IGrantAPI getGrantAPI() { return grantAPI; }

    @Override
    public void onEnable() {
        instance = this;

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            getLogger().severe("Vault not found! Disabling MXGrant.");
            setEnabled(false);
            return;
        }

        VaultAPI.setupChat();
        LuckPermsAPI.init();

        getDataFolder().mkdirs();

        // Load configs
        this.settings = loadOrCreate("settings.yml", Settings.class, new Settings());
        this.messages = loadOrCreate("messages.yml", Messages.class, new Messages());

        File invFolder = new File(getDataFolder(), "inv");
        invFolder.mkdirs();
        this.grantInv = loadOrCreate("inv/grantinv.yml", GrantInv.class, new GrantInv());

        // Database
        this.database = new Database(settings, getDataFolder());
        this.database.createTable();

        this.grantAPI = new GrantAPI();

        // Register
        getCommand("grant").setExecutor(new GrantCommand());
        Bukkit.getPluginManager().registerEvents(new Handler(), this);

        getLogger().info("MXGrant enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (database != null) database.close();
        getLogger().info("MXGrant disabled.");
    }

    private <T> T loadOrCreate(String fileName, Class<T> type, T defaults) {
        Path path = new File(getDataFolder(), fileName).toPath();
        File file = path.toFile();
        if (!file.exists()) {
            YamlConfigurations.save(path, type, defaults);
            return defaults;
        }
        return YamlConfigurations.load(path, type);
    }
}
