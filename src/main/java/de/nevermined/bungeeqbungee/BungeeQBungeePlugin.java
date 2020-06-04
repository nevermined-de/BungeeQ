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

package de.nevermined.bungeeqbungee;

import de.nevermined.bungeeqbungee.command.ActivateCommand;
import de.nevermined.bungeeqbungee.command.BungeeQCommand;
import de.nevermined.bungeeqbungee.command.ExitCommand;
import de.nevermined.bungeeqbungee.command.QAskCommand;
import de.nevermined.bungeeqbungee.command.QChatCommand;
import de.nevermined.bungeeqbungee.command.QDeclineCommand;
import de.nevermined.bungeeqbungee.command.QExitCommand;
import de.nevermined.bungeeqbungee.command.QGetCommand;
import de.nevermined.bungeeqbungee.command.QHistoryCommand;
import de.nevermined.bungeeqbungee.command.QListCommand;
import de.nevermined.bungeeqbungee.command.QMoveCommand;
import de.nevermined.bungeeqbungee.command.QRepeatCommand;
import de.nevermined.bungeeqbungee.command.QSolutionCommand;
import de.nevermined.bungeeqbungee.command.QUnlockCommand;
import de.nevermined.bungeeqbungee.command.QWatchCommand;
import de.nevermined.bungeeqbungee.config.ConfigManager;
import de.nevermined.bungeeqbungee.database.SqlManager;
import de.nevermined.bungeeqbungee.listener.ChatListener;
import de.nevermined.bungeeqbungee.listener.PostLoginListener;
import de.nevermined.bungeeqbungee.listener.PlayerDisconnectListener;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

@Getter
public class BungeeQBungeePlugin extends Plugin {

  @Getter
  private static BungeeQBungeePlugin instance;
  @Getter
  private static final String CHANNEL = "de.nevermined:bungeeq";
  private SqlManager sqlManager;

  @Override
  public void onEnable() {
    instance = this;

    ConfigManager config = ConfigManager.getInstance();

    if (config.isConfigLoaded()) {
      this.getProxy().registerChannel(CHANNEL);
      initDatabase();
      registerCommand();
      registerListener();
    }
  }

  @Override
  public void onDisable() {
    sqlManager.closePool();
  }

  private void registerCommand() {
    PluginManager pm = this.getProxy().getPluginManager();

    pm.registerCommand(this, new ActivateCommand());
    pm.registerCommand(this, new BungeeQCommand());
    pm.registerCommand(this, new ExitCommand());

    pm.registerCommand(this, new QAskCommand());
    pm.registerCommand(this, new QChatCommand());
    pm.registerCommand(this, new QDeclineCommand());
    pm.registerCommand(this, new QExitCommand());
    pm.registerCommand(this, new QGetCommand());
    pm.registerCommand(this, new QHistoryCommand());
    pm.registerCommand(this, new QListCommand());
    pm.registerCommand(this, new QMoveCommand());
    pm.registerCommand(this, new QRepeatCommand());
    pm.registerCommand(this, new QSolutionCommand());
    pm.registerCommand(this, new QUnlockCommand());
    pm.registerCommand(this, new QWatchCommand());
  }

  private void registerListener() {
    PluginManager pm = this.getProxy().getPluginManager();

    pm.registerListener(this, new ChatListener());
    pm.registerListener(this, new PostLoginListener());
    pm.registerListener(this, new PlayerDisconnectListener());
  }

  private void initDatabase() {
    sqlManager = new SqlManager();
  }
}
