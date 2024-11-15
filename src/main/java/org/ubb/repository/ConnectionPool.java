package org.ubb.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;
//    Private constructor to prevent instantiation
    private ConnectionPool() {}

//    ConnectionPool Class initializes the pool in a static block,
//    The connection pool is created when the class is loaded, and it is available globally
    static{
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://ep-tiny-surf-a5253x32.us-east-2.aws.neon.tech/Book_store");
        config.setUsername(System.getenv("neon_postgres_user"));
        config.setPassword(System.getenv("neon_postgres_pass"));
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(300000);
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
/*
    Spring Boot folosește HikariCP ca connection pool implicit pentru sursele de date.
Începând cu Spring Boot 2.0, HikariCP a fost ales ca connection pool implicit
datorită performanței ridicate și a consumului redus de resurse în comparație cu alte
opțiuni precum Tomcat Connection Pool sau Apache Commons DBCP.

 */
