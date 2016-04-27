package yourmd.interview;//

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.lucene.analysis.StopAnalyzer.ENGLISH_STOP_WORDS_SET;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LuceneSearchService implements ConditionService {

    private static Log LOG = LogFactory.getLog(LuceneSearchService.class);

    private final IndexSearcher searcher;

    private final PhraseExtractor phraseExtractor;

    @Autowired
    public LuceneSearchService(@Value("${dictionary.file.name}") String dictionaryFileName, PhraseExtractor phraseExtractor) {
        this.phraseExtractor = phraseExtractor;

        Directory dir = new RAMDirectory();
        try {
            IndexWriter indexWriter = new IndexWriter(dir, new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.LIMITED);
            Files.lines(Paths.get(dictionaryFileName)).forEach(condition -> addCondition(condition, indexWriter));
            indexWriter.close();
            searcher = new IndexSearcher(dir);
        } catch (IOException e) {
            throw new ExceptionInInitializerError();
        }
    }

    private void addCondition(String condition, IndexWriter indexWriter) {
        try {
            Document doc = new Document();
            // don't analyse because we match against whole condition
            doc.add(new Field("condition", condition, Field.Store.YES, Field.Index.NOT_ANALYZED));
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            throw new UncheckedIOException("", e);
        }
    }

    @Override
    public List<String> findConditions(String phrase) {
        assert (phrase != null);

        List<String> words = Arrays.asList(phrase.split("\\s+")).stream()
                .filter( word -> !ENGLISH_STOP_WORDS_SET.contains(word))
                .collect(toList());

        BooleanQuery booleanQuery = getBooleanClauses(words);

        try {
            TopDocs matches = searcher.search(booleanQuery, 20);

            List<String> conditions = Arrays.stream(matches.scoreDocs)
                    .map(scoreDoc -> extractCondition(booleanQuery, scoreDoc))
                    .collect(toList());

            return conditions;

        } catch (IOException e) {
            throw new UncheckedIOException("", e);
        }
    }

    private BooleanQuery getBooleanClauses(List<String> words) {
        BooleanQuery booleanQuery = new BooleanQuery();

        for (String subPhrase : phraseExtractor.extractPhrases(words)) {
            Term term = new Term("condition", subPhrase);
            Query query = new TermQuery(term);
            booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        }
        return booleanQuery;
    }

    private String extractCondition(Query query, ScoreDoc scoreDoc) {
        try {
            LOG.info(searcher.explain(query, scoreDoc.doc).toString());
            return searcher.doc(scoreDoc.doc).get("condition");
        } catch (IOException e) {
            throw new UncheckedIOException("", e);
        }
    }

}
