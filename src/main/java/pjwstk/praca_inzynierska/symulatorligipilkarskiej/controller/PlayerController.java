package pjwstk.praca_inzynierska.symulatorligipilkarskiej.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.helpingMethods.Counter;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.ManagerService;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.service.PlayerService;

import java.util.List;

@Controller
@RequestMapping("/zawodnik")
@RequiredArgsConstructor

public class PlayerController {
    private final PlayerService playerService;
    private final ManagerService managerService;

    @GetMapping("/mojeMecze")
    public String getSchedule(Model model) {


        System.out.println("witam");
        List<MatchTeam> getMyTeamMatches = playerService.findMyMatches();




        model.addAttribute("sum", new Counter());
        model.addAttribute("currentUser", managerService.getCurrentUser().getUsername());
        model.addAttribute("myMatches", getMyTeamMatches);

        return "player/myMatches";
    }
}
