package by.bsu.fpmi.teamstat.service.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class JiraService {


    private static final String JIRA_BASE_URL = "";
    private static final String JIRA_LOGIN_URL = "auth/1/session";
    private static final String JIRA_LOGIN_URL = "auth/1/session";
    private static final String JIRA_LOGIN_URL = "auth/1/session";
    private static final String DEFAULT_USER = "admin";

    public String loginToJira(String username, String password) {
        try {
            URL url = new URL(JIRA_BASE_URL + JIRA_LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String input = "{\"username\":\"" + username + "\", \"password\": \"" + password + "\"}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(input.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output = "";
                StringBuilder response = new StringBuilder();
                while ((output = bufferedReader.readLine()) != null) {
                    response.append(output);
                }
                return response.toString();
            }
            connection.disconnect();

        } catch (Exception e) {
            throw new JiraLoginException(e.getMessage(), e);
        }
        return "";
    }

    public String parseJSessionId(String loginJson) {
        try {
            JsonNode parent= new ObjectMapper().readTree(loginJson);
            //session
            return parent.path("value").asText();
        } catch (JsonProcessingException e) {
            throw new JiraException("Cannot obtain jssessionId: " + e.getMessage(), e);
        }
    }

    public String getJsonData(String jsessionId) {

    }

    public String formatDataToCsv(String jsonData) {

    }

    public boolean getInfoFile(String csvData) {

    }

}
