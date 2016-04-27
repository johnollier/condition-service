package yourmd.interview;//

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class PhraseExtractor {

    public List<String> extractPhrases(List<String> words){
        assert( words != null);
        return extractPhrasesAsLists(words).stream().map(l -> String.join(" ", l)).collect(toList());
    }

    private List<List<String>> extractPhrasesAsLists(List<String> words){
        List<List<String>> subPhrases = new LinkedList<>();
        for(int i = 0; i <= words.size(); i++){
            for (int j = i + 1; j <= words.size(); j++){
                subPhrases.add( words.subList(i,j));
            }
        }
        return subPhrases;
    }


}
