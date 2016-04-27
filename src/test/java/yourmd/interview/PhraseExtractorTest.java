package yourmd.interview;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

public class PhraseExtractorTest {

    private PhraseExtractor extractor;

    @Before
    public void setup(){
        extractor = new PhraseExtractor();
    }

    @Test
    public void testExtractPhrases() throws Exception {
        List<String> phrases = extractor.extractPhrases(Arrays.asList("nasty","cut"));
        assertThat( phrases.size(), equalTo(3));
        assertThat(phrases.get(0), equalTo("nasty"));
        assertThat(phrases.get(1), equalTo("nasty cut"));
        assertThat(phrases.get(2), equalTo("cut"));
    }

    @Test
    public void testExtractSingleWord() throws Exception {
        List<String> phrases = extractor.extractPhrases(Arrays.asList("cut"));
        assertThat( phrases.size(), equalTo(1));
        assertThat(phrases.get(0), equalTo("cut"));
    }



}