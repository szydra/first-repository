package google.database;

import java.util.List;
import java.util.Optional;

import google.dao.DaoException;
import google.model.Result;

public interface DbService {

	/**
	 * This method saves the given result in the database.
	 * 
	 * @param result
	 *            {@link Result} object to be saved.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	void saveResult(Result result);

	/**
	 * This method gets all results from the database.
	 * 
	 * @return A list of all results stored in the database or an empty list when no
	 *         results were found.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	List<Result> findAllResults();

	/**
	 * This method finds a result corresponding to the given query.
	 * 
	 * @param query
	 *            Query to be found.
	 * 
	 * @return {@link Optional} of the found result or an empty optional if no
	 *         result was found.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	Optional<Result> findResult(String query);

	/**
	 * This method cleans the database.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	void deleteAllResults();

	/**
	 * This method removes all results corresponding to the given list of queries.
	 * 
	 * @param resultsToRemove
	 *            A list of queries.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	void deleteResults(List<String> resultsToRemove);

	/**
	 * This method removes a result corresponding to the given query.
	 * 
	 * @param query
	 *            Query to be deleted.
	 * 
	 * @throws DaoException
	 *             When a problem with the database occurred.
	 */
	void deleteResult(String query);

}
