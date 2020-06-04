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

import de.nevermined.bungeeqbungee.config.ConfigManager;
import de.nevermined.bungeeqbungee.exception.AlreadyInUnlockQueueException;
import de.nevermined.bungeeqbungee.exception.AlreadyInUnlockSessionException;
import de.nevermined.bungeeqbungee.exception.QueueEmptyException;
import de.nevermined.bungeeqbungee.object.TwoKeyMap;
import de.nevermined.bungeeqbungee.object.UnlockSession;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class UnlockManager {

  private static UnlockManager instance = null;
  private Set<UUID> availableUnlockers = new HashSet<>();
  @Getter
  private Queue<UUID> unlockQueue = new LinkedList<>();
  @Getter
  private TwoKeyMap<UUID, UUID, UnlockSession> runningUnlocks = new TwoKeyMap<>();

  private UnlockManager() {
  }

  private void startUnlock(UUID targetUuid, UUID unlockerUuid) {
    UnlockSession session = new UnlockSession(targetUuid, unlockerUuid);
    runningUnlocks.put(targetUuid, unlockerUuid, session);
  }

  public void queuePlayer(UUID target) throws AlreadyInUnlockSessionException, AlreadyInUnlockQueueException {
    if (getUnlockByTarget(target) != null) {
      throw new AlreadyInUnlockSessionException();
    }

    if (this.unlockQueue.contains(target)) {
      throw new AlreadyInUnlockQueueException();
    }

    this.unlockQueue.add(target);

    String userName = ProxyServer
        .getInstance()
        .getPlayer(target)
        .getName();

    this.sendUnlockerMessage(
        Message.PLAYER_JOINED_UNLOCK_QUEUE.getOutputComponent(userName),
        true);
  }

  public void unqueuePlayer(UUID unlocker) throws QueueEmptyException {
    if (this.unlockQueue.isEmpty()) {
      throw new QueueEmptyException();
    }

    UUID target = this.unlockQueue.poll();

    startUnlock(target, unlocker);

    if (!this.unlockQueue.isEmpty()) {
      this.sendUnlockerMessage(Message.QUEUE_HAS_USERS.getOutputComponent(
          this.unlockQueue.size() + ""
      ), false);
    }
  }

  public void sendUnlockerMessage(TextComponent message, boolean sendSound) {
    this.availableUnlockers.stream()
        .map(unlockerUuid -> ProxyServer.getInstance().getPlayer(unlockerUuid))
        .filter(Objects::nonNull)
        .forEach(unlocker ->
        {
          unlocker.sendMessage(message);

          if (sendSound) {
            ServerCommunicationHelper.sendPlayerServerMessage(
                unlocker.getServer(),
                "sound",
                unlocker.getUniqueId().toString(),
                ConfigManager.getInstance().getInformationSound());
          }
        });
  }

  public UnlockSession getUnlockByTarget(UUID target) {
    UnlockSession session = this.runningUnlocks.getByKey1(target);

    return session;
  }

  public UnlockSession getUnlockByUnlocker(UUID unlocker) {
    UnlockSession session = this.runningUnlocks.getByKey2(unlocker);

    return session;
  }

  public void addPossibleUnlocker(UUID player) {
    this.availableUnlockers.add(player);
  }

  public void removePossibleUnlocker(UUID player) {
    this.availableUnlockers.remove(player);
  }

  public UnlockSession getUnlockByWatcher(UUID watcherUUID) {
    UnlockSession session = this.runningUnlocks.values()
        .stream()
        .filter(unlock -> unlock.getWatchers().contains(watcherUUID))
        .findFirst()
        .orElse(null);
    return session;
  }

  public void removePlayerFromQueue(UUID player) {
    this.unlockQueue.remove(player);
  }

  public static UnlockManager getInstance() {
    if (instance == null) {
      instance = new UnlockManager();
    }
    return instance;
  }
}
