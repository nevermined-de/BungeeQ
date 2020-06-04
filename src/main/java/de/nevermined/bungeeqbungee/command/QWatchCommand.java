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

package de.nevermined.bungeeqbungee.command;

import com.google.common.collect.ImmutableList;
import de.nevermined.bungeeqbungee.exception.BungeeQException;
import de.nevermined.bungeeqbungee.exception.NotWatchableException;
import de.nevermined.bungeeqbungee.exception.NotWatchingException;
import de.nevermined.bungeeqbungee.object.UnlockSession;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PermissionHelper;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import de.nevermined.bungeeqbungee.util.UnlockManager;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class QWatchCommand extends AbstractPlayerCommand {

  private static String partCommand = "qwatch";
  private static PermissionHelper partPermission = PermissionHelper.COMMAND_Q_WATCH;
  private static String[] aliases = {"qw"};
  private Message infoMessage = Message.COMMAND_Q_WATCH_USE;
  private Predicate<Integer> argsNeededLength = (l -> l == 0 || l == 1);

  public QWatchCommand() {
    super(partCommand, partPermission, aliases);
  }

  @Override
  public void onCommand(ProxiedPlayer sender, String[] args) throws BungeeQException {
    if (args.length == 0) {
      leaveWatch(sender);
    } else {
      String userName = args[0];
      joinWatch(userName, sender);
    }
  }

  private void joinWatch(String userName, ProxiedPlayer player) throws NotWatchableException {
    UUID userUuid = PlayerHelper.getUUIDFromPlayerName(userName);

    if (userUuid == null) {
      throw new NotWatchableException();
    }

    UnlockSession unlockSession = UnlockManager.getInstance().getUnlockByTarget(userUuid);

    if (unlockSession == null) {
      throw new NotWatchableException();
    }

    unlockSession.addWatcher(player.getUniqueId());

    player.sendMessage(Message.YOU_ARE_WATCHING.getOutputComponent());
  }

  private void leaveWatch(ProxiedPlayer player) throws NotWatchingException {
    UnlockSession unlockSession = UnlockManager.getInstance().getUnlockByWatcher(player.getUniqueId());

    if (unlockSession == null) {
      throw new NotWatchingException();
    }

    unlockSession.removeWatcher(player.getUniqueId());
    player.sendMessage(Message.YOU_WATCH_NOT_MORE.getOutputComponent());
  }

  @NotNull
  @Override
  public String getPartCommand() {
    return partCommand;
  }

  @Nullable
  @Override
  public PermissionHelper getPartPermission() {
    return partPermission;
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    Iterable<String> tabComplete;
    if (args.length <= 1) {
      tabComplete = UnlockManager.getInstance().getRunningUnlocks().values().stream()
          .map(UnlockSession::getTargetUuid)
          .map(PlayerHelper::getPlayerNameFromUUID)
          .filter(Objects::nonNull)
          .filter(s -> args.length != 1 || s.startsWith(args[0]))
          .collect(Collectors.toList());
    } else {
      tabComplete = ImmutableList.of();
    }
    return tabComplete;
  }
}
