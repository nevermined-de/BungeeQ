package de.nevermined.bungeeqbungee.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class UnlockQueue extends LinkedList<UnlockQueueEntry> {

  public boolean containsPlayer(UUID player) {
    boolean contains = false;
    Iterator<UnlockQueueEntry> iterator = super.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getPlayer().equals(player)) {
        contains = true;
        break;
      }
    }
    return contains;
  }

  public boolean addPlayer(UUID player) {
    UnlockQueueEntry unlockQueueEntry = new UnlockQueueEntry(player);
    boolean result = super.add(unlockQueueEntry);
    return result;
  }

  public boolean removePlayer(UUID player) {
    boolean result = false;
    Iterator<UnlockQueueEntry> iterator = super.iterator();
    while (iterator.hasNext()) {
      UnlockQueueEntry entry = iterator.next();
      if (entry.getPlayer().equals(player)) {
        result = super.remove(entry);
        break;
      }
    }
    return result;
  }

  @Override
  public UnlockQueueEntry poll() {
    UnlockQueueEntry entry = super.poll();
    if (entry != null) {
      entry.polled();
    }
    return entry;
  }
}
