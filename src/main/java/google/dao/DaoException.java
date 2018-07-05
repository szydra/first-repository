package google.dao;

/**
 * This exception is thrown when a problem with database access occurred.
 */
public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	DaoException(String message, Throwable e) {
		super(message, e);
	}

}
