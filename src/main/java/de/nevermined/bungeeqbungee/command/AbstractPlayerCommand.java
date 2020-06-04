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
import de.nevermined.bungeeqbungee.exception.NoPermissionException;
import de.nevermined.bungeeqbungee.exception.NoPlayerException;
import de.nevermined.bungeeqbungee.exception.WrongInputException;
import de.nevermined.bungeeqbungee.util.Message;
import de.nevermined.bungeeqbungee.util.PermissionHelper;
import java.util.function.Predicate;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPlayerCommand extends Command implements TabExecutor {

  public AbstractPlayerCommand(String name, PermissionHelper permissionHelper, String... aliases) {
    super(
        name,
        permissionHelper != null ? permissionHelper.getPermission() : null,
        aliases
    );
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    try {
      if (!(commandSender instanceof ProxiedPlayer)) {
        throw new NoPlayerException();
      }
      ProxiedPlayer player = (ProxiedPlayer) commandSender;

      if (getPartPermission() != null && !player.hasPermission(getPartPermission().getPermission())) {
        throw new NoPermissionException();
      }

      if (getArgsNeededLength() != null && !getArgsNeededLength().test(args.length)) {
        throw new WrongInputException(this);
      }

      onCommand(player, args);

    } catch (BungeeQException e) {
      commandSender.sendMessage(e.getMessageComponent());
    }
  }

  public abstract void onCommand(ProxiedPlayer player, String[] args) throws BungeeQException;

  @Nullable
  public abstract Predicate<Integer> getArgsNeededLength();

  @NotNull
  public abstract String getPartCommand();

  @Nullable
  public abstract PermissionHelper getPartPermission();

  @Nullable
  public abstract Message getInfoMessage();

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    return ImmutableList.of();
  }
}
