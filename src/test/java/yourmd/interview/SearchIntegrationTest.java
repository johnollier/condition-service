package yourmd.interview;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class SearchIntegrationTest {

    RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void shouldFindCephalodynia() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/conditions?search=cephalodynia", String.class);
        String type = entity.getHeaders().getContentType().toString();
        assertEquals("application/json;charset=UTF-8", type);
        String body = entity.getBody();
        assertEquals("[\"cephalodynia\"]", body);

    }

    @Test
    public void shouldFindCoughAndSoreThroat() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/conditions?search=I have a sore throat and headache", String.class);
        String type = entity.getHeaders().getContentType().toString();
        assertEquals("application/json;charset=UTF-8", type);
        String body = entity.getBody();
        assertEquals("[\"headache\",\"sore throat\"]", body);

    }

    @Test
    public void shouldReturn400forInvalidSearch() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/conditions?search=c", String.class);
        assertEquals(400, entity.getStatusCode().value());
        String type = entity.getHeaders().getContentType().toString();
        assertEquals("application/json;charset=UTF-8", type);
        String body = entity.getBody();
        assertEquals("[\"Search phrase must contain between 3 and 128 characters\"]", body);
    }

    @Test
    public void shouldReturn405forPost() {
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:8080/conditions?search=c", "{'condition':'headache'}", String.class);
        assertEquals(405, entity.getStatusCode().value());
    }


}
