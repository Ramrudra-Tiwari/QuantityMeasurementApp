
package com.apps.quantitymeasurementapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConnectionPool {

    private static final Logger logger =
            Logger.getLogger(ConnectionPool.class.getName());

    private static ConnectionPool instance;

    private List<Connection> availableConnections;
    private List<Connection> usedConnections;

    private final int poolSize;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String driverClass;
    private final String testQuery;

    /**
     * Private constructor to initialize the connection pool.
     */
    private ConnectionPool() throws SQLException {

        ApplicationConfig config = ApplicationConfig.getInstance();

        this.poolSize = config.getIntProperty("db.pool-size", 5);
        this.dbUrl = config.getProperty("db.url");
        this.dbUsername = config.getProperty("db.username");
        this.dbPassword = config.getProperty("db.password");
        this.driverClass = config.getProperty("db.driver");
        this.testQuery = config.getProperty("db.hikari.connection-test-query", "SELECT 1");

        this.availableConnections = new ArrayList<>();
        this.usedConnections = new ArrayList<>();

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }

        initializeConnections();
    }

    /**
     * Get singleton instance
     */
    public static synchronized ConnectionPool getInstance() throws SQLException {

        if (instance == null) {
            instance = new ConnectionPool();
        }

        return instance;
    }

    /**
     * Initializes the connection pool.
     */
    private void initializeConnections() throws SQLException {

        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createConnection());
        }

        logger.info("Connection pool initialized with size: " + poolSize);
    }

    /**
     * Create new connection
     */
    private Connection createConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(
                dbUrl,
                dbUsername,
                dbPassword
        );

        logger.info("New database connection created");

        return connection;
    }

    /**
     * Acquire connection from pool
     */
    public synchronized Connection getConnection() throws SQLException {

        if (!availableConnections.isEmpty()) {

            Connection connection =
                    availableConnections.remove(availableConnections.size() - 1);

            usedConnections.add(connection);

            return connection;
        }

        if (usedConnections.size() < poolSize) {

            Connection connection = createConnection();
            usedConnections.add(connection);

            return connection;
        }

        throw new SQLException("Maximum pool size reached");
    }

    /**
     * Release connection back to pool
     */
    public synchronized void releaseConnection(Connection connection) {

        if (connection == null) {
            return;
        }

        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    /**
     * Validate connection
     */
    public boolean validateConnection(Connection connection) {

        try (Statement stmt = connection.createStatement()) {

            stmt.execute(testQuery);
            return true;

        } catch (SQLException e) {

            logger.warning("Connection validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close all connections
     */
    public synchronized void closeAll() {

        for (Connection connection : availableConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warning("Error closing available connection: " + e.getMessage());
            }
        }

        for (Connection connection : usedConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warning("Error closing used connection: " + e.getMessage());
            }
        }

        availableConnections.clear();
        usedConnections.clear();

        logger.info("All database connections closed.");
    }

    public int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    public int getUsedConnectionCount() {
        return usedConnections.size();
    }

    public int getTotalConnectionCount() {
        return availableConnections.size() + usedConnections.size();
    }

    public static void main(String[] args) {

        try {

            ConnectionPool pool = ConnectionPool.getInstance();

            Connection conn1 = pool.getConnection();

            logger.info("Validate connection: "
                    + (pool.validateConnection(conn1) ? "Success" : "Failure"));

            logger.info("Available connections after acquiring 1: "
                    + pool.getAvailableConnectionCount());

            logger.info("Used connections after acquiring 1: "
                    + pool.getUsedConnectionCount());

            pool.releaseConnection(conn1);

            logger.info("Available connections after releasing 1: "
                    + pool.getAvailableConnectionCount());

            logger.info("Used connections after releasing 1: "
                    + pool.getUsedConnectionCount());

            pool.closeAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
