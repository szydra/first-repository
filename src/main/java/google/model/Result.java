package google.model;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Result {
	
	//Constructors
	public Result() { }
	public Result(String query) {
		this.query = query;
	}
	
	//Static Fields
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/"
			+ "58.0.3029.81 Chrome/58.0.3029.81 Safari/537.36";
	
	private static final String REGEX_RESULTS = "Około(.*)wyników";
	private static final String REGEX_NUMBER = "\\d+";
	
	//Non-static fields
	private String query;
	
	//Non-static Methods
	public Long getNumberOfResults() throws IOException {
		final Document page = Jsoup.connect("https://google.pl/search?q="
        		+ this.query.replace(' ', '+')).userAgent(USER_AGENT).get();
		
        String resultStats = page.select("div#resultStats").text();
        Matcher matcher = Pattern.compile(REGEX_RESULTS).matcher(resultStats);
        
        if(matcher.find()) {
        	String number = matcher.group(1).replaceAll("\\D", "");
        	if(number.matches(REGEX_NUMBER))
        		return Long.valueOf(number);
        	else
        		return Long.valueOf(-1);
        }
        return Long.valueOf(0);
	}
	
	public void printNumberOfResults() throws IOException {
		Long numberOfResults = this.getNumberOfResults();
		
		if(numberOfResults.equals(Long.valueOf(0)))
			System.out.println("Dla zapytania " + this.query +
        			" Google niczego nie znalazło.");
		else if(numberOfResults.equals(Long.valueOf(-1))) {
			System.out.println("Nieznany błąd.");
		} else {
			System.out.println("Dla zapytania " + this.query +
        			" Google znalazło ok. " + numberOfResults.toString() +
        			" wyników.");
		}
	}
}
