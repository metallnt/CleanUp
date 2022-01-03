package com.github.metallnt.cleanup.configs;

import com.github.metallnt.cleanup.CleanUp;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Class com.github.metallnt.cleanup.configs
 * <p>
 * Date: 19.12.2021 15:44 19 12 2021
 *
 * @author Metall
 */
public class DefaultConfig extends AbstractConfig {

    private final String fileName = "config.yml";

    public DefaultConfig(final CleanUp cleanUp) {
        this.setFileName(fileName);
        this.setPlugin(cleanUp);
    }

    // Обновление конфига с новыми настройками
    public boolean updateConfigNewOptions() {
        File configFile = new File(this.getPlugin().getDataFolder(), fileName);

        try {
            ConfigUpdater.update(this.getPlugin(), fileName, configFile, Collections.emptyList());
            this.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void reloadConfig() {
        this.loadConfig();
    }

    // Вставляем фенкции получения конкретных значений из конфига
    public boolean getUpdateConfig() {
        return this.getConfig().getBoolean("update_config", true);
    }

    public boolean getDeleteDirs() {
        return this.getConfig().getBoolean("delete_dirs.enable", true);
    }

    public int getPeriodDeleteDirs() {
        return this.getConfig().getInt("delete_dirs.period_days");
    }

    public boolean getPurgeData() {
        return this.getConfig().getBoolean("purge_data.enable", true);
    }

    public int getPeriodPurgeData() {
        return this.getConfig().getInt("purge_data.period_month");
    }

    public boolean getClearDatabase() {
        return this.getConfig().getBoolean("database.enable", true);
    }

    public int getNonPluginPeriod() {
        return this.getConfig().getInt("database.period_month_non_plugin");
    }

    public int getWithPluginPeriod() {
        return this.getConfig().getInt("database.period_month_with_plugin");
    }

    public String getTypeDatabase() {
        return this.getConfig().getString("database.type_database");
    }

    public List getProtectionPlayers() {
        return (List) this.getConfig().getStringList("protection_players");
    }

    public List getProtectionPlugins() {
        return (List) this.getConfig().getStringList("protection_plugins");
    }

    public List getProtectionFolders() {
        return (List) this.getConfig().getStringList("protection_folders");
    }
}
