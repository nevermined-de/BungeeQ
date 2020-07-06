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
