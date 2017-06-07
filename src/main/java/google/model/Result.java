package google.model;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Result {
	
	private static final Logger log = Logger.getLogger(Result.class);
	
	public Result() { }
	
	public Result(String query) {
		this.query = query;
	}
	
	public Result(String query, Long numberOfResults) {
		this.query = query;
		this.numberOfResults = numberOfResults;
	}
	
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/"
			+ "58.0.3029.81 Chrome/58.0.3029.81 Safari/537.36";
	
	private static final String REGEX_RESULTS = "Około(.*)wyników";
	
	private String query;
	
	private Long numberOfResults;
	
	public Long getNumberOfResults() {
		return numberOfResults;
	}
	
	public void setNumberOfResults() {
		final Document page;
		try {
			page = Jsoup.connect("https://google.pl/search?q="
					+ this.query.replace(' ', '+')).userAgent(USER_AGENT).get();
			log.debug("Pobrano stronę dla zapytania '" + this.query + "'.");
		} catch(IOException ioe) {
			log.error("Błąd pobierania strony dla zapytania '"
					+ this.query + "'.", ioe);
			this.numberOfResults = 0L;
			return;
		}
		
		String resultStats = page.select("div#resultStats").text();
		Matcher matcher = Pattern.compile(REGEX_RESULTS).matcher(resultStats);

		if(matcher.find()) {
		String number = matcher.group(1).replaceAll("\\D", "");
			this.numberOfResults = Long.valueOf(number);
		} else {
			this.numberOfResults = 0L;
		}
	}
	
	public void printNumberOfResults() {		
		if(this.numberOfResults.equals(0L)) {
			log.info("Dla zapytania '" + this.query
					+ "' Google niczego nie znalazło.");
		} else {
			log.info("Dla zapytania '" + this.query
					+ "' Google znalazło ok. "
					+ String.format("%,d", this.numberOfResults) + " wyników.");
		}
	}
}
