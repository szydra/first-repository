package google.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageBuilderTest {

	@Test
	public void testBuilderWithNull() {
		String expected = "Dla zapytania 'foo' Google niczego nie znalazło.";
		String actual = new MessageBuilder().withQuery("foo").withNumberOfResults(null).build();
		assertEquals(expected, actual);
	}

	@Test
	public void testBuilderWithZero() {
		String expected = "Dla zapytania 'foo' Google niczego nie znalazło.";
		String actual = new MessageBuilder().withQuery("foo").withNumberOfResults(0L).build();
		assertEquals(expected, actual);
	}

	@Test
	public void testBuilderWithoutFormatting() {
		String expected = "Dla zapytania 'foo' Google znalazło ok. 700 wyników.";
		String actual = new MessageBuilder().withQuery("foo").withNumberOfResults(700L).build();
		assertEquals(expected, actual);
	}

	@Test
	public void testBuilderWithFormatting() {
		String number = String.format("%,d", 50_000L);
		String expected = String.format("Dla zapytania 'foo' Google znalazło ok. %s wyników.",
				number);
		String actual = new MessageBuilder().withQuery("foo").withNumberOfResults(50_000L).build();
		assertEquals(expected, actual);
	}

}
