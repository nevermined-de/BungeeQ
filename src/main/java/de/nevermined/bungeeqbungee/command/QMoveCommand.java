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

import de.nevermined.bungeeqbungee.exception.AlreadyUnlockingPlayerException;
import de.nevermined.bungeeqbungee.exception.BungeeQException;
import de.nevermined.bungeeqbungee.exception.NotMoveableException;
import de.nevermined.bungeeqbungee.object.TwoKeyMap;
import de.nevermined.bungeeqbungee.object.UnlockSession;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PermissionHelper;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import de.nevermined.bungeeqbungee.util.UnlockManager;
import java.util.UUID;
import java.util.function.Predicate;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class QMoveCommand extends AbstractPlayerCommand {

  private static String partCommand = "qmove";
  private static PermissionHelper partPermission = PermissionHelper.COMMAND_Q_MOVE;
  private static String[] aliases = {"qm"};
  private Message infoMessage = Message.COMMAND_Q_MOVE_USE;
  private Predicate<Integer> argsNeededLength = (l -> l == 1);

  public QMoveCommand() {
    super(partCommand, partPermission, aliases);
  }

  @Override
  public void onCommand(ProxiedPlayer sender, String[] args) throws BungeeQException {
    UnlockManager unlockManager = UnlockManager.getInstance();
    UnlockSession unlockSession = unlockManager.getUnlockByUnlocker(sender.getUniqueId());

    if (unlockSession != null) {
      String userName = unlockSession.getTargetName();

      throw new AlreadyUnlockingPlayerException(userName);
    }

    String userName = args[0];
    UUID userUuid = PlayerHelper.getUUIDFromPlayerName(userName);

    if (userUuid == null) {
      throw new NotMoveableException();
    }

    UnlockSession session = unlockManager.getUnlockByTarget(userUuid);

    if (session == null) {
      throw new NotMoveableException();
    }

    TwoKeyMap<UUID, UUID, UnlockSession> runningUnlocks = unlockManager.getRunningUnlocks();
    UUID targetUuid = session.getTargetUuid();
    runningUnlocks.removeByKey1(targetUuid);

    session.setUnlocker(sender.getUniqueId());
    runningUnlocks.put(targetUuid, sender.getUniqueId(), session);
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
    Iterable<String> tabComplete = UnlockManager.getInstance().getTargetNamesTabComplete(args);
    return tabComplete;
  }
}
