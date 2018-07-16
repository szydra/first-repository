package google.model;

import java.time.LocalDate;

public class Result {

	public Result() {
	}

	public Result(String query) {
		this.query = query;
	}

	public Result(String query, Long numberOfResults, String date) {
		this.query = query;
		this.numberOfResults = numberOfResults;
		this.date = date == null ? LocalDate.now() : LocalDate.parse(date);
	}

	private String query;

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

	public void setQuery(String query) {
		this.query = query;
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

}
