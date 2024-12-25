package transaction;

import org.otabek.exceptions.ConnectionPoolException;
import org.otabek.exceptions.DaoException;
import org.otabek.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final ConnectionPool connectionPool;

    public TransactionManager(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void beginTransaction(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.setAutoCommit(false);
        } else {
            throw new SQLException("Cannot begin transaction. Connection is closed.");
        }
    }

    public void commitTransaction(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
        } else {
            throw new SQLException("Cannot commit transaction. Connection is closed.");
        }
    }

    public void rollbackTransaction(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
        } else {
            throw new SQLException("Cannot rollback transaction. Connection is closed.");
        }
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() throws DaoException {
        try {
            return connectionPool.takeConnection();
        } catch (ConnectionPoolException e) {
            throw new DaoException("Error obtaining connection from pool", e);
        }
    }
}