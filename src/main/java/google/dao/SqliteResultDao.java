package google.dao;

import static google.dao.SqlHelper.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import google.model.Result;

@Repository
class SqliteResultDao implements ResultDao {

	private static final Logger log = LogManager.getLogger(SqliteResultDao.class);

	@Override
	public void insert(Result result) {
		try (Sqlite sqlite = new Sqlite()) {
			sqlite.executeUpdate(getSqlForInsert(result));
			log.debug("Zapytanie {} dodano do bazy danych.", result.getQuery());
		} catch (SQLException sqle) {
			throw new DaoException("Podczas dodawania zapytania " + result.getQuery()
					+ " do bazy danych wystąpił błąd.", sqle);
		}
	}

	@Override
	public List<Result> findAll() {
		List<Result> results = new ArrayList<>();
		try (Sqlite sqlite = new Sqlite();
				Statement statement = sqlite.getStatement();
				ResultSet resultSet = statement.executeQuery(getSqlForSelectAll())) {
			while (resultSet.next()) {
				Result result = new Result(resultSet.getString(COLUMN_QUERY));
				result.setDate(LocalDate.parse(resultSet.getString(COLUMN_DATE)));
				result.setNumberOfResults(resultSet.getLong(COLUMN_NUMBER));
				results.add(result);
				log.debug("Zapytanie {} pobrano z bazy danych.", result.getQuery());
			}
		} catch (SQLException sqle) {
			throw new DaoException("Błąd bazy danych podczas odczytu wszystkich zapytań.", sqle);
		}
		return results;
	}

	@Override
	public Optional<Result> find(String query) {
		try (Sqlite sqlite = new Sqlite();
				Statement statement = sqlite.getStatement();
				ResultSet resultSet = statement.executeQuery(getSqlForSingleSelect(query))) {
			if (resultSet.next()) {
				log.debug("Zapytanie {} znajduje się w bazie danych.", query);
				return Optional.of(new Result(query, resultSet.getLong(COLUMN_NUMBER),
						resultSet.getString(COLUMN_DATE)));
			}
		} catch (SQLException sqle) {
			throw new DaoException("Błąd pobierania danych dla zapytania" + query, sqle);
		}
		return Optional.empty();
	}

	@Override
	public void deleteAll() {
		try (Sqlite sqlite = new Sqlite()) {
			sqlite.executeUpdate(getSqlForDeleteAll());
		} catch (SQLException sqle) {
			throw new DaoException("Błąd podczas czyszczenia bazy danych.", sqle);
		}
	}

	@Override
	public void delete(Result result) {
		delete(result.getQuery());
	}

	@Override
	public void delete(String query) {
		try (Sqlite sqlite = new Sqlite()) {
			sqlite.executeUpdate(getSqlForDelete(query));
			log.debug("Zapytanie {} usunięto z bazy danych.", query);
		} catch (SQLException sqle) {
			throw new DaoException("Błąd podczas usuwania zapytania " + query, sqle);
		}
	}

}
