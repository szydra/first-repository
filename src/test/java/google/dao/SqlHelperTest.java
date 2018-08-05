package google.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import google.model.Result;

public class SqlHelperTest {

	@Test
	public void testInsert() {
		Result result = new Result("Asterix i Obelix");
		result.setNumberOfResults(2_000);
		result.setDate(LocalDate.of(2017, Month.APRIL, 15));
		String expectedSql = "INSERT INTO RESULTS (GOOGLE_QUERY, NUMBER_OF_RESULTS, DATE_OF_SEARCH)"
				+ " VALUES ('Asterix i Obelix', 2000, '2017-04-15')";
		assertEquals(expectedSql, SqlHelper.getSqlForInsert(result));
	}

	@Test
	public void testSelectAll() {
		assertEquals("SELECT * FROM RESULTS", SqlHelper.getSqlForSelectAll());
	}

	@Test
	public void testSingleSelect() {
		assertEquals("SELECT * FROM RESULTS WHERE GOOGLE_QUERY = 'Papa Smerf'",
				SqlHelper.getSqlForSingleSelect("Papa Smerf"));
	}

	@Test
	public void testDeleteAll() {
		assertEquals("DELETE FROM RESULTS", SqlHelper.getSqlForDeleteAll());
	}

	@Test
	public void testDelete() {
		assertEquals("DELETE FROM RESULTS WHERE GOOGLE_QUERY = 'Myszka Miki'",
				SqlHelper.getSqlForDelete("Myszka Miki"));
	}

}
