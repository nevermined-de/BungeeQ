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
import de.nevermined.bungeeqbungee.object.UnlockStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class BqUnlocks {

  private static final Logger LOGGER = BungeeQBungeePlugin.getInstance().getLogger();

  private int id;
  private String target;
  private String unlocker;
  private LocalDateTime start;
  private LocalDateTime end;
  private UnlockStatus status;
  private String notice;

  public BqUnlocks(
      String target,
      String unlocker,
      LocalDateTime start,
      LocalDateTime end,
      UnlockStatus status,
      String notice) {
    this.target = target;
    this.unlocker = unlocker;
    this.start = start;
    this.end = end;
    this.status = status;
    this.notice = notice;
  }

  private BqUnlocks(
      int id,
      String target,
      String unlocker,
      LocalDateTime start,
      LocalDateTime end,
      UnlockStatus status,
      String notice) {
    this.id = id;
    this.target = target;
    this.unlocker = unlocker;
    this.start = start;
    this.end = end;
    this.status = status;
    this.notice = notice;
  }

  public boolean persist() {
    boolean success = false;

    ConnectionPoolManager connectionPool = BungeeQBungeePlugin.getInstance().getSqlManager().getPool();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String prefix = ConfigManager.getInstance().getMySqlPrefix();

    try {
      conn = connectionPool.getConnection();
      ps = conn.prepareStatement(
          "INSERT INTO " + prefix + "bq_unlocks "
              + "(`target_uuid`, `unlocker_uuid`, `start_datetime`, `end_datetime`, `status`, `notice`) "
              + "VALUES "
              + "( ? , ? , ? , ? , ? , ? );");
      ps.setString(1, target);
      ps.setString(2, unlocker);
      ps.setObject(3, start);
      ps.setObject(4, end);
      ps.setString(5, status.getColumnName());
      ps.setString(6, notice);
      ps.executeUpdate();

      ps = conn.prepareStatement(
          "SELECT LAST_INSERT_ID() AS id;"
      );
      rs = ps.executeQuery();
      if (rs.next()) {
        this.id = rs.getInt("id");
        success = true;
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Error: ", e);
    } finally {
      connectionPool.close(conn, ps, rs);
    }
    return success;
  }

  @NotNull
  public static Collection<BqUnlocks> getBqUnlocksByTarget(UUID targetUuid) {
    Collection<BqUnlocks> bqUnlocks = new ArrayList<>();

    ConnectionPoolManager connectionPool = BungeeQBungeePlugin.getInstance().getSqlManager().getPool();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String prefix = ConfigManager.getInstance().getMySqlPrefix();

    try {
      conn = connectionPool.getConnection();
      ps = conn.prepareStatement(
          "SELECT * "
              + "FROM " + prefix + "bq_unlocks "
              + "WHERE target_uuid = ? ;");
      ps.setString(1, targetUuid.toString());

      rs = ps.executeQuery();

      while (rs.next()) {
        BqUnlocks bqUnlocksEntry;

        int id = rs.getInt("id");
        String target = rs.getString("target_uuid");
        String unlocker = rs.getString("unlocker_uuid");
        LocalDateTime start = rs.getObject("start_datetime", LocalDateTime.class);
        LocalDateTime end = rs.getObject("end_datetime", LocalDateTime.class);
        String statusColumnName = rs.getString("status");
        String notice = rs.getString("notice");

        Optional<UnlockStatus> status = UnlockStatus.byColumnName(statusColumnName);

        if (status.isPresent()) {
          bqUnlocksEntry = new BqUnlocks(id, target, unlocker, start, end, status.get(), notice);
          bqUnlocks.add(bqUnlocksEntry);
        }
      }
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Error: ", e);
    } finally {
      connectionPool.close(conn, ps, rs);
    }

    return bqUnlocks;
  }
}
