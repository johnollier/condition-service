package yourmd.interview;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class InjectionSearchValidatorTest {

    private InjectionSearchValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new InjectionSearchValidator();
    }

    @Test
    public void testValidate() throws Exception {
        assertTrue(validator.validate("simple TEXT 123").isEmpty());
        assertTrue(validator.validate("ill").isEmpty());
    }

    @Test
    public void searchShouldNotBeTooLong() throws Exception {
        List<String> errors = validator.validate(longString(129));
        assertEquals(1, errors.size());
        assertEquals("Search phrase must contain between 3 and 128 characters", errors.get(0));
    }

    @Test
    public void searchCanBe128Chars() throws Exception {
        List<String> errors = validator.validate(longString(128));
        assertEquals(1, errors.size());
        assertEquals("Search phrase must contain between 3 and 128 characters", errors.get(0));
    }

    @Test
    public void searchShouldNotBeTooShort() throws Exception {
        List<String> errors = validator.validate("xx");
        assertEquals(1, errors.size());
        assertEquals("Search phrase must contain between 3 and 128 characters", errors.get(0));
    }

    @Test
    public void searchShouldNotBeEmpty() throws Exception {
        List<String> errors = validator.validate(null);
        assertEquals(1, errors.size());
        assertEquals("Search phrase must contain between 3 and 128 characters", errors.get(0));
    }

    @Test
    public void shouldShouldNotContainSpecialCharacters() throws Exception {
        List<String> errors = validator.validate("<script>alert()</script>");
        assertEquals(1, errors.size());
        assertEquals("Search phrase should not contain special characters", errors.get(0));
    }

    private String longString(int length){
        StringBuilder builder = new StringBuilder();
        IntStream.range(1,length).forEach(builder::append);
        return builder.toString();
    }
}