package yourmd.interview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ConditionSearchControllerTest {

    @Mock
    private ConditionService conditionService;

    @Mock
    private HttpServletResponse response;

    private ConditionSearchController controller;

    @Before
    public void setUp() throws Exception {
        InjectionSearchValidator validator = new InjectionSearchValidator();
        controller = new ConditionSearchController(conditionService, validator);
    }

    @Test
    public void shouldReturnConditions() throws Exception {
        List<String> coughList = Arrays.asList("cough");
        when(conditionService.findConditions("I have a cough")).thenReturn(coughList);

        List<String> result = controller.conditionSearch("I have a cough", response);

        assertEquals(1, result.size());
        assertEquals(result.get(0), "cough");
    }

    @Test
    public void shouldReturnErrors() throws Exception {
        List<String> result = controller.conditionSearch("xx", response);

        verify(response).setStatus(400);
        assertEquals(1, result.size());
        assertEquals(result.get(0), "Search phrase must contain between 3 and 128 characters");
    }
}