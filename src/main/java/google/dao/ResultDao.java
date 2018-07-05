package google.dao;

import java.util.List;
import java.util.Optional;

import google.model.Result;

public interface ResultDao {

	/**
	 * This method saves the given result in the database.
	 * 
	 * @param result
	 *            A {@link Result} object to be inserted.
	 * 
	 * @throws DaoException
	 */
	void insert(Result result);

	/**
	 * This method gets the whole data stored in the database and returns it as a
	 * list.
	 * 
	 * @return A list of all results stored in the database.
	 * 
	 * @throws DaoException
	 */
	List<Result> getAllResults();

	/**
	 * This method returns a result stored in the database.
	 * 
	 * @param query
	 *            The query to be found.
	 * 
	 * @return Optional of the found result or an empty optional if no result was
	 *         found.
	 * 
	 * @throws DaoException
	 */
	Optional<Result> getResult(String query);

	/**
	 * This method removes all data stored in the database.
	 * 
	 * @throws DaoException
	 */
	void deleteAllResults();

	/**
	 * This method removes the given result from the database.
	 * 
	 * @param result
	 *            A {@link Result} object to be deleted.
	 * 
	 * @throws DaoException
	 */
	void deleteResult(Result result);

	/**
	 * This method removes data corresponding to the given query from the database.
	 * 
	 * @param query
	 *            A query to be deleted.
	 * 
	 * @throws DaoException
	 */
	void deleteResult(String query);

}
