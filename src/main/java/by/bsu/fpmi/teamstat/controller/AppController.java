package by.bsu.fpmi.teamstat.controller;

import by.bsu.fpmi.teamstat.entity.Issue;
import by.bsu.fpmi.teamstat.entity.Message;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.repository.TeamRepository;
import by.bsu.fpmi.teamstat.service.BugService;
import by.bsu.fpmi.teamstat.service.MeetingService;
import by.bsu.fpmi.teamstat.service.TaskService;
import by.bsu.fpmi.teamstat.service.jira.JiraService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/teamStat")
public class AppController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private final BugService bugService;
    private final MeetingService meetingService;
    private final TaskService taskService;
    private final TeamRepository teamRepository;

    private final JiraService jiraService;

    @GetMapping("/efficiency/{teamId}")
    public ResponseEntity<Message> getTeamEfficiency(@PathVariable int teamId, @RequestParam("start") @DefaultValue("") String start,
                                                     @DefaultValue("") @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        String efficiencyPercent = taskService.getEfficiencyPercent(teamId, startDate, endDate);
        return new ResponseEntity<>(new Message(efficiencyPercent), HttpStatus.OK);
    }

    @GetMapping("/prodBugs/{teamId}")
    public ResponseEntity<Message> getTeamBugs(@PathVariable int teamId, @RequestParam("start") String start,
                                               @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        String prodBugsPercent = bugService.getProdBugsPercent(teamId, startDate, endDate);
        return new ResponseEntity<>(new Message(prodBugsPercent), HttpStatus.OK);
    }

    @GetMapping("/presence/{teamId}")
    public ResponseEntity<Message> getTeamPresence(@PathVariable int teamId, @RequestParam("start") String start,
                                                   @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        List<String> teamPresencePercent = meetingService.getTeamPresencePercent(teamId, startDate, endDate);
        return new ResponseEntity<>(new Message(teamPresencePercent), HttpStatus.OK);
    }

    @GetMapping("/jira/login")
    public ResponseEntity<String> jiraLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        return jiraService.loginToJira(username, password);
    }

    @GetMapping("/jira/issues/{id}")
    public ResponseEntity<String> getIssue(@PathVariable String id) {
        return jiraService.getIssue(id);
    }

    @GetMapping("/jira/issues/")
    public ResponseEntity<List<Issue>> getIssues() {
        return new ResponseEntity<>(jiraService.getIssues(), HttpStatus.OK);
    }

    @GetMapping("/jira/issues/types")
    public ResponseEntity<String> jiraIssuesTypes() {
        return jiraService.getIssuesTypes();
    }

    @PostMapping("jira/create")
    public ResponseEntity<String> createIssue(@RequestBody String issueJson) {
        return jiraService.createIssue(issueJson);
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams() {
        return new ResponseEntity<>(teamRepository.findAll(), HttpStatus.OK);
    }
}
