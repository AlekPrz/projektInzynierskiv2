package pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.SeasonTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.Model.Team;

import java.util.List;

public interface SeasonTeamRepository extends JpaRepository<SeasonTeam, Long> {

    SeasonTeam findByTeam_Id(Long teamId);

    List<SeasonTeam> findAllByOrderByPointsDescGoalsDesc();
}
