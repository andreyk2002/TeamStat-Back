package by.bsu.fpmi.teamstat.repository;

import by.bsu.fpmi.teamstat.entity.Bug;
import by.bsu.fpmi.teamstat.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Integer> {
    List<Bug> findAllByAppearDateBetweenAndBelongsTo(LocalDate begin, LocalDate end, Team team);
}
