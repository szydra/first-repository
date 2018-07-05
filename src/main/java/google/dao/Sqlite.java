package google.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sqlite implements AutoCloseable {

	private static final Logger log = LogManager.getLogger(Sqlite.class);
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:sql/results.db";

	private Connection connection;

	Sqlite() {
		try {
			Class.forName(DRIVER);
			log.debug("Załadowano sterownik bazy danych.");
			connection = DriverManager.getConnection(DB_URL);
			log.debug("Połączono z bazą danych.");
		} catch (ClassNotFoundException cnfe) {
			throw new DaoException("Brak sterownika bazy danych.", cnfe);
		} catch (SQLException sqle) {
			throw new DaoException("Błąd połączenia z bazą danych.", sqle);
		}
	}

	Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	void executeUpdate(String sqlQuery) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(sqlQuery);
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
			log.debug("Rozłączono z bazą danych.");
		} catch (SQLException sqle) {
			throw new DaoException("Błąd zamknięcia połączenia z bazą danych.", sqle);
		}
	}

}
