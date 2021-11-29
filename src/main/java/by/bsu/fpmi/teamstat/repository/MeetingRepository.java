package by.bsu.fpmi.teamstat.repository;

import by.bsu.fpmi.teamstat.entity.Meeting;
import by.bsu.fpmi.teamstat.entity.Team;
import by.bsu.fpmi.teamstat.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    List<Meeting> findAllByEndDateBetweenAndTeam(LocalDateTime start, LocalDateTime end, Team team);

    List<Meeting> findAllByEndDateBetweenAndPresentMembersContains(LocalDateTime begin, LocalDateTime end, TeamMember member);

}
