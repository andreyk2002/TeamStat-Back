package by.bsu.fpmi.teamstat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Issues {
    private int total;
    private List<Object> issues;
}
