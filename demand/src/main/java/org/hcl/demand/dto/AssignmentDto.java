package org.hcl.demand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDto {

    private Long sapid;
    private String name;
    private String assignmentStatus;
    private String assignmentName;
    private Integer assignmentScore;
    private String assignmentTakenDate;
    private String recommendation;

}
