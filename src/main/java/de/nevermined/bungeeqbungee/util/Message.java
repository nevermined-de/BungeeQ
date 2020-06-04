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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public enum Message {

  OUTPUT_PREFIX(ChatColor.DARK_AQUA + "[Q] "),

  WRONG_INPUT(OUTPUT_PREFIX, ChatColor.RED + "Benutze: " + ChatColor.GRAY + "/%commandString%", "%commandString%"),
  NO_PERMISSION(ChatColor.DARK_RED + "Du hast wohl keine Rechte für diesen Befehl"),
  NO_PLAYER(ChatColor.DARK_RED + "Du musst ein Spieler sein, um das zu machen"),
  NOT_IMPLEMENTED_EXCEPTION(ChatColor.DARK_RED + "Dieses Feature ist noch nicht implementiert"),
  CONFIG_RELOAD_SUCCESSFUL(OUTPUT_PREFIX, "Das Laden der Konfigurationsdatei war erfolgreich!"),
  CONFIG_RELOAD_NOT_SUCCESSFUL(OUTPUT_PREFIX, "Das Laden der Konfigurationsdatei war nicht erfolgreich!"),

  NOT_IN_UNLOCK_SESSION_PLAYER(OUTPUT_PREFIX, ChatColor.AQUA + "Du bist zurzeit in keiner Freischaltung!"),
  NOT_IN_UNLOCK_SESSION_UNLOCKER(OUTPUT_PREFIX, ChatColor.AQUA + "Du schaltest zurzeit niemanden frei!"),
  YOU_UNLOCK_PLAYER(OUTPUT_PREFIX, ChatColor.AQUA + "Du schaltest zurzeit %PLAYER% frei!", "%PLAYER%"),
  YOU_UNLOCK_SOMEBODY(OUTPUT_PREFIX, ChatColor.AQUA + "Du schaltest nun jemanden frei!"),
  QUEUE_IS_EMPTY(OUTPUT_PREFIX, ChatColor.AQUA + "Zurzeit ist niemand in der Warteschlange!"),
  PLAYER_HAS_NO_HISTORY(OUTPUT_PREFIX, ChatColor.AQUA + "Dieser Spieler hat keine Freischaltgeschichte!"),
  YOU_WATCH_NOBODY(OUTPUT_PREFIX, ChatColor.AQUA + "Du beobachtest zurzeit keine Freischaltung!"),
  YOU_WATCH_NOT_MORE(OUTPUT_PREFIX, ChatColor.AQUA + "Du beobachtest die Freischaltung nicht mehr!"),
  THIS_CAN_NOT_WATCH(OUTPUT_PREFIX, ChatColor.AQUA + "Diese Freischaltung kann nicht beobachtet werden!"),
  YOU_ARE_WATCHING(OUTPUT_PREFIX, ChatColor.AQUA + "Du beobachtest nun die Freischaltung!"),

  COMMAND_BUNGEE_Q_USE("%COMMAND% <reload|update>", "%COMMAND%"),
  COMMAND_Q_CHAT_USE("%COMMAND% <Nachricht ...>", "%COMMAND%"),
  COMMAND_Q_DECLINE_USE("%COMMAND% <Notiz ...>", "%COMMAND%"),
  COMMAND_Q_EXIT_USE("%COMMAND% [<Notiz ...>]", "%COMMAND%"),
  COMMAND_Q_HISTORY_USE("%COMMAND% <Spieler>", "%COMMAND%"),
  COMMAND_Q_UNLOCK_USE("%COMMAND% <Notiz ...>", "%COMMAND%"),
  COMMAND_Q_WATCH_USE("%COMMAND% [Spieler]", "%COMMAND%"),

  HISTORY_HEADLINE(ChatColor.DARK_AQUA + "Freischaltgeschichte von " + ChatColor.GRAY + "%USER%" + ChatColor.DARK_AQUA + ":", "%USER%"),
  HISTORY_UNLOCKER(ChatColor.AQUA + " ╔ Freischaltung bei " + ChatColor.GRAY + "%UNLOCKER%", "%UNLOCKER%"),
  HISTORY_START(ChatColor.AQUA + " ╠ Startzeitpunkt: " + ChatColor.GRAY + "%START%", "%START%"),
  HISTORY_END(ChatColor.AQUA + " ╠ Endzeitpunkt: " + ChatColor.GRAY + "%END%", "%END%"),
  HISTORY_STATUS(ChatColor.AQUA + " ╠ Status: " + "%STATUS%", "%STATUS%"),
  HISTORY_NOTICE(ChatColor.AQUA + " ╚ Notiz: " + ChatColor.GRAY + "%NOTICE%", "%NOTICE%"),

  ALREADY_IN_UNLOCK_SESSION(OUTPUT_PREFIX, ChatColor.AQUA + "Du bist zurzeit in einer Freischaltung!"),
  ALREADY_IN_UNLOCK_QUEUE(OUTPUT_PREFIX, ChatColor.AQUA + "Du bist der Warteschlange bereits beigetreten!"),
  JOIN_IN_UNLOCK_QUEUE(OUTPUT_PREFIX, ChatColor.AQUA + "Du bist der Warteschlange beigetreten!"),
  PLAYER_JOINED_UNLOCK_QUEUE(OUTPUT_PREFIX, ChatColor.AQUA + "%USER% ist der Warteschlange beigetreten!", "%USER%"),

  NO_MORE_QUESTIONS_LEFT(OUTPUT_PREFIX, ChatColor.AQUA + "Es keine weiteren Fragen verfügbar!"),
  BREAK_BY_GUEST("Abbrechen durch Gast"),
  BREAK_BY_PERSON(OUTPUT_PREFIX, ChatColor.AQUA + "Die Freischaltung von %USER% wurde von %BREAKER% abgebrochen", "%USER%", "%BREAKER%"),
  UNLOCKER_GET_PLAYER(OUTPUT_PREFIX, ChatColor.AQUA + "%UNLOCKER% schaltet nun %USER% frei.", "%UNLOCKER%", "%USER%"),
  QUEUE_HAS_USERS(OUTPUT_PREFIX, ChatColor.AQUA + "Es sind noch %COUNT%  Gäste in der Warteschlange", "%DECLINER%"),
  UNLOCK_DELINE(OUTPUT_PREFIX, ChatColor.AQUA + "Die Freischaltung wurde abgelehnt!"),
  DECLINED_MESSAGE(OUTPUT_PREFIX, ChatColor.AQUA + "Die Freischaltung von %DECLINER% wurde von %DECLINED% abgelehnt!", "%DECLINER%", "%DECLINED%"),
  UNLOCK_CANCELLED("Die Freischaltung wurde abgebrochen!"),
  QUESTIONS_LEFT(OUTPUT_PREFIX, ChatColor.AQUA + "Du musst zuerst alle Fragen stellen!"),
  UNLOCK_SUCCESS("Die Freischaltung wurde angenommen!"),
  SUCCESS_MESSAGE(OUTPUT_PREFIX, ChatColor.AQUA + "Die Freischaltung von %USER% wurde von %UNLOCKER% angenommen!", "%USER%", "%UNLOCKER%"),

  ACTUALLY_QUEUES(OUTPUT_PREFIX, ChatColor.AQUA + "Es sind zurzeit " + ChatColor.GRAY + "%QUEUE_SIZE%" + ChatColor.AQUA + " Gäste in der Warteschlange:\n " + ChatColor.GRAY + "%PLAYERS%", "%QUEUE_SIZE%", "%PLAYERS%"),
  ACTUALLY_UNLOCKS(OUTPUT_PREFIX, ChatColor.AQUA + "Es laufen zurzeit " + ChatColor.GRAY + "%UNLOCKS_SIZE%" + ChatColor.AQUA + " Freischaltungen:\n " + ChatColor.GRAY + "%PLAYERS%", "%UNLOCKS_SIZE%", "%PLAYERS%"),
  LIST_QUEUE_SEPARATOR(", "),
  LIST_UNLOCK_SEPARATOR("\n"),
  LIST_UNLOCK_PLAYER_SEPARATOR(" -> "),

  MOD_LIST_ENTRY(ChatColor.AQUA + "%MOD%" + ChatColor.GRAY + "(%VERSION%)", "%MOD%", "%VERSION%"),
  MOD_LIST_SEPARATOR(", "),

  STATUS_RUNNING(ChatColor.GRAY + "Laufend"),
  STATUS_SUCCESSFUL(ChatColor.DARK_GREEN + "Erfolgreich"),
  STATUS_DECLINED(ChatColor.DARK_RED + "Abgelehnt"),
  STATUS_CANCELLED(ChatColor.GOLD + "Abgebrochen"),

  PLAYER_MESSAGE(OUTPUT_PREFIX, ChatColor.GRAY + "%SENDER%" + ChatColor.DARK_AQUA + ": " + ChatColor.AQUA + "%MESSAGE%", "%SENDER%", "%MESSAGE%"),
  PLUGIN_MESSAGE(OUTPUT_PREFIX, ChatColor.GRAY + "BungeeQ" + ChatColor.DARK_AQUA + ": " + ChatColor.AQUA + "%MESSAGE%", "%MESSAGE%"),

  GLOBAL_MESSAGE(ChatColor.YELLOW + "Willkommen auf dem Server, " + ChatColor.GRAY + "%USER%" + ChatColor.YELLOW + "!", "%USER%");

  private String[] variables;
  private String output;

  Message(String output, String... variables) {
    this.output = output;
    this.variables = variables;
  }

  Message(Message preMessage, String output, String... variables) {
    this.output = preMessage.getOutput() + output;
    this.variables = variables;
  }

  private String getOutput(String... values) {
    String string = output;
    for (int i = 0; i < values.length; i++) {
      string = string.replace(variables[i], values[i]);
    }
    return string;
  }

  public String getOutputString(String... values) {
    String output = getOutput(values);
    String outputString = MessageColorUtils.convertString(output);

    return outputString;
  }

  public TextComponent getOutputComponent(String... values) {
    String output = getOutput(values);
    TextComponent outputComponent = MessageColorUtils.convertTextComponent(output);

    return outputComponent;
  }
}
