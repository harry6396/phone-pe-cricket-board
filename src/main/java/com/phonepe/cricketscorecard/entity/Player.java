package com.phonepe.cricketscorecard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer Id;

    private String name;

    private Integer teamId;

    @OneToMany(mappedBy = "player")
    private List<Scorecard> scorecardList;

    @OneToMany(mappedBy = "player")
    private List<Bowling> bowlingList;

    @OneToOne
    @JoinColumn(name = "teamId", referencedColumnName = "id", insertable = false, updatable = false)
    private Team team;

}
