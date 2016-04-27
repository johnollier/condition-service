package yourmd.interview;//

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class InjectionSearchValidator implements SearchValidator {

    private static List<String> ILLEGAL_CHARACTERS = Arrays.asList("<", ">", "\\", "+", "!", "(", ")", ":", "^", "]", "{", "}", "~", "*", "?");

    @Override
    public List<String> validate(String phrase) {
        List<String> errors = new LinkedList<>();
        if (StringUtils.isEmpty(phrase) || phrase.length() < 3 || phrase.length() > 128) {
            errors.add("Search phrase must contain between 3 and 128 characters");
        } else if (containsIllegalCharacters(phrase)) {
            errors.add("Search phrase should not contain special characters");
        }
        return errors;
    }

    private boolean containsIllegalCharacters(String phrase) {
        assert (phrase != null);
        return ILLEGAL_CHARACTERS.stream().anyMatch(s -> phrase.contains(s));
    }
}
