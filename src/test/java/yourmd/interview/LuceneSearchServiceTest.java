package yourmd.interview;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LuceneSearchServiceTest {

    private LuceneSearchService service;

    @Before
    public void setUp() throws Exception {
        PhraseExtractor phraseExtractor = new PhraseExtractor();
        service = new LuceneSearchService("src/test/resources/dictionary.txt", phraseExtractor);
    }

    @Test
    public void testFindCondition() throws Exception {
        List<String> conditions = service.findConditions("headache");
        assertEquals(1, conditions.size());
        assertThat(conditions, hasItems("headache"));
    }

    @Test
    public void testFindConditionMultiWord() throws Exception {
        List<String> conditions = service.findConditions("sore throat");
        assertEquals(1, conditions.size());
        assertThat(conditions, hasItems("sore throat"));
    }

    @Test
    public void testFindMultipleConditions() throws Exception {
        List<String> conditions = service.findConditions("headache sore throat");
        assertEquals(2, conditions.size());
        assertThat(conditions, hasItems("headache", "sore throat"));
    }

    @Test
    public void testFindMultipleConditionsIgnoresStopWords() throws Exception {
        List<String> conditions = service.findConditions("I have a headache and sore throat");
        assertEquals(2, conditions.size());
        assertThat(conditions, hasItems("headache", "sore throat"));
    }


    @Test
    public void testFindMultipleConditionsIgnoresExtraWhitespace() throws Exception {
        List<String> conditions = service.findConditions("headache    sore    throat  ");
        assertEquals(2, conditions.size());
        assertThat(conditions, hasItems("headache", "sore throat"));
    }


}