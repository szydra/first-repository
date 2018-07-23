package google.main;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import google.dao.DaoException;
import google.database.DbService;
import google.finder.FinderService;
import google.model.Result;

@Component
public class ResultProcessor {

	private static final Logger log = LogManager.getLogger(ResultProcessor.class);

	@Autowired
	private DbService dbService;

	@Autowired
	private FinderService resultService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	private CountDownLatch latch;

	public void process(String... args) {
		if (args.length == 0) {
			log.info("Nie podano żadnego argumentu.");
		} else {
			long start = System.currentTimeMillis();
			processNonEmptyArgs(args);
			log.info("Program zakończył działanie po {} sek.",
					(System.currentTimeMillis() - start) / 1_000f);
		}
	}

	void processNonEmptyArgs(String... args) {
		switch (args[0]) {
		case "--clear-db":
			try {
				dbService.deleteAllResults();
				log.info("Baza danych została wyczyszczona.");
			} catch (DaoException de) {
				log.error("Nie udało się wyczyścić bazy danych.", de);
			}
			break;
		case "--remove":
			List<String> resultsToRemove = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
			try {
				dbService.deleteResults(resultsToRemove);
				log.info("Podane zapytania zostały usunięte z bazy danych.");
			} catch (DaoException de) {
				log.error("Błąd podczas usuwania zapytań z bazy danch", de);
			}
			break;
		case "--show-db":
			try {
				List<Result> results = dbService.findAllResults();
				results.stream().forEach(log::info);
			} catch (DaoException de) {
				log.error("Błąd odczytu bazy danych.", de);
			}
			break;
		default:
			latch = new CountDownLatch(args.length);
			Arrays.stream(args).forEach(this::findAndSave);
			awaitTerminationAndPrintAnswer();
			break;
		}
	}

	// TODO Hard coded timeout should configurable
	void awaitTerminationAndPrintAnswer() {
		try {
			if (latch == null) {
				throw new IllegalStateException("Latch cannot be null.");
			} else if (latch.await(60, TimeUnit.SECONDS)) {
				resultService.getAllResults().stream().forEach(this::printResult);
			} else {
				log.info("Osiągnięto limit czasu oczekiwania. Zamykanie aplikacji...");
			}
		} catch (InterruptedException ie) {
			log.error("Błąd podczas oczekiwania na wynik.", ie);
			Thread.currentThread().interrupt();
		} finally {
			taskExecutor.shutdown();
		}
	}

	void printResult(Result result) {
		String message = new MessageBuilder().withQuery(result.getQuery())
				.withNumberOfResults(result.getNumberOfResults())
				.build();
		log.info(message);
	}

	void findAndSave(String query) {
		taskExecutor.execute(() -> {
			resultService.findAndSave(query);
			latch.countDown();
		});
	}

}
