package google.model;

import java.io.IOException;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
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
	
	public Result(String query, Long numberOfResults, String date) {
		this.query = query;
		this.numberOfResults = numberOfResults;
		this.date = LocalDate.parse(date);
	}
	
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/"
			+ "58.0.3029.81 Chrome/58.0.3029.81 Safari/537.36";
	
	private static final String REGEX_RESULTS = "Około(.*)wyników";
	
	private Boolean justAdded = false;
	
	private String query;
	
	private LocalDate date;
	
	private Long numberOfResults;
	
	public Long getNumberOfResults() {
		return this.numberOfResults;
	}
	
	public void setDate() {
		this.date = LocalDate.now();
		this.justAdded = true;
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
	
	public void printDate() {
		String stringQuery = "Zapytanie '" + this.query + "'";
		if(justAdded)
			log.info(stringQuery + " teraz dodano do bazy danych.");
		else if(this.date.equals(LocalDate.now()))
			log.info(stringQuery + " dziś dodano do bazy danych.");
		else
			log.info(stringQuery + " dodano do bazy danych "
					+ DAYS.between(this.date, LocalDate.now()) + " dni temu.");
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
