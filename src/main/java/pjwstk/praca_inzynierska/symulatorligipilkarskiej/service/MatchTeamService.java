package pjwstk.praca_inzynierska.symulatorligipilkarskiej.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Enum.StatusOfMatch;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Season;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.SeasonTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Team;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User.Player;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.repository.*;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchTeamService {

    private final MatchTeamRepository matchTeamRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonTeamRepository seasonTeamRepository;
    private final UserRepository<Player> playerUserRepository;


    public void generateSchedule() {


        MatchTeam.oldMatch.clear();
        matchTeamRepository.deleteAll();
        seasonRepository.deleteAll();
        seasonTeamRepository.deleteAll();;


        if (teamRepository.findAll().size() % 2 == 0) {


            Season season = Season.builder()
                    .seasonStart(LocalDate.now())
                    .seasonEnd(LocalDate.now().plusYears(1))
                    .match(new LinkedHashSet<>())
                    .seasonTeams(new LinkedHashSet<>()).build();


            seasonRepository.save(season);

            List<Team> allTeamsNoModify = teamRepository.findAll();
            List<Team> allTeams = teamRepository.findAll();


            for (Team team : allTeams) {

                SeasonTeam seasonTeam = SeasonTeam.builder().team(team).season(season).points(0).goals(0).matchesDone(0).isCurrently(true).currentlyPlace(0).build();


                seasonTeamRepository.save(seasonTeam);
            }


            int i = teamRepository.findAll().size() - 1;


            int queue = 1;
            LocalDate localDate = LocalDate.now();

            Random random = new Random();

            int firstRandom;
            int secondRandom;


            while (i != 0) {

                while (allTeams.size() != 0) {


                    firstRandom = random.nextInt(allTeams.size());
                    secondRandom = random.nextInt(allTeams.size());

                    while (firstRandom == secondRandom) {
                        secondRandom = random.nextInt(allTeams.size());
                    }
                    Team firstTeam = allTeams.get(firstRandom);
                    Team secondTeam = allTeams.get(secondRandom);

                    MatchTeam matchTeam =
                            MatchTeam.builder()
                                    .homeTeam(firstTeam)
                                    .visitTeam(secondTeam)
                                    .season(season)
                                    .queue(queue)
                                    .dateOfGame(localDate)
                                    .statusOfMatch(StatusOfMatch.SCHEDULED)
                                    .build();

                    if (allTeams.size() == 2 && isDuplication(matchTeam)) {


                        List<MatchTeam> toRemove = new ArrayList<MatchTeam>();


                        for (MatchTeam tmp : MatchTeam.oldMatch) {
                            if (tmp.getQueue() == queue) {
                                toRemove.add(tmp);
                            }
                        }

                        queue = queue - 1;
                        i = i + 1;
                        localDate = localDate.minusWeeks(2);

                        for (MatchTeam tmp2 : MatchTeam.oldMatch) {
                            if (tmp2.getQueue() == queue) {
                                toRemove.add(tmp2);
                            }
                        }
                        MatchTeam.oldMatch.removeAll(toRemove);

                        matchTeam.setQueue(20);
                        allTeams.clear();
                        allTeams.addAll(allTeamsNoModify);
                    }


                    if (!isDuplication(matchTeam) && matchTeam.getQueue() != 20) {

                        MatchTeam.oldMatch.add(matchTeam);


                        if (firstRandom > secondRandom) {
                            allTeams.remove(firstRandom);
                            allTeams.remove(secondRandom);
                        } else {
                            allTeams.remove(secondRandom);
                            allTeams.remove(firstRandom);
                        }
                    }

                }
                i--;
                queue++;
                localDate = localDate.plusWeeks(2);
                allTeams.addAll(teamRepository.findAll());
            }
            for (MatchTeam tmp : MatchTeam.oldMatch) {
                matchTeamRepository.save(tmp);
            }


        } else {

            Season season = Season.builder()
                    .seasonStart(LocalDate.now())
                    .seasonEnd(LocalDate.now().plusYears(1))
                    .match(new LinkedHashSet<>())
                    .seasonTeams(new LinkedHashSet<>()).build();


            seasonRepository.save(season);

            List<Team> allTeams = teamRepository.findAll();

            List<Team> currentlyTeams;

            for (Team team : allTeams) {

                SeasonTeam seasonTeam = SeasonTeam.builder().team(team).season(season).points(0).goals(0).matchesDone(0).isCurrently(true).currentlyPlace(0).build();


                seasonTeamRepository.save(seasonTeam);
            }


            int i = teamRepository.findAll().size();
            int queue = 1;
            int count = 0;
            LocalDate localDate = LocalDate.now();

            Random random = new Random();

            int firstRandom;
            int secondRandom;


            while (i != 0) {


                allTeams = teamRepository.findAll();
                allTeams.remove(count);
                currentlyTeams = new ArrayList<>(allTeams);

                while (allTeams.size() != 0) {


                    firstRandom = random.nextInt(allTeams.size());
                    secondRandom = random.nextInt(allTeams.size());

                    while (firstRandom == secondRandom) {
                        secondRandom = random.nextInt(allTeams.size());
                    }
                    Team firstTeam = allTeams.get(firstRandom);
                    Team secondTeam = allTeams.get(secondRandom);

                    MatchTeam matchTeam =
                            MatchTeam.builder()
                                    .homeTeam(firstTeam)
                                    .visitTeam(secondTeam)
                                    .season(season)
                                    .queue(queue)
                                    .dateOfGame(localDate)
                                    .statusOfMatch(StatusOfMatch.SCHEDULED)
                                    .build();


                    if (allTeams.size() == 2 && isDuplication(matchTeam)) {
                        MatchTeam.oldMatch.remove(MatchTeam.oldMatch.size() - 1);
                        MatchTeam.oldMatch.remove(MatchTeam.oldMatch.size() - 1);
                        allTeams.clear();
                        allTeams.addAll(currentlyTeams);

                        for (MatchTeam tmp : matchTeamRepository.findAll()) {
                            if (tmp.getQueue() == queue) {
                                matchTeamRepository.delete(tmp);
                            }
                        }

                    }


                    if (!isDuplication(matchTeam)) {

                        MatchTeam.oldMatch.add(matchTeam);
                        matchTeamRepository.save(matchTeam);


                        if (firstRandom > secondRandom) {
                            allTeams.remove(firstRandom);
                            allTeams.remove(secondRandom);
                        } else {
                            allTeams.remove(secondRandom);
                            allTeams.remove(firstRandom);
                        }
                    }

                }
                count++;
                i--;
                queue++;
                localDate = localDate.plusWeeks(2);
                allTeams.addAll(teamRepository.findAll());

            }

        }

    }


    private boolean isDuplication(MatchTeam tmp) {
        for (MatchTeam oldMatch : MatchTeam.oldMatch) {
            if (oldMatch.isDuplicate(tmp)) {
                return true;
            }
        }
        return false;
    }


    public void generateTable(MatchTeam matchTeam) {

        String currentlyScore = matchTeam.getScore();

        String previousScore = matchTeamRepository.findById(matchTeam.getId()).orElse(null).getScore();

        MatchTeam matchTeamToSave = matchTeamRepository.findById(matchTeam.getId()).orElse(null);


        if (currentlyScore != null && !currentlyScore.isEmpty()) {
            SeasonTeam firstTeam = seasonTeamRepository.findByTeam_Id(matchTeam.getHomeTeam().getId());
            SeasonTeam secondTeam = seasonTeamRepository.findByTeam_Id(matchTeam.getVisitTeam().getId());

            Integer firstTeamGoalsCurrently = Integer.parseInt(String.valueOf(currentlyScore.charAt(0)));
            Integer secondTeamGoalsCurrently = Integer.parseInt(String.valueOf(currentlyScore.charAt(2)));


            System.out.println(firstTeamGoalsCurrently);
            System.out.println(secondTeamGoalsCurrently);


            Integer firstTeamGoalsPrevious = 0;
            Integer secondTeamGoalsPrevious = 0;


            if (previousScore != null && !previousScore.isEmpty()) {
                firstTeamGoalsPrevious = Integer.parseInt(String.valueOf(previousScore.charAt(0)));
                secondTeamGoalsPrevious = Integer.parseInt(String.valueOf(previousScore.charAt(2)));

            }

            if (previousScore == null || previousScore.isEmpty()) {
                firstTeam.setMatchesDone(firstTeam.getMatchesDone() + 1);
                secondTeam.setMatchesDone(secondTeam.getMatchesDone() + 1);

            }

            firstTeam.setGoals(firstTeam.getGoals() - firstTeamGoalsPrevious + firstTeamGoalsCurrently);
            secondTeam.setGoals(secondTeam.getGoals() - secondTeamGoalsPrevious + secondTeamGoalsCurrently);


            if (whoWon(firstTeamGoalsCurrently, secondTeamGoalsCurrently).equals("firstTeamWon")) {


                System.out.println("Teraz wygrała 1");

                if (previousScore == null || previousScore.isEmpty()) {
                    firstTeam.setPoints(firstTeam.getPoints() + 3);


                } else {

                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {


                        firstTeam.setPoints(firstTeam.getPoints() + 3);
                        secondTeam.setPoints(secondTeam.getPoints() - 3);

                    }
                    //w poprzednim był remis
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {


                        firstTeam.setPoints(firstTeam.getPoints() - 1 + 3);
                        secondTeam.setPoints(secondTeam.getPoints() - 1);


                        // w poprzednim wygrała 1?
                    } else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        //nic sie nie dzieje

                        // Mecz odbył się 1x
                    }
                }
            }
            //Teraz wygrała 2
            else if (whoWon(firstTeamGoalsCurrently, secondTeamGoalsCurrently).equals("secondTeamWon")) {

                System.out.println("Teraz wygrałą 2");

                if (previousScore == null || previousScore.isEmpty()) {
                    secondTeam.setPoints(secondTeam.getPoints() + 3);
                } else {

                    //w poprzednim wygrała 1
                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 3);
                        secondTeam.setPoints(secondTeam.getPoints() + 3);
                    }
                    // był remis poprzednio
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 1);
                        secondTeam.setPoints(secondTeam.getPoints() - 1 + 3);
                    }
                    // w poprzednim wygrała 2
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {
                    }
                    // Meczy odbył się 1x
                }
                // Jeśli był teraz remis to
            } else {


                if (previousScore == null || previousScore.isEmpty()) {
                    firstTeam.setPoints(firstTeam.getPoints() + 1);
                    secondTeam.setPoints(secondTeam.getPoints() + 1);
                } else {

                    System.out.println("Teraz jest remis");


                    //w poprzednim wygrała 1
                    if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("firstTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() - 3 + 1);
                        secondTeam.setPoints(secondTeam.getPoints() + 1);
                    }
                    //w poprzednim wygrała 2
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("secondTeamWon")) {
                        firstTeam.setPoints(firstTeam.getPoints() + 1);
                        secondTeam.setPoints(secondTeam.getPoints() - 3 + 1);
                    }
                    // w poprzednim był remis
                    else if (whoWon(firstTeamGoalsPrevious, secondTeamGoalsPrevious).equals("draw")) {

                    }
                }
            }
            seasonTeamRepository.save(firstTeam);
            seasonTeamRepository.save(secondTeam);
            matchTeamToSave.setStatusOfMatch(StatusOfMatch.DONE);
            matchTeamToSave.setScore(matchTeam.getScore());
            matchTeamRepository.save(matchTeamToSave);
            setPlace();
        }


    }


    public String whoWon(Integer firstTeamGoals, Integer secondTeamGoals) {
        if (firstTeamGoals > secondTeamGoals) {
            return "firstTeamWon";
        } else if (firstTeamGoals < secondTeamGoals) {
            return "secondTeamWon";
        } else {
            return "draw";
        }
    }

    public void setPlace() {

        Integer w = 1;

        for (SeasonTeam tmp : seasonTeamRepository.findAllByOrderByPointsDescGoalsDesc()) {


            tmp.setCurrentlyPlace(w);
            seasonTeamRepository.save(tmp);
            w++;
        }

    }


    public void deleteSchedule() {
/*
        seasonRepository.deleteAll();
*/


        for (MatchTeam tmp : matchTeamRepository.findAll()) {

            for (Player tmp1 : tmp.getHomeTeamPlayers()) {
                tmp1.getMatchTeamsHome().remove(tmp);
                playerUserRepository.save(tmp1);
            }
            for (Player tmp1 : tmp.getVisitTeamPlayers()) {
                tmp1.getMatchTeamsVisit().remove(tmp);
                playerUserRepository.save(tmp1);
            }

            tmp.getHomeTeamPlayers().clear();
            tmp.getVisitTeamPlayers().clear();


            matchTeamRepository.save(tmp);

        }



        seasonRepository.deleteAll();
        matchTeamRepository.deleteAll();



    }


}
