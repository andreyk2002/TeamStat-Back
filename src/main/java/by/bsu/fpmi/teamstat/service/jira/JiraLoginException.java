package by.bsu.fpmi.teamstat.service.jira;

public class JiraLoginException extends RuntimeException {

    public JiraLoginException() {
    }

    public JiraLoginException(String message) {
        super(message);
    }

    public JiraLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public JiraLoginException(Throwable cause) {
        super(cause);
    }
}
