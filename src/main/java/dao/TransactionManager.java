package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionManager implements AutoCloseable {
	// データベース接続に使用する情報
	private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/taskmanage";
	private final String DB_USER = "sa";
	private final String DB_PASS = "";

	private final Connection connection;

	public TransactionManager() throws SQLException {
		this.connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
		this.connection.setAutoCommit(false); // トランザクションの開始
	}

	public Connection getConnection() {
		return connection;
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	@Override
	public void close() throws SQLException {
		connection.close();
	}
}
