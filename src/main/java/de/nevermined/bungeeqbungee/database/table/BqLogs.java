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

package de.nevermined.bungeeqbungee.database.table;

import de.nevermined.bungeeqbungee.BungeeQBungeePlugin;
import de.nevermined.bungeeqbungee.config.ConfigManager;
import de.nevermined.bungeeqbungee.database.ConnectionPoolManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BqLogs {

  private static final Logger LOGGER = BungeeQBungeePlugin.getInstance().getLogger();

  private int id;
  private String log;

  public boolean persist() {
    boolean success = false;

    ConnectionPoolManager connectionPool = BungeeQBungeePlugin.getInstance().getSqlManager().getPool();
    Connection conn = null;
    PreparedStatement ps = null;
    String prefix = ConfigManager.getInstance().getMySqlPrefix();

    try {
      conn = connectionPool.getConnection();
      ps = conn.prepareStatement(
          "INSERT INTO " + prefix + "bq_logs "
              + "(`unlock_id`, `log`) "
              + "VALUES "
              + "( ? , ? );");
      ps.setInt(1, id);
      ps.setString(2, log);
      ps.executeUpdate();
      success = true;
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Error: ", e);
    } finally {
      connectionPool.close(conn, ps, null);
    }
    return success;
  }
}
