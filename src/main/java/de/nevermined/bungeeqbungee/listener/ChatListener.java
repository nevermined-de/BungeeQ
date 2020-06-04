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

package de.nevermined.bungeeqbungee.listener;

import de.nevermined.bungeeqbungee.command.ExitCommand;
import de.nevermined.bungeeqbungee.object.UnlockSession;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.UnlockManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChat(ChatEvent event) {
    if (event.getSender() instanceof ProxiedPlayer) {

      ProxiedPlayer player = (ProxiedPlayer) event.getSender();

      UnlockSession unlockSession = UnlockManager.getInstance()
          .getUnlockByTarget(player.getUniqueId());

      if (unlockSession != null) {
        event.setCancelled(true);

        String message = event.getMessage();

        if (("/" + ExitCommand.getStaticPartCommand()).equalsIgnoreCase(message)) {
          unlockSession.setNotice(Message.BREAK_BY_GUEST.getOutputString());
          unlockSession.cancel(false);
        } else {
          unlockSession.sendMessage(
              Message.PLAYER_MESSAGE.getOutputComponent(player.getName(), message));
        }
      }
    }
  }
}
