package com.github.metallnt.cleanup;

import com.github.metallnt.cleanup.actions.ClearDatabase;
import com.github.metallnt.cleanup.actions.DeleteDirs;
import com.github.metallnt.cleanup.actions.PurgeData;
import com.github.metallnt.cleanup.configs.DefaultConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CleanUp extends JavaPlugin {
    public static Logger log;

    private static CleanUp plugin;

    private DefaultConfig defaultConfig;
    private DeleteDirs deleteDirs;
    private PurgeData purgeData;
    private ClearDatabase clearDatabase;

    public static CleanUp getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getLogger();

        // Регистрируем конфиг
        setDefaultConfig(new DefaultConfig(this));

        // Загружаем конфиг
        if (!this.getDefaultConfig().loadConfig()) {
            this.getLogger().info("Конфиг не загружен!");
        }

        // Обновляем конфиг
        if (this.getDefaultConfig().getUpdateConfig()) {
            if (this.getDefaultConfig().updateConfigNewOptions()) {
                this.getLogger().info("Конфиг обновлен");
            } else {
                this.getLogger().info("Конфиг не смог обновиться");
            }
        }

        // Register classes
        setDeleteDirs(new DeleteDirs(this));
        setPurgeData(new PurgeData(this));
        setClearDatabase(new ClearDatabase(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(DefaultConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public DeleteDirs getDeleteDirs() {
        return deleteDirs;
    }

    public void setDeleteDirs(DeleteDirs deleteDirs) {
        this.deleteDirs = deleteDirs;
    }

    public PurgeData getPurgeData() {
        return purgeData;
    }

    public void setPurgeData(PurgeData purgeData) {
        this.purgeData = purgeData;
    }

    public ClearDatabase getClearDatabase() {
        return clearDatabase;
    }

    public void setClearDatabase(ClearDatabase clearDatabase) {
        this.clearDatabase = clearDatabase;
    }
}
