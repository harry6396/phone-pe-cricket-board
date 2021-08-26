package com.phonepe.cricketscorecard.entity;

import com.phonepe.cricketscorecard.enums.InningType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer Id;

    private String venue;

    private Integer overs;

    private Integer noOfTeamMembers;

    @Column(name = "teamA")
    private String teamA;

    @Column(name = "teamB")
    private String teamB;

    @Enumerated(EnumType.STRING)
    private InningType inningType;

    private Instant createdTime;

    @OneToMany(mappedBy = "Match")
    private List<Scorecard> scorecardList;

    @OneToMany(mappedBy = "Match")
    private List<Bowling> bowlingList;

    @OneToMany(mappedBy = "Match")
    private List<Over> overList;
}
