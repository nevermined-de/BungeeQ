/*
 * This file is part of BungeeQ, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Maurice Hildebrand <webadmin@nevermined.de>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.nevermined.bungeeqbungee.config;

import de.nevermined.bungeeqbungee.BungeeQBungeePlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@Getter
public class ConfigManager {

  private static final Logger LOGGER = BungeeQBungeePlugin.getInstance().getLogger();
  private static final String configFileName = "config.yml";

  private static ConfigManager instance;
  private static Configuration configuration;
  private static File configurationFile;

  private boolean configLoaded = false;

  private String mySqlUser;
  private String mySqlPassword;
  private String mySqlHost;
  private String mySqlPort;
  private String mySqlDatabase;
  private String mySqlPrefix;

  private List<String> questions;
  private String unlockGroup;
  private boolean sendGlobalMessage;
  private String informationSound;
  private int timeToAutoResponse;

  private ConfigManager() {
    instance = this;

    Plugin plugin = BungeeQBungeePlugin.getInstance();

    loadConfigFile(plugin);
    loadConfig();
  }

  public static ConfigManager getInstance() {
    if (instance == null) {
      return new ConfigManager();
    }
    return instance;
  }

  public boolean reloadConfig () {
    try {
      configuration = ConfigurationProvider
          .getProvider(YamlConfiguration.class)
          .load(configurationFile);

      this.configLoaded = true;
      loadConfig();
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, "Error: ", ex);
      this.configLoaded = false;
    }
    return this.configLoaded;
  }

  private void loadConfigFile (Plugin plugin) {
    File dataFolder = plugin.getDataFolder();

    configurationFile = new File(dataFolder, configFileName);
    if (configurationFile.exists()) {
      reloadConfig();
    } else {
      createConfigFile(plugin, dataFolder);
    }
  }

  private void createConfigFile(Plugin plugin, File dataFolder) {
    if (!dataFolder.exists()) {
      dataFolder.mkdir();
    }

    File file = new File(dataFolder, configFileName);

    if (!file.exists()) {
      try (InputStream in = plugin.getResourceAsStream(configFileName)) {
        Files.copy(in, file.toPath());
      } catch (IOException ex) {
        LOGGER.log(Level.SEVERE, null, ex);
      }
    }
  }

  private void loadConfig() {
    this.mySqlUser = configuration.getString("mysql.user");
    this.mySqlPassword = configuration.getString("mysql.password");
    this.mySqlHost = configuration.getString("mysql.host");
    this.mySqlPort = configuration.getString("mysql.port");
    this.mySqlDatabase = configuration.getString("mysql.database");
    this.mySqlPrefix = configuration.getString("mysql.prefix");

    this.questions = configuration.getStringList("general.questions");
    this.unlockGroup = configuration.getString("general.unlock_group");
    this.sendGlobalMessage = configuration.getBoolean("general.send_global_message");
    this.informationSound = configuration.getString("general.information_sound");
    this.timeToAutoResponse = configuration.getInt("general.time_to_auto_response");
  }
}
