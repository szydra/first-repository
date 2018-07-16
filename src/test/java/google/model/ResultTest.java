package google.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

public class ResultTest {

	@Test
	public void testConstructorWithDateAsString() {
		Result result = new Result("Monkey", 15_000L, "2016-08-12");
		assertEquals("Monkey", result.getQuery());
		assertEquals(15_000, result.getNumberOfResults().longValue());
		assertEquals(LocalDate.of(2016, Month.AUGUST, 12), result.getDate());
	}

	@Test
	public void testConstructorWithNull() {
		Result result = new Result("Elephant", 23_000L, null);
		assertNotNull(result.getDate());
	}

	@Test
	public void testToStringMethod() {
		Result result = new Result("Camel");
		result.setNumberOfResults(45_000L);
		assertEquals("Camel: 45000", result.toString());
	}

}
