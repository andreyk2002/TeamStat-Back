package by.bsu.fpmi.teamstat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Issue {
    private String summary;
    private String description;
    private String type;
    private String priority;
    private String resolution;
}
