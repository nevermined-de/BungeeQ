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

import de.nevermined.bungeeqbungee.config.ConfigManager;
import de.nevermined.bungeeqbungee.database.table.BqLogs;
import de.nevermined.bungeeqbungee.database.table.BqUnlocks;
import de.nevermined.bungeeqbungee.exception.QuestionsLeftException;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import de.nevermined.bungeeqbungee.util.UnlockManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class UnlockSession {

  private UUID targetUuid;
  private String targetName;
  private UUID unlockerUuid;
  private String unlockerName;
  private Map<String, String> messages = new HashMap<>();
  private Set<UUID> watchers = new HashSet<>();
  private LocalDateTime start;
  private LocalDateTime end;
  private UnlockStatus status;
  private Questions questions;
  @Setter
  private String notice = "";
  private List<String> messageLog = new ArrayList<>();

  public UnlockSession(UUID targetUuid, UUID unlockerUuid) {
    this.targetUuid = targetUuid;
    this.unlockerUuid = unlockerUuid;
    this.targetName = ProxyServer.getInstance().getPlayer(targetUuid).getName();
    this.unlockerName = ProxyServer.getInstance().getPlayer(unlockerUuid).getName();
    this.status = UnlockStatus.RUNNING;
    this.start = LocalDateTime.now();
    this.questions = new Questions();

    UnlockManager.getInstance()
        .sendUnlockerMessage(Message.UNLOCKER_GET_PLAYER.getOutputComponent(
            PlayerHelper.getPlayerNameFromUUID(unlockerUuid),
            PlayerHelper.getPlayerNameFromUUID(targetUuid)
        ), true);
  }

  public void sendMessage(TextComponent message) {
    messageLog.add(message.toPlainText());

    Set<ProxiedPlayer> receivers = getReceivers();

    receivers.stream()
        .filter(Objects::nonNull)
        .forEach(receiver -> receiver.sendMessage(message));
  }

  @NotNull
  private Set<@Nullable ProxiedPlayer> getReceivers() {
    Set<ProxiedPlayer> receivers = new HashSet<>();

    receivers.add(ProxyServer.getInstance().getPlayer(unlockerUuid));
    receivers.add(ProxyServer.getInstance().getPlayer(targetUuid));

    this.watchers
        .stream()
        .map(watcherUuid -> ProxyServer
            .getInstance()
            .getPlayer(watcherUuid))
        .forEach(receivers::add);

    return receivers;
  }

  public void decline() {
    this.sendMessage(
        Message.PLUGIN_MESSAGE.getOutputComponent(Message.UNLOCK_DELINE.getOutputString())
    );

    UnlockManager
        .getInstance()
        .sendUnlockerMessage(
            Message.DECLINED_MESSAGE.getOutputComponent(targetName, unlockerName),
            true);

    this.end = LocalDateTime.now();

    this.status = UnlockStatus.DECLINED;

    this.remove();
  }

  public void cancel(boolean byUnlocker) {
    this.sendMessage(
        Message.PLUGIN_MESSAGE.getOutputComponent(Message.UNLOCK_CANCELLED.getOutputString())
    );

    UnlockManager
        .getInstance()
        .sendUnlockerMessage(
            Message.BREAK_BY_PERSON.getOutputComponent(targetName, byUnlocker ? unlockerName : targetName),
            true);

    this.end = LocalDateTime.now();

    this.status = UnlockStatus.CANCELLED;

    this.remove();
  }

  public void accept() throws QuestionsLeftException {
    if (this.questions.hasNext()) {
      throw new QuestionsLeftException();
    }

    this.sendMessage(
        Message.PLUGIN_MESSAGE.getOutputComponent(Message.UNLOCK_SUCCESS.getOutputString())
    );

    UnlockManager.getInstance()
        .sendUnlockerMessage(Message.SUCCESS_MESSAGE.getOutputComponent(targetName, unlockerName), true);

    if (ConfigManager.getInstance().isSendGlobalMessage()) {
      ProxyServer.getInstance().broadcast(
          Message.GLOBAL_MESSAGE.getOutputComponent(this.targetName)
      );
    }

    PlayerHelper.setUserGroup(targetUuid, ConfigManager.getInstance().getUnlockGroup());

    this.end = LocalDateTime.now();

    this.status = UnlockStatus.SUCCESSFUL;

    this.remove();
  }

  private void remove() {
    UnlockManager.getInstance().getRunningUnlocks().removeByKey1(targetUuid);
    CompletableFuture.runAsync(this::persist);
  }

  public void persist() {
    BqUnlocks bqUnlocks =
        new BqUnlocks(
            this.getTargetUuid().toString(),
            this.getUnlockerUuid().toString(),
            this.getStart(),
            this.getEnd(),
            this.getStatus(),
            this.getNotice()
        );

    boolean success = bqUnlocks.persist();

    if (success) {
      BqLogs bqLogs = new BqLogs(
          bqUnlocks.getId(),
          this.getMessageLog().stream()
              .collect(Collectors.joining(System.lineSeparator())));

      bqLogs.persist();
    }
  }

  public void addWatcher(UUID watcher) {
    this.watchers.add(watcher);
  }

  public void removeWatcher(UUID watcher) {
    this.watchers.remove(watcher);
  }
}
