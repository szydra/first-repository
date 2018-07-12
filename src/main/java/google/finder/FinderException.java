package google.finder;

/**
 * This exception is thrown when a problem with www.google.pl connection
 * occurred and the number of results cannot be determined.
 */
public class FinderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	FinderException(String message, Throwable e) {
		super(message, e);
	}

}
