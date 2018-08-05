package google.finder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
class GoogleConnector {

	private static final Logger log = LogManager.getLogger(GoogleConnector.class);

	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/"
			+ "58.0.3029.81 Chrome/58.0.3029.81 Safari/537.36";

	private static final String REGEX_RESULTS = "Około(.*)wyników";

	long getNumberOfResults(String query) {
		final Document page;
		try {
			page = getPage(query);
			log.debug("Pobrano stronę dla zapytania '{}'.", query);
		} catch (IOException ioe) {
			log.error("Błąd pobierania strony dla zapytania '{}'.", query);
			throw new FinderException("Błąd pobierania strony dla zapytania " + query, ioe);
		}

		Matcher matcher = Pattern.compile(REGEX_RESULTS)
				.matcher(page.select("div#resultStats").text());

		if (matcher.find()) {
			return Long.parseLong(matcher.group(1).replaceAll("\\D", ""));
		} else {
			return 0;
		}
	}

	Document getPage(String query) throws IOException {
		return Jsoup.connect("https://google.pl/search?q=" + query.replace(' ', '+'))
				.userAgent(USER_AGENT)
				.get();
	}

}
