package google.main;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NumberOfResults {
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/"
			+ "58.0.3029.81 Chrome/58.0.3029.81 Safari/537.36";
	private static final String REGEX_RESULTS = "Około(.*)wyników";
	
	public static void printNumberOfResults(String query) throws IOException {
		final Document page = Jsoup.connect("https://google.pl/search?q="
        		+ query.replace(' ', '+')).userAgent(USER_AGENT).get();
        
        String results = page.select("div#resultStats").text();
        Matcher matcher = Pattern.compile(REGEX_RESULTS).matcher(results);
        
        if(matcher.find())
        	System.out.println("Dla zapytania " + query +
        			" Google znalazło ok." + matcher.group(1) + "wyników.");
        else if(results.length() == 0)
        	System.out.println("Dla zapytania " + query +
        			" Google niczego nie znalazło.");
        else
        	System.out.println("Coś poszło nie tak.");
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.println("Nie podano żadnego argumentu.");
			return;
		} else {
			for(String s: args)
				printNumberOfResults(s);
		}
	}
}