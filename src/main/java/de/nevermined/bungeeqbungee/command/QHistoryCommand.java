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
import de.nevermined.bungeeqbungee.database.table.BqUnlocks;
import de.nevermined.bungeeqbungee.exception.BungeeQException;
import de.nevermined.bungeeqbungee.exception.HistoryEmptyException;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PermissionHelper;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class QHistoryCommand extends AbstractPlayerCommand {

  private static String partCommand = "qhistory";
  private static PermissionHelper partPermission = PermissionHelper.COMMAND_Q_HISTORY;
  private static String[] aliases = {"qh"};
  private Message infoMessage = Message.COMMAND_Q_HISTORY_USE;
  private Predicate<Integer> argsNeededLength = (l -> l == 1);

  public QHistoryCommand() {
    super(partCommand, partPermission, aliases);
  }

  @Override
  public void onCommand(ProxiedPlayer sender, String[] args) throws BungeeQException {
    String userName = args[0];

    printHistory(sender, userName);
  }

  private void printHistory(ProxiedPlayer player, String userName) {
    CompletableFuture.runAsync(() -> {

      try {
        UUID targetUuid = PlayerHelper.getUUIDFromPlayerName(userName);
        Collection<BqUnlocks> unlockHistory = BqUnlocks.getBqUnlocksByTarget(targetUuid);

        if (unlockHistory.isEmpty()) {
          throw new HistoryEmptyException();
        }

        player.sendMessage(Message.HISTORY_HEADLINE.getOutputComponent(userName));

        for (BqUnlocks unlock : unlockHistory) {
          sendHistoryEntry(player, unlock);
        }

      } catch (BungeeQException e) {
        player.sendMessage(e.getMessageComponent());
      }
    });
  }

  private void sendHistoryEntry(ProxiedPlayer player, BqUnlocks unlock) {
    String unlockerName = PlayerHelper.getPlayerNameFromUUID(UUID.fromString(unlock.getUnlocker()));
    player.sendMessage(
        Message.HISTORY_UNLOCKER.getOutputComponent(
            unlockerName));
    player.sendMessage(
        Message.HISTORY_START.getOutputComponent(
            unlock.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    player.sendMessage(
        Message.HISTORY_END.getOutputComponent(
            unlock.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    player.sendMessage(
        Message.HISTORY_STATUS.getOutputComponent(
            unlock.getStatus().getMessage().getOutputString()));
    player.sendMessage(
        Message.HISTORY_NOTICE.getOutputComponent(
            unlock.getNotice()));
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
      tabComplete = ProxyServer.getInstance()
          .getPlayers().stream()
          .map(ProxiedPlayer::getName)
          .filter(s -> args.length != 1 || s.startsWith(args[0]))
          .collect(Collectors.toList());
    } else {
      tabComplete = ImmutableList.of();
    }

    return tabComplete;
  }
}
