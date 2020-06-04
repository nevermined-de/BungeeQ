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

import de.nevermined.bungeeqbungee.util.Message;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum UnlockStatus {

  RUNNING("running", Message.STATUS_RUNNING),
  SUCCESSFUL("successful", Message.STATUS_SUCCESSFUL),
  CANCELLED("cancelled", Message.STATUS_CANCELLED),
  DECLINED("declined", Message.STATUS_DECLINED);

  private String columnName;
  private Message message;

  UnlockStatus(String columnName, Message message) {
    this.columnName = columnName;
    this.message = message;
  }

  public static Optional<UnlockStatus> byColumnName(String columnName) {
    Optional<UnlockStatus> optionalUnlockStatus = Arrays.stream(UnlockStatus.values())
        .filter(unlockStatus -> unlockStatus.getColumnName().equals(columnName))
        .findFirst();
    return optionalUnlockStatus;
  }
}
