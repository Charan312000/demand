package org.hcl.demand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "SAP ID cannot be null")
    @Positive(message = "SAP ID must be a positive number")
    @Column(name = "sap_ID")
    private Long sapid;

    @NotBlank(message = "Employee name cannot be blank")
    @Size(min = 2, max = 255, message = "Employee name must be between 2 and 255 characters")
    @Column(name = "employee_name")
    private String employeeName;

    @NotBlank(message = "Band cannot be blank")
    @Column(name = "band")
    private String band;

    @Column(name = "sub_band")
    private String subBand;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Primary skill cannot be blank")
    @Column(name = "primary_skill")
    private String primarySkill;

    @NotNull(message = "Experience cannot be null")
    @PositiveOrZero(message = "Experience must be a non-negative number")
    @Column(name = "experience")
    private Double experience;

    @NotNull(message = "Relevant experience cannot be null")
    @PositiveOrZero(message = "Relevant experience must be a non-negative number")
    @Column(name = "relevant_exp")
    private Double relevantExp;

    @NotBlank(message = "Location cannot be blank")
    @Column(name = "location")
    private String location;

    @Column(name = "bench_date")
    private LocalDate benchDate;

    @Min(value = 0, message = "Bench ageing cannot be negative")
    @Column(name = "bench_ageing")
    private Integer benchAgeing;

    @NotBlank(message = "Country cannot be blank")
    @Column(name = "country")
    private String country;

    @NotBlank(message = "Role cannot be blank")
    @Column(name = "role")
    private String role;

    @Column(name = "assignment_name")
    private String assignmentName;

    @Column(name = "assignment_score")
    private Integer assignmentScore;

    @Column(name = "assignment_taken_date")
    private String assignmentTakenDate;

    private String recommendation;

    @Column(name = "assignment_status")
    public String AssignmentStatus;

    @ManyToOne
    private Assessment assessment;

}
