package by.bsu.fpmi.teamstat.service.jira;

import by.bsu.fpmi.teamstat.entity.Issue;
import by.bsu.fpmi.teamstat.entity.Issues;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final RestTemplate restTemplate;

    private final ObjectMapper issueMapper;
    private String username;

    private String password;
    private static final String JIRA_BASE_URL = "http://localhost:8080/rest/";
    private static final String JIRA_LOGIN_URL = "auth/1/session";

    private static final String PROJECT_ISSUES_TYPES = "http://localhost:8080/rest/api/2/issue/createmeta/10000/issuetypes/";

    private static final String CREATE_ISSUES_URL = "http://localhost:8080/rest/api/2/issue/";

    private static final String ISSUES_URL = "http://localhost:8080/rest/api/2/search/";

    private static final String ISSUE_URL = "http://localhost:8080/rest/api/2/issue/";

    public ResponseEntity<String> loginToJira(String username, String password) {
        try {
            this.password = password;
            this.username = username;
            String input = "{\"username\":\"" + username + "\", \"password\": \"" + password + "\"}";
            HttpEntity<String> request = createRequest(input, new HttpHeaders());
            return restTemplate.postForEntity(new URI(JIRA_BASE_URL + JIRA_LOGIN_URL), request, String.class);
        } catch (Exception e) {
            throw new JiraLoginException(e.getMessage(), e);
        }
    }

    public ResponseEntity<String> getIssuesTypes() {
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(URI.create(PROJECT_ISSUES_TYPES), HttpMethod.GET, request, String.class);
    }

    public ResponseEntity<String> createIssue(String issueJson) {
        HttpEntity<String> request = createRequest(issueJson, getAuthHeaders());
        return restTemplate.postForEntity(CREATE_ISSUES_URL, request, String.class);
    }

    public ResponseEntity<String> getIssue(String id) {
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(URI.create(ISSUE_URL + id), HttpMethod.GET, request, String.class);
    }

    public List<Issue> getIssues() {
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(URI.create(ISSUES_URL), HttpMethod.GET, request, String.class);
        String body = result.getBody();
        Issues issues = null;
        try {
            issues = issueMapper.readValue(body, Issues.class);
            List<Issue> issuesList = new ArrayList<>();
            for (Object issueRaw : issues.getIssues()) {
                Map<String, ?> issueAsMap = (Map<String, ?>) issueRaw;
                Map<String, Object> fields = (Map<String, Object>) ((Map<?, ?>) issueRaw).get("fields");
                String summary = (String) fields.get("summary");
                String description = (String) fields.get("description");
                String resolution = fields.get("resolution") == null ? "unresolved" : (String) fields.get("resolution");
                String priority = (String) ((Map<String, ?>) fields.get("priority")).get("name");
                String type = (String) ((Map<String, ?>) fields.get("issuetype")).get("name");
                Issue issue = new Issue(summary, description, type, priority, resolution);
                issuesList.add(issue);
            }
            return issuesList;
        } catch (Exception e) {
            throw new JiraException(e.getMessage(), e);
        }
    }

    private HttpHeaders getAuthHeaders() {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    private HttpEntity<String> createRequest(String input, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(input, headers);
        return request;
    }
}
