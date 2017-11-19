package google.model;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDate;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResultTest {

	private static final String LOG_FILENAME = "log4j2-test.log";

	private Result result;

	@Mock
	private Appender mockAppender;

	@Captor
	private ArgumentCaptor<LogEvent> captorLogEvent;

	private Logger logger;

	@BeforeClass
	public static void setLogFilename() {
		System.setProperty("logFilename", LOG_FILENAME);
	}

	@Before
	public void setupLogger() {
		when(mockAppender.getName()).thenReturn("MockAppender");
		when(mockAppender.isStarted()).thenReturn(true);
		when(mockAppender.isStopped()).thenReturn(false);

		logger = (Logger) LogManager.getLogger(Result.class);
		logger.addAppender(mockAppender);
		logger.setLevel(Level.INFO);
	}

	@After
	public void tearDownLogger() {
		logger.removeAppender(mockAppender);
	}

	@AfterClass
	public static void deleteLogFile() {
		if (!new File(LOG_FILENAME).delete()) {
			fail("Nie udało się usunąć pliku " + LOG_FILENAME + ".");
		}
	}

	@Test
	public void testCommonQuery() {
		result = new Result("bbc news");
		result.setNumberOfResults();
		assertTrue(result.getNumberOfResults().longValue() > 100 * 1000 * 1000L);
	}

	@Test
	public void testNonExistingQuery() {
		result = new Result("xya2dow9of84bdk2oo7g");
		result.setNumberOfResults();
		assertEquals(result.getNumberOfResults().longValue(), 0L);
	}

	@Test
	public void testTodayRequest() {
		result = new Result("chomik", Long.valueOf(0L), LocalDate.now().toString());
		result.printDate();
		verify(mockAppender, times(1)).append(captorLogEvent.capture());
		LogEvent loggingEvent = captorLogEvent.getAllValues().get(0);
		assertEquals("Zapytanie 'chomik' dziś dodano do bazy danych.", loggingEvent.getMessage().getFormattedMessage());
	}

	@Test
	public void testNowRequest() {
		result = new Result();
		result.setDate();
		result.printDate();
		verify(mockAppender, times(1)).append(captorLogEvent.capture());
		LogEvent loggingEvent = captorLogEvent.getAllValues().get(0);
		assertEquals("Zapytanie 'null' teraz dodano do bazy danych.", loggingEvent.getMessage().getFormattedMessage());
	}

	@Test
	public void testOldRequest() {
		final String date = "2012-11-02";
		result = new Result("chomik", null, date);
		result.printDate();
		verify(mockAppender, times(1)).append(captorLogEvent.capture());
		LogEvent loggingEvent = captorLogEvent.getAllValues().get(0);
		assertEquals("Zapytanie 'chomik' dodano do bazy danych " + DAYS.between(LocalDate.parse(date), LocalDate.now())
				+ " dni temu.", loggingEvent.getMessage().getFormattedMessage());
	}

	@Test
	public void testZeroResultsPrinter() {
		result = new Result("chomik", Long.valueOf(0L), null);
		result.printNumberOfResults();
		verify(mockAppender, times(1)).append(captorLogEvent.capture());
		LogEvent loggingEvent = captorLogEvent.getAllValues().get(0);
		assertEquals("Dla zapytania 'chomik' Google niczego nie znalazło.",
				loggingEvent.getMessage().getFormattedMessage());
	}

	@Test
	public void testNonZeroResultsPrinter() {
		long number = 10000L;
		result = new Result("chomik", Long.valueOf(number), null);
		result.printNumberOfResults();
		verify(mockAppender, times(1)).append(captorLogEvent.capture());
		LogEvent loggingEvent = captorLogEvent.getAllValues().get(0);
		assertEquals("Dla zapytania 'chomik' Google znalazło ok. " + String.format("%,d", number) + " wyników.",
				loggingEvent.getMessage().getFormattedMessage());
	}

}
