package yourmd.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ConditionSearchController {

    private final ConditionService conditionService;

    private final SearchValidator searchValidator;

    @Autowired
    public ConditionSearchController(ConditionService conditionService, SearchValidator searchValidator) {
        this.conditionService = conditionService;
        this.searchValidator = searchValidator;
    }

    @RequestMapping(value = "/conditions", method = RequestMethod.GET)
    public List<String> conditionSearch(@RequestParam(value = "search") String phrase, HttpServletResponse response) {
        List<String> errors = searchValidator.validate(phrase);
        if (errors.isEmpty()) {
            return conditionService.findConditions(phrase);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return errors;
        }
    }

}