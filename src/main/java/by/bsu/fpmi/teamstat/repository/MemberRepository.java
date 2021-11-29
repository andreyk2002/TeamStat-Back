package by.bsu.fpmi.teamstat.repository;

import by.bsu.fpmi.teamstat.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<TeamMember, Integer> {
}
