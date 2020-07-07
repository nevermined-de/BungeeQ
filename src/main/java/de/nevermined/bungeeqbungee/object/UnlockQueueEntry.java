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

package de.nevermined.bungeeqbungee.object;

import de.nevermined.bungeeqbungee.BungeeQBungeePlugin;
import de.nevermined.bungeeqbungee.config.ConfigManager;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class UnlockQueueEntry {

  @Getter
  private long entryTime;
  @Getter
  private UUID player;
  private ScheduledTask afkTimer;

  public UnlockQueueEntry(UUID player) {
    this.player = player;
    entryTime = System.currentTimeMillis();
    afkTimer = BungeeQBungeePlugin.getInstance().getProxy().getScheduler()
        .schedule(
            BungeeQBungeePlugin.getInstance(),
            this::afkTrigger,
            ConfigManager.getInstance().getTimeToAutoResponse(),
            TimeUnit.SECONDS
        );
  }

  protected void polled() {
    this.afkTimer.cancel();
  }

  private void afkTrigger() {
    if (Alternative.getAlternatives().size() > 0) {
      ProxiedPlayer pPlayer = PlayerHelper.getPlayerFromUUID(this.player);

      pPlayer.sendMessage(
          Message.PLUGIN_MESSAGE.getOutputComponent(
              Message.ALTERNATIVE_TITLE.getOutputString()));

      for (Entry<String, Alternative> entry : Alternative.getAlternatives().entrySet()) {
        TextComponent tempComponent = entry.getValue().getInfoText();
        String command = "/activate " + entry.getKey();
        tempComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        pPlayer.sendMessage(tempComponent);
      }
    }
  }
}
