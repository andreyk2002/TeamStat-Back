package by.bsu.fpmi.teamstat.service;

import by.bsu.fpmi.teamstat.entity.Meeting;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.repository.BugRepository;
import by.bsu.fpmi.teamstat.repository.MeetingRepository;
import by.bsu.fpmi.teamstat.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MeetingService {
    private final BugRepository bugRepository;
    private static final String TEAM_MSG = "Team %s presence percent = %.1f.";
    private static final String MEMBER_MSG = "Member %s presence percent = %.1f. %s";

    private static final String FINE = "This is fine. No changes needed";
    private static final String WARNING = "Presence percent below normal" +
            " (it's final if person have vacations during selected period)! Otherwise, you should contact the person " +
            "to ask if he have any questions.";
    private static final String CRITICAL = "Presence percent is very low! Please contact the person immeadetely, because" +
            "such low presence percent may influence team performance, cause demotivation and conflicts";

    private static final String AVG_TEAM = "Average for team";
    private final MeetingRepository meetingRepository;
    private final TeamRepository teamRepository;

    public List<String> getTeamPresencePercent(int teamId, LocalDateTime start, LocalDateTime end) {
        Team team = teamRepository.getById(teamId);
        List<Meeting> meetings = meetingRepository.findAllByEndDateBetweenAndTeam(start, end, team);
        Map<String, Double> presenceMap = new HashMap<>();
        long totalMeetings = meetings.size();
        long totalVisits = 0;
        for (var member : team.getTeamMembers()) {
            List<Meeting> memberMeetings = meetingRepository.findAllByEndDateBetweenAndPresentMembersContains(start, end, member);
            String nameSurname = member.getName() + " " + member.getSurname();
            long visits = memberMeetings.size();
            totalVisits += visits;
            Double presence = visits * 100.0 / totalMeetings;
            presenceMap.put(nameSurname, presence);
        }
        int membersInTeem = team.getTeamMembers().size();
        Double avgPresence = totalVisits * 100.0 / (membersInTeem * totalMeetings);
        presenceMap.put(AVG_TEAM, avgPresence);
        return getMessages(team.getName(), presenceMap);
    }

    private List<String> getMessages(String teamName, Map<String, Double> presenceMap) {
        List<String> messages = new ArrayList<>();
        for (var membersPresence : presenceMap.entrySet()) {
            String name = membersPresence.getKey();
            Double presence = membersPresence.getValue();
            if (Objects.equals(name, AVG_TEAM)) {
                messages.add(String.format(TEAM_MSG, teamName, presence));
            } else {
                if (presence >= 85.) {
                    messages.add(String.format(MEMBER_MSG, name, presence, FINE));
                } else if (presence >= 45.) {
                    messages.add(String.format(MEMBER_MSG, name, presence, WARNING));
                } else {
                    messages.add(String.format(MEMBER_MSG, name, presence, CRITICAL));
                }
            }
        }
        return messages;
    }
}
