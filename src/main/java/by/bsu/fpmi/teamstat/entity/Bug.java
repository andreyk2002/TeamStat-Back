package by.bsu.fpmi.teamstat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "bug")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "appear_date")
    private LocalDate appearDate;

    @Column(name = "fixed")
    private boolean fixed;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BugStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team belongsTo;

}
