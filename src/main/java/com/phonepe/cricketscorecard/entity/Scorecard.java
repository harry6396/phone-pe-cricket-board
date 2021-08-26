package com.phonepe.cricketscorecard.entity;

import com.phonepe.cricketscorecard.enums.BatsmanEnd;
import com.phonepe.cricketscorecard.enums.InningType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Scorecard {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer Id;

    @Enumerated(EnumType.STRING)
    private BatsmanEnd batsmanEnd;

    private Integer playerLineUpNumber;

    private boolean isNotOut;

    private Integer matchId;

    private Integer teamId;

    private Integer playerId;

    @Enumerated(EnumType.STRING)
    private InningType inningType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matchId", referencedColumnName = "id", insertable = false, updatable = false)
    private Match Match;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId", referencedColumnName = "id", insertable = false, updatable = false)
    private Team team;
}
