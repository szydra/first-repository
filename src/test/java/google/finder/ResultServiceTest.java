package google.finder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import google.database.DbService;
import google.model.Result;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest {

	@Mock
	private GoogleConnector connectorMock;

	@Mock
	private DbService dbServiceMock;

	@InjectMocks
	private ResultService resultFinder;

	@Test
	public void whenResultNotFoundInDb_thenFindAndSave() {
		when(dbServiceMock.findResult(anyString())).thenReturn(Optional.empty());
		String query = "Venus";
		resultFinder.findAndSave(query);
		verify(dbServiceMock).findResult(query);
		verify(dbServiceMock).saveResult(any());
		verify(connectorMock).getNumberOfResults(query);
	}

	@Test
	public void whenResultFoundInDb_thenDontInteractWithConnector() {
		when(dbServiceMock.findResult(anyString())).thenReturn(Optional.of(new Result()));
		resultFinder.findAndSave("Earth");
		verify(dbServiceMock).findResult("Earth");
		verifyNoMoreInteractions(dbServiceMock);
		verifyZeroInteractions(connectorMock);
		assertFalse(resultFinder.getAllResults().isEmpty());
	}

	@Test
	public void whenResultFoundInDb_thenAddToCache() {
		String query = "Mars";
		Result result = new Result(query);
		when(dbServiceMock.findResult(query)).thenReturn(Optional.of(result));
		resultFinder.findAndSave(query);
		assertTrue(resultFinder.getAllResults().contains(result));
	}

	@Test
	public void whenResultNotFoundInDb_thenAddToCache() {
		String query = "Saturn";
		when(connectorMock.getNumberOfResults(query)).thenReturn(15_000L);
		when(dbServiceMock.findResult(anyString())).thenReturn(Optional.empty());
		resultFinder.findAndSave(query);
		Result result = new Result(query, 15_000L, null);
		assertTrue(resultFinder.getAllResults().contains(result));
	}

	@Test
	public void whenMoreResultsArePresent_thenSortThem() {
		Result result1 = new Result("Mercury", 45_000L, null);
		Result result2 = new Result("Uranus", 390_000L, null);
		Result result3 = new Result("Neptune", 129_000L, null);
		when(dbServiceMock.findResult("Mercury")).thenReturn(Optional.of(result1));
		when(dbServiceMock.findResult("Uranus")).thenReturn(Optional.of(result2));
		when(dbServiceMock.findResult("Saturn")).thenReturn(Optional.of(result3));
		resultFinder.findAndSave("Mercury");
		resultFinder.findAndSave("Uranus");
		resultFinder.findAndSave("Saturn");
		List<Result> expected = Arrays.asList(result2, result3, result1);
		assertEquals(expected, resultFinder.getAllResults());
	}

}
