package google.finder;

import java.util.List;

import google.dao.DaoException;
import google.model.Result;

public interface FinderService {

	/**
	 * This method checks if a result corresponding to the given query is stored in
	 * the database. If so it is added to cache, if not a request to www.google.pl
	 * is sent, then the result is saved in the database and added to cache.
	 * 
	 * @param query
	 *            A query to be processed.
	 * 
	 * @throws DaoException
	 *             if a problem with database occurred.
	 * @throws FinderException
	 *             if a problem with Internet connection occurred.
	 */
	void findAndSave(String query);

	/**
	 * This method returns all cached results - the ones from database as well as
	 * ones that were just got from the network. The returned list is sorted
	 * descending with respect to popularity.
	 * 
	 * @return A list of all received results.
	 */
	List<Result> getAllResults();

}
