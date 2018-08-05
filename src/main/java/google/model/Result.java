package google.model;

import java.time.LocalDate;
import java.util.Objects;

public class Result {

	public Result() {
		this("");
	}

	public Result(String query) {
		this.query = query;
	}

	public Result(String query, Long numberOfResults, String date) {
		this(query);
		this.numberOfResults = numberOfResults;
		this.date = date == null ? LocalDate.now() : LocalDate.parse(date);
	}

	private final String query;

	private LocalDate date;

	private Long numberOfResults;

	public String getQuery() {
		return query;
	}

	public LocalDate getDate() {
		return date;
	}

	public Long getNumberOfResults() {
		return numberOfResults;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setNumberOfResults(long numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	@Override
	public String toString() {
		return query + ": " + numberOfResults;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Result)) {
			return false;
		}
		Result oResult = (Result) o;
		return Objects.equals(query, oResult.query);
	}

	@Override
	public int hashCode() {
		return Objects.hash(query);
	}

}
