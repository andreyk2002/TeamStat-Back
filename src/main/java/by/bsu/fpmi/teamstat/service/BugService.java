package by.bsu.fpmi.teamstat.service;

import by.bsu.fpmi.teamstat.entity.Bug;
import by.bsu.fpmi.teamstat.entity.BugStatus;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.repository.BugRepository;
import by.bsu.fpmi.teamstat.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BugService {
    private final BugRepository bugRepository;
    private final TeamRepository teamRepository;

    private static final String TEAM_MSG = "Team %s bring %d bugs in total. Production bug percent = %.1f. %s";

    private static final String FINE = "This is fine. No changes needed";
    private static final String WARNING = "Warning! Production bug percent exceeds limit of 5%!" +
            " Recommended practises: increasing tests coverage, using static code analizator, reducing technical debt.";
    private static final String CRITICAL = "Warning!! Production bug percent is more than 15! " +
            "Team must stop implementing new tasks and focus to reduce code debt";


    public String getProdBugsPercent(int teamId, LocalDate start, LocalDate end) {
        Team team = teamRepository.getById(teamId);
        List<Bug> bugs = bugRepository.findAllByAppearDateBetweenAndBelongsTo(start, end, team);
        long totalCount = bugs.size();
        long prodBugs = bugs.stream()
                .filter(bug -> !bug.isFixed() && bug.getStatus() == BugStatus.PROD)
                .count();
        double prodPercent = prodBugs * 100.0 / totalCount;
        String resultMsg;
        if (prodPercent <= 5.) {
            resultMsg = FINE;
        } else if (prodPercent <= 15.) {
            resultMsg = WARNING;
        } else {
            resultMsg = CRITICAL;
        }
        return String.format(TEAM_MSG, team.getName(), totalCount, prodPercent, resultMsg);
    }
}
