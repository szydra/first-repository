package google.finder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GoogleConnectorTest {

	@Spy
	private GoogleConnector connector;

	@Test(expected = FinderException.class)
	public void testBrokenConnection() throws IOException {
		doThrow(IOException.class).when(connector).getPage(anyString());
		connector.getNumberOfResults("github");
	}

	@Test
	public void testNonExistingQuery() {
		long number = connector.getNumberOfResults("xya2dow9of84bdk2oo7g");
		assertEquals(0L, number);
	}

	@Test
	public void testCommonQuery() {
		long number = connector.getNumberOfResults("bbc news");
		assertTrue(number > 1_000_000L);
	}

}
