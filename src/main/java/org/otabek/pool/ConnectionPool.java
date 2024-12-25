package org.otabek.pool;

import org.otabek.exceptions.ConnectionPoolException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionPool {
    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;

    private ConnectionPool() {
    }

    public static ConnectionPool create(String url, String user, String password, int poolSize) throws ConnectionPoolException {
        ConnectionPool connectionPool = new ConnectionPool();
        try {
            connectionPool.availableConnections = new ArrayBlockingQueue<>(poolSize);
            connectionPool.usedConnections = new ArrayBlockingQueue<>(poolSize);

            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                connectionPool.availableConnections.add(connection);
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException("Error initializing connection pool", e);
        }
        return connectionPool;
    }

    public Connection takeConnection() throws ConnectionPoolException {
        try {
            Connection connection = availableConnections.take();
            usedConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Error taking connection from pool", e);
        }
    }

    public void returnConnection(Connection connection) throws ConnectionPoolException {
        try {
            if (usedConnections.remove(connection)) {
                availableConnections.put(connection);
            } else {
                throw new SQLException("Connection was not part of the used connections pool");
            }
        } catch (InterruptedException | SQLException e) {
            throw new ConnectionPoolException("Error returning connection to pool", e);
        }
    }

    public void close() throws ConnectionPoolException {
        try {
            for (Connection connection : availableConnections) {
                connection.close();
            }
            for (Connection connection : usedConnections) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new ConnectionPoolException("Error closing connection pool", e);
        }
    }

    public Connection beginTransaction() throws ConnectionPoolException, SQLException {
        Connection conn = takeConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    public void commitTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }

    public void rollbackTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    public void endTransaction(Connection conn) throws SQLException, ConnectionPoolException {
        if (conn != null) {
            conn.setAutoCommit(true);
            returnConnection(conn);
        }
    }
}
