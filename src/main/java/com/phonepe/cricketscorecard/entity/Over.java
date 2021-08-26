package com.phonepe.cricketscorecard.entity;

import com.phonepe.cricketscorecard.enums.OverStatus;
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
public class Over {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Integer overNumber;

    private Integer matchId;

    private Integer teamId;

    private Integer bowlerId;

    private Integer totalBowls;

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private OverStatus status;

    @OneToMany(mappedBy = "over")
    private List<Bowling> bowlingList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bowlerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matchId", referencedColumnName = "id", insertable = false, updatable = false)
    private Match Match;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId", referencedColumnName = "id", insertable = false, updatable = false)
    private Team team;

}
