package by.bsu.fpmi.teamstat.repository;

import by.bsu.fpmi.teamstat.entity.Task;
import by.bsu.fpmi.teamstat.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByResolutionDateBetweenAndTeam(LocalDate begin, LocalDate end, Team team);


}
