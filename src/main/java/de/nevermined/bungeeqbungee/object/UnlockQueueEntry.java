package de.nevermined.bungeeqbungee.object;

import java.util.UUID;
import lombok.Getter;

@Getter
public class UnlockQueueEntry {

  private UUID player;

  public UnlockQueueEntry(UUID player) {
    this.player = player;
  }

  protected void polled() {

  }
}
