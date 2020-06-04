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
import de.nevermined.bungeeqbungee.object.UnlockSession;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.MessageColorUtils;
import de.nevermined.bungeeqbungee.util.PermissionHelper;
import de.nevermined.bungeeqbungee.util.PlayerHelper;
import de.nevermined.bungeeqbungee.util.UnlockManager;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class QGetCommand extends AbstractPlayerCommand {

  private static String partCommand = "qget";
  private static PermissionHelper partPermission = PermissionHelper.COMMAND_Q_GET;
  private static String[] aliases = {"qg"};
  private Message infoMessage;
  private Predicate<Integer> argsNeededLength = (l -> l == 0);

  public QGetCommand() {
    super(partCommand, partPermission, aliases);
  }

  @Override
  public void onCommand(ProxiedPlayer sender, String[] args) throws BungeeQException {
    UnlockManager unlockManager = UnlockManager.getInstance();
    UnlockSession unlockSession = unlockManager.getUnlockByUnlocker(sender.getUniqueId());

    if (unlockSession != null) {
      String userName = PlayerHelper.getPlayerNameFromUUID(unlockSession.getTargetUuid());

      throw new AlreadyUnlockingPlayerException(userName);
    }

    unlockManager.unqueuePlayer(sender.getUniqueId());

    sender.sendMessage(Message.YOU_UNLOCK_SOMEBODY.getOutputComponent());

    UnlockSession session = unlockManager.getUnlockByUnlocker(sender.getUniqueId());

    ProxiedPlayer targetPlayer = PlayerHelper.getPlayerFromUUID(session.getTargetUuid());

    sender.sendMessage(getModsInfoMessage(targetPlayer.getModList()));

    String question = session.getQuestions().getNext();

    session.sendMessage(
        Message.PLAYER_MESSAGE.getOutputComponent(sender.getName(), question));
  }

  public TextComponent getModsInfoMessage(Map<String, String> modMap) {
    String modList = modMap.entrySet().stream()
        .map(entry -> Message.MOD_LIST_ENTRY.getOutputString(entry.getKey(), entry.getValue()))
        .collect(Collectors.joining(Message.MOD_LIST_SEPARATOR.getOutputString()));

    TextComponent modListComponent = MessageColorUtils.convertTextComponent(modList);
    return modListComponent;
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
}
