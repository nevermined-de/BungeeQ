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

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageColorUtils {

  private static final Map<String, ChatColor> TOKEN_MAP = ImmutableMap.<String, ChatColor>builder()
      .put("§l", ChatColor.BOLD)
      .put("§o", ChatColor.ITALIC)
      .put("§n", ChatColor.UNDERLINE)
      .put("§m", ChatColor.STRIKETHROUGH)
      .put("§k", ChatColor.MAGIC)
      .put("§r", ChatColor.RESET)
      .put("§f", ChatColor.WHITE)
      .put("§0", ChatColor.BLACK)
      .put("§b", ChatColor.AQUA)
      .put("§3", ChatColor.DARK_AQUA)
      .put("§9", ChatColor.BLUE)
      .put("§1", ChatColor.DARK_BLUE)
      .put("§6", ChatColor.GOLD)
      .put("§7", ChatColor.GRAY)
      .put("§8", ChatColor.DARK_GRAY)
      .put("§d", ChatColor.LIGHT_PURPLE)
      .put("§5", ChatColor.DARK_PURPLE)
      .put("§a", ChatColor.GREEN)
      .put("§2", ChatColor.DARK_GREEN)
      .put("§c", ChatColor.RED)
      .put("§4", ChatColor.DARK_RED)
      .put("§e", ChatColor.YELLOW)
      .build();

  public static String convertString(String s) {
    ChatColor.translateAlternateColorCodes('&', s);
    return s;
  }

  public static TextComponent convertTextComponent(String s) {
    ArrayList<TextComponent> textComponents = new ArrayList<>();

    int endOfLastSubstring = 0;
    ChatColor lastChatColor = ChatColor.WHITE;

    for (int i = 0; i < s.length(); i++) {
      if (i == s.length() - 1) {
        TextComponent textComponent = new TextComponent(s.substring(endOfLastSubstring, i + 1));
        textComponent.setColor(lastChatColor);
        textComponents.add(textComponent);
      }
      if (s.charAt(i) == '§') {
        if (i != 0) {
          TextComponent textComponent = new TextComponent(s.substring(endOfLastSubstring, i));
          textComponent.setColor(lastChatColor);
          textComponents.add(textComponent);
        }

        endOfLastSubstring = i + 2;

        String colorCode = s.substring(i, i + 2);
        lastChatColor = TOKEN_MAP.getOrDefault(colorCode, lastChatColor);
      }
    }
    TextComponent completeComponent = new TextComponent();
    for (TextComponent textComponent : textComponents) {
      completeComponent.addExtra(textComponent);
    }
    return completeComponent;
  }
}
