package by.bsu.fpmi.teamstat.service;

import by.bsu.fpmi.teamstat.entity.Task;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.repository.TaskRepository;
import by.bsu.fpmi.teamstat.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;

    private static final String TEAM_MSG = "Team %s efficiency = %.1f. %s";

    private static final String FINE = "This is fine. No changes needed";
    private static final String WARNING = "Efficiency is lower than required! Communicate with team leader" +
            " to know if team has any problems.";
    private static final String CRITICAL = "Efficiency is very low!! If there are now serious mistake in management," +
            " it means that team does not cope with the tasks and needs to be fully reformed.";

    public String getEfficiencyPercent(int teamId, LocalDate start, LocalDate end) {
        Team team = teamRepository.getById(teamId);
        List<Task> tasks = taskRepository.findAllByResolutionDateBetweenAndTeam(start, end, team);
        long totalCount = tasks.size();
        long solvedCount = tasks.stream().filter(Task::isSolved).count();
        double efficiency = solvedCount * 100.0 / totalCount;
        String efficiencyMsg;
        if (efficiency >= 80.) {
            efficiencyMsg = FINE;
        } else if (efficiency >= 40.) {
            efficiencyMsg = WARNING;
        } else {
            efficiencyMsg = CRITICAL;
        }
        return String.format(TEAM_MSG, team.getName(), efficiency, efficiencyMsg);
    }
}
