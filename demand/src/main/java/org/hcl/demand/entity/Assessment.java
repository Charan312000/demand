package org.hcl.demand.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer assessmentid;

    @NotBlank(message = "Assessment name cannot be blank")
    @Size(min = 2, max = 255, message = "Assessment name must be between 2 and 255 characters")
    private String assessmentname;

    @NotBlank(message = "Assessment type cannot be blank")
    private String assessmenttype;

    @NotBlank(message = "Assessment role cannot be blank")
    private String assessmentRole;

    @NotBlank(message = "Assessment URL cannot be blank")
    private String assessmentURl;

    @NotBlank(message = "Skill cannot be blank")
    private String skill;
}