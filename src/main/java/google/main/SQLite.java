package google.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import google.model.Result;

public class SQLite {

	private static final Logger log = LogManager.getLogger(SQLite.class);

	public SQLite() {
		this.connect();
	}

	public static final String DRIVER = "org.sqlite.JDBC";

	public static final String DB_URL = "jdbc:sqlite:sql/results.db";

	private Connection connection;

	private Statement statement;

	private void closeSet(ResultSet resultSet) {
		if (resultSet != null)
			try {
				resultSet.close();
			} catch (SQLException sqle) {
				log.error("Błąd zamknięcia zbioru wyników.", sqle);
			}
	}

	public void connect() {
		try {
			Class.forName(DRIVER);
			log.debug("Załadowano sterownik bazy danych.");
		} catch (ClassNotFoundException cnfe) {
			log.error("Brak sterownika bazy danych.", cnfe);
		}

		try {
			this.connection = DriverManager.getConnection(DB_URL);
			this.statement = connection.createStatement();
			log.debug("Połączono z bazą danych.");
		} catch (SQLException sqle) {
			log.error("Błąd połączenia z bazą danych", sqle);
		}
	}

	public void disconnect() {
		try {
			this.connection.close();
			log.debug("Rozłączono z bazą danych.");
		} catch (SQLException sqle) {
			log.error("Błąd zamknięcia połączenia z bazą danych.", sqle);
		}
	}

	public Result getResult(String query) {
		Result result = null;
		final String sqlQuery = "SELECT * FROM RESULTS " + "WHERE GOOGLE_QUERY = '" + query + "'";
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sqlQuery);
			if (resultSet.next()) {
				result = new Result(query, resultSet.getLong("NUMBER_OF_RESULTS"),
						resultSet.getString("DATE_OF_SEARCH"));
				log.debug("Zapytanie {} znajduje się w bazie danych.", query);
			}
		} catch (SQLException sqle) {
			log.error("Błąd pobierania danych dla zapytania {}.", query, sqle);
		} finally {
			this.closeSet(resultSet);
		}
		return result;
	}

	public void insertResult(String query, Long numberOfResults) {
		final String sqlQuery = "INSERT INTO RESULTS (GOOGLE_QUERY, " + "NUMBER_OF_RESULTS, DATE_OF_SEARCH) VALUES ('"
				+ query + "', '" + numberOfResults + "', '" + LocalDate.now() + "')";
		try {
			statement.executeUpdate(sqlQuery);
			log.debug("Zapytanie {} dodano do bazy danych.", query);
		} catch (SQLException sqle) {
			log.error("Podczas dodawania zapytania {} do bazy danych wystąpił błąd.", query, sqle);
		}
	}

	public void removeResult(String query) {
		final String sqlQuery = "DELETE FROM RESULTS WHERE GOOGLE_QUERY = '" + query + "'";
		try {
			statement.executeUpdate(sqlQuery);
			log.info("Zapytanie {} usunięto z bazy danych.", query);
		} catch (SQLException sqle) {
			log.error("Podczas usuwania zapytania {} z bazy danych wystąpił błąd.", query, sqle);
		}
	}

	public void showAll() {
		final String sqlQuery = "SELECT * FROM RESULTS";
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(sqlQuery);
			String date;
			String query;
			Long numberOfResults;
			while (resultSet.next()) {
				query = resultSet.getString("GOOGLE_QUERY");
				numberOfResults = resultSet.getLong("NUMBER_OF_RESULTS");
				date = resultSet.getString("DATE_OF_SEARCH");
				log.info(query + ": " + numberOfResults + " " + date);
			}
		} catch (SQLException sqle) {
			log.error("Błąd bazy danych.", sqle);
		} finally {
			this.closeSet(resultSet);
		}
	}
}
