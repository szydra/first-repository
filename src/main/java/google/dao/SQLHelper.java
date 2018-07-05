package google.dao;

import java.time.LocalDate;

import google.model.Result;

class SQLHelper {

	static final String TABLE_NAME = "RESULTS";
	static final String COLUMN_QUERY = "GOOGLE_QUERY";
	static final String COLUMN_NUMBER = "NUMBER_OF_RESULTS";
	static final String COLUMN_DATE = "DATE_OF_SEARCH";

	private SQLHelper() {
		// Helper class which should not be instantiated.
	}

	static String getSqlForInsert(Result result) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(TABLE_NAME)
				.append(" (").append(COLUMN_QUERY).append(", ")
				.append(COLUMN_NUMBER).append(", ")
				.append(COLUMN_DATE).append(") ")
				.append("VALUES ('").append(result.getQuery()).append("', '")
				.append(result.getNumberOfResults()).append("', '")
				.append(LocalDate.now()).append("')");
		return sql.toString();
	}

	static String getSqlForSelectAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(TABLE_NAME);
		return sql.toString();
	}

	static String getSqlForSingleSelect(String query) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(TABLE_NAME)
				.append(" WHERE ").append(COLUMN_QUERY)
				.append(" = '").append(query).append("'");
		return sql.toString();
	}

	static String getSqlForDeleteAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(TABLE_NAME);
		return sql.toString();
	}

	static String getSqlForDelete(String result) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(TABLE_NAME)
				.append(" WHERE ").append(COLUMN_QUERY)
				.append(" = '").append(result).append("'");
		return sql.toString();
	}

}
