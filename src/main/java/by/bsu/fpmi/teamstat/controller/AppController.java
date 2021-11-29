package by.bsu.fpmi.teamstat.controller;

import by.bsu.fpmi.teamstat.entity.Message;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.repository.TeamRepository;
import by.bsu.fpmi.teamstat.service.BugService;
import by.bsu.fpmi.teamstat.service.MeetingService;
import by.bsu.fpmi.teamstat.service.TaskService;
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

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams() {
        return new ResponseEntity<>(teamRepository.findAll(), HttpStatus.OK);
    }
}
