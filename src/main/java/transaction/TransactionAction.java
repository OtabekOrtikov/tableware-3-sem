package transaction;

import java.sql.Connection;

@FunctionalInterface
public interface TransactionAction<T> {
    T execute(Connection connection) throws Exception;
}

