package pjwstk.praca_inzynierska.symulatorligipilkarskiej.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "[A-ZŁĘĄŹĆŚÓŃ]([a-zęóśźżłćń]+) [A-Z]([a-zęóśźżłćń]+)", message = "2 wyrazy zacyznajace się z dużej litery")
    @NotBlank(message = "Pole nie może być puste")
    private String name;
    @Pattern(regexp = "([A-ZŁĘĄŹĆŚÓŃ+]){3,4}", message = "Od 3 do 4 liter i duże litery")
    @NotBlank(message = "Pole nie może być puste")
    private String shortName;
    @NotBlank(message = "Pole nie może być puste")
    private String colors;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private Set<Contract> contracts = new LinkedHashSet<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private Set<ManagerTeam> managerTeams = new LinkedHashSet<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "homeTeam")
    private Set<MatchTeam> homeGames = new LinkedHashSet<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visitTeam")
    private Set<MatchTeam> visitGames = new LinkedHashSet<>();


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "team")
    private Set<SeasonTeam> seasonTeams = new LinkedHashSet<>();





}
