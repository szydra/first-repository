package google.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import google.model.Result;

public class SQLite {
	
	private static final Logger log = Logger.getLogger(SQLite.class);
	
	public SQLite() {
		this.connect();
		this.createTable();
	}
	
	public static final String DRIVER = "org.sqlite.JDBC";
	
    public static final String DB_URL = "jdbc:sqlite:results.db";
    
    private Connection connection;
    
    private Statement statement;
    
    
    private void closeSet(ResultSet resultSet) {
		if (resultSet != null)
			try {
				resultSet.close();
			} catch (SQLException sqle) {
				log.error("Błąd zamknięcia zbioru wyników", sqle);
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
    
    public void createTable() {

    	try {
			statement.execute("CREATE TABLE IF NOT EXISTS RESULTS (GOOGLE_QUERY "
					+ "VARCHAR(31) PRIMARY KEY NOT NULL, NUMBER_OF_RESULTS LONG)");
			log.debug("Utworzono tabelę lub już istnieje.");
		} catch (SQLException sqle) {
			log.error("Błąd tworzenia tabeli.", sqle);
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
    	final String SQLQuery = "SELECT * FROM RESULTS "
    			+ "WHERE GOOGLE_QUERY = '" + query + "'";
    	ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(SQLQuery);
			if(resultSet.next()) {
	    		result = new Result(query, resultSet.getLong("NUMBER_OF_RESULTS"));
	    		log.info("Zapytanie " + query + " znajduje się w bazie danych.");
	    	}
		} catch (SQLException sqle) {
			log.error("Błąd pobierania danych dla zapytania "
					+ query + ".", sqle);
		} finally {
			this.closeSet(resultSet);
		}
    	return result;
    }
    
    public void insertResult(String query, Long numberOfResults) {
    	final String SQLQuery = "INSERT INTO RESULTS (GOOGLE_QUERY, "
    			+ "NUMBER_OF_RESULTS) VALUES ('" + query + "', '"
    			+ numberOfResults + "')";
    	try {
			statement.executeUpdate(SQLQuery);
			log.debug("Zapytanie " + query + " dodano do bazy danych.");
		} catch (SQLException sqle) {
			log.error("Podczas dodawania zapytania " + query
					+ " do bazy danych wystąpił błąd.", sqle);
		}
    }
    
    public void showAll() {
    	final String SQLQuery = "SELECT * FROM RESULTS";
    	ResultSet resultSet = null;
    	try {
			resultSet = statement.executeQuery(SQLQuery);
			String query;
			Long numberOfResults;
			while(resultSet.next()) {
				query = resultSet.getString("GOOGLE_QUERY");
				numberOfResults = resultSet.getLong("NUMBER_OF_RESULTS");
				log.info(query + ": " + numberOfResults);
			}
		} catch (SQLException sqle) {
			log.error("Błąd bazy danych.", sqle);
		} finally {
			this.closeSet(resultSet);
		}
    }
}
