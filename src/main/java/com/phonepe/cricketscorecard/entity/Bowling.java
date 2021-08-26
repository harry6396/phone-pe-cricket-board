package com.phonepe.cricketscorecard.entity;

import com.phonepe.cricketscorecard.enums.ScoreType;
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
public class Bowling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Integer extra;

    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

    private Integer matchId;

    private Integer overId;

    private Integer bowlerId;

    private Integer batsmanId;

    private Integer batsmanTeamId;

    private Integer bowlingTeamId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bowlerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matchId", referencedColumnName = "id", insertable = false, updatable = false)
    private Match Match;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "overId", referencedColumnName = "id", insertable = false, updatable = false)
    private Over over;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "batsmanTeamId", referencedColumnName = "id", insertable = false, updatable = false)
    private Team batsmanTeam;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bowlingTeamId", referencedColumnName = "id", insertable = false, updatable = false)
    private Team bowlingTeam;

    public boolean isValidDelivery() {
        return !(ScoreType.NB.equals(this.getScoreType()) ||
                ScoreType.Wd.equals(this.getScoreType()) ||
                ScoreType.W.equals(this.getScoreType()));
    }

}
