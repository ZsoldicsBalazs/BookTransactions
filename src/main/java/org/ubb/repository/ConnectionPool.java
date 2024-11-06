package org.ubb.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;

    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://ep-tiny-surf-a5253x32.us-east-2.aws.neon.tech/postgres");
        config.setUsername(System.getenv("neon_postgres_user"));
        config.setPassword(System.getenv("neon_postgres_pass"));
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
