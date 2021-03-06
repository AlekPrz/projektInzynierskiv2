package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.User;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Contract;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.MatchTeam;
import pjwstk.praca_inzynierska.symulatorligipilkarskiej.model.Position;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "PlayerId")
@Data

public class Player extends User {

    @Pattern(regexp = "[A-ZŁĘĄŹĆŚÓŃ][a-zęóśźżłćń]+", message = " Imie musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String name;
    @Pattern(regexp = "[A-ZŁĘĄŹĆŚÓŃ][a-zęóśźżłćń]+", message = " Nazwisko musi zaczynać się z dużej litery, reszta z małej, bez spacji")
    private String surname;
    @Enumerated(EnumType.STRING)
    private Position position;
    private String shirtName;

    @Min(value = 16, message = "Wiek musi być większy niż 16")
    private Integer age;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Contract> contracts = new LinkedHashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "MatchTeamHomePlayers",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "matchTeam_id")}
    )
    Set<MatchTeam> matchTeamsHome = new LinkedHashSet<>();

    public void removeMatchTeamHome(MatchTeam matchTeam) {
        this.matchTeamsHome.remove(matchTeam);
        matchTeam.getHomeTeamPlayers().remove(this);
    }


    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "MatchTeamVisitPlayers",
            joinColumns = {@JoinColumn(name = "player_id")},
            inverseJoinColumns = {@JoinColumn(name = "matchTeam_id")}
    )
    Set<MatchTeam> matchTeamsVisit = new LinkedHashSet<>();


}
