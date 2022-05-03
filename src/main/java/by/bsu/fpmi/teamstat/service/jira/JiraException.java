package by.bsu.fpmi.teamstat.service.jira;

public class JiraException extends RuntimeException{
    public JiraException() {
    }

    public JiraException(String message) {
        super(message);
    }

    public JiraException(String message, Throwable cause) {
        super(message, cause);
    }

    public JiraException(Throwable cause) {
        super(cause);
    }
}
