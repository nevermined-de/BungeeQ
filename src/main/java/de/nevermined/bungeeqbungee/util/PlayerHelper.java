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

package de.nevermined.bungeeqbungee.util;

import de.nevermined.bungeeqbungee.BungeeQBungeePlugin;
import de.nevermined.bungeeqbungee.config.ConfigManager;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.messaging.MessagingService;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

public class PlayerHelper {

  private static final Logger LOGGER = BungeeQBungeePlugin.getInstance().getLogger();

  public static ProxiedPlayer getPlayerFromUUID(UUID uuid) {
    return ProxyServer.getInstance().getPlayer(uuid);
  }

  @Nullable
  public static String getPlayerNameFromUUID(UUID uuid) {
    String result = null;

    ProxiedPlayer player = getPlayerFromUUID(uuid);

    if (player == null) {
      try {
        result = LuckPermsProvider.get().getUserManager().lookupUsername(uuid).get();
      } catch (InterruptedException | ExecutionException ex) {
        LOGGER.log(Level.SEVERE, "Error: ", ex);
      }
    } else {
      return player.getName();
    }

    return result;
  }

  public static UUID getUUIDFromPlayerName(String playerName) {

    UUID result = null;

    try {
      result = LuckPermsProvider.get().getUserManager().lookupUniqueId(playerName).get();
    } catch (InterruptedException | ExecutionException ex) {
      LOGGER.log(Level.SEVERE, "Error: ", ex);
    }

    return result;
  }

  public static void setUserGroup(UUID userUuid, String group) {
    LuckPerms luckPerms = LuckPermsProvider.get();
    UserManager userManager = luckPerms.getUserManager();
    Optional<MessagingService> messagingService = luckPerms.getMessagingService();

    User user = userManager.getUser(userUuid);

    assert user != null;
    user.data().clear();

    Node n = Node.builder("group." + group).build();

    user.data().add(n);

    user.setPrimaryGroup(ConfigManager.getInstance().getUnlockGroup());

    userManager.saveUser(user);

    if (messagingService.isPresent()) {
      messagingService.get().pushUserUpdate(user);
    }
  }
}
