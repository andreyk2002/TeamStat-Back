package by.bsu.fpmi.teamstat.repository;

import by.bsu.fpmi.teamstat.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

}
