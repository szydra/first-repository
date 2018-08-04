package google.main;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import google.database.DbService;
import google.finder.FinderService;
import google.model.Result;

@RunWith(MockitoJUnitRunner.class)
public class ResultProcessorTest {

	private static final String LOG_FILENAME = "log4j2-test.log";

	@BeforeClass
	public static void setupLogger() {
		System.setProperty("logFilename", LOG_FILENAME);
	}

	@AfterClass
	public static void deleteLogFile() {
		if (!new File(LOG_FILENAME).delete()) {
			fail("Cannot delete file " + LOG_FILENAME);
		}
	}

	@Mock
	private DbService dbServiceMock;

	@Mock
	private FinderService finderMock;

	@Mock
	private ThreadPoolTaskExecutor executorMock;

	@InjectMocks
	private ResultProcessor processor;

	@Test
	public void whenNoArgs_thenDoNothing() {
		processor.process();
		verifyZeroInteractions(dbServiceMock, finderMock, executorMock);
	}

	@Test
	public void whenClearDb_thenDeleteAllResults() {
		processor.process("--clear-db");
		verify(dbServiceMock).deleteAllResults();
		verifyNoMoreInteractions(dbServiceMock, finderMock);
	}

	@Test
	public void whenShowDb_thenFindAllResults() {
		processor.process("--show-db");
		verify(dbServiceMock).findAllResults();
		verifyNoMoreInteractions(dbServiceMock, finderMock);
	}

	@Test
	public void whenRemove_thenDeleteResults() {
		processor.process("--remove", "arg1", "arg2");
		verify(dbServiceMock).deleteResults(Arrays.asList("arg1", "arg2"));
		verifyNoMoreInteractions(dbServiceMock, finderMock);
	}

	@Test
	public void whenNoFlag_thenProcessAndShutdown() {
		prepareExecutor();
		prepareFinder();
		processor.process("arg1", "arg2");
		verify(finderMock).findAndSave("arg1");
		verify(finderMock).findAndSave("arg2");
		verify(executorMock).shutdown();
	}

	private void prepareExecutor() {
		doAnswer(invocation -> {
			((Runnable) invocation.getArgument(0)).run();
			return null;
		}).when(executorMock).execute(any());
	}

	private void prepareFinder() {
		doReturn(Arrays.asList(new Result("arg1"), new Result("arg2")))
				.when(finderMock)
				.getAllResults();
	}

}
