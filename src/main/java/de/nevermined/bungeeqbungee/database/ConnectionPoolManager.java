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

package de.nevermined.bungeeqbungee.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.nevermined.bungeeqbungee.config.ConfigManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager {

  private HikariDataSource dataSource;

  private String hostname;
  private String port;
  private String database;
  private String username;
  private String password;

  private int minimumConnections;
  private int maximumConnections;
  private long connectionTimeout;
  private String testQuery;

  public ConnectionPoolManager() {
    init();
    setupPool();
  }

  private void init() {
    ConfigManager config = ConfigManager.getInstance();
    this.hostname = config.getMySqlHost();
    this.port = config.getMySqlPort();
    this.database = config.getMySqlDatabase();
    this.username = config.getMySqlUser();
    this.password = config.getMySqlPassword();
    this.minimumConnections = 3;
    this.maximumConnections = 10;
    this.connectionTimeout = 180000;
    this.testQuery = "SELECT version();";
  }

  private void setupPool() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(
        "jdbc:mysql://" +
            this.hostname +
            ":" +
            this.port +
            "/" +
            this.database
    );
    config.setDriverClassName("com.mysql.jdbc.Driver");
    config.setUsername(this.username);
    config.setPassword(this.password);
    config.setMinimumIdle(this.minimumConnections);
    config.setMaximumPoolSize(this.maximumConnections);
    config.setConnectionTimeout(this.connectionTimeout);
    config.setConnectionTestQuery(this.testQuery);
    this.dataSource = new HikariDataSource(config);
  }

  public Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }

  public void close(Connection conn, PreparedStatement ps, ResultSet res) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException ignored) {
      }
    }
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException ignored) {
      }
    }
    if (res != null) {
      try {
        res.close();
      } catch (SQLException ignored) {
      }
    }
  }

  public void closePool() {
    if (this.dataSource != null && !this.dataSource.isClosed()) {
      this.dataSource.close();
    }
  }
}
