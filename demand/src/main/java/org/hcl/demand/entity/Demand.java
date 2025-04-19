package org.hcl.demand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "demand")
public class Demand {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "SR number cannot be blank")
        @Column(name = "sr_number")
        private String srNumber;

        @Column(name = "sr_raised_date")
        @Temporal(TemporalType.DATE)
        private Date srRaisedDate;

        @Column(name = "billing_start_date")
        @Temporal(TemporalType.DATE)
        private Date billingStartDate;

        @Column(name = "sr_start_date")
        @Temporal(TemporalType.DATE)
        private Date srStartDate;

        @NotBlank(message = "SR status cannot be blank")
        @Column(name = "sr_status")
        private String srStatus;

        @NotBlank(message = "Primary skill cannot be blank")
        @Column(name = "primary_skill")
        private String primarySkill;

        @NotBlank(message = "Client name cannot be blank")
        @Column(name = "client_name")
        private String clientName;

        @NotBlank(message = "L2 LOB cannot be blank")
        @Column(name = "l2_lob")
        private String l2Lob;

        @NotBlank(message = "L4 LOB cannot be blank")
        @Column(name = "l4_lob")
        private String l4Lob;

        @NotBlank(message = "Band cannot be blank")
        @Column(name = "band")
        private String band;

        @Column(name = "sub_band")
        private String subBand;

        @NotBlank(message = "Location cannot be blank")
        @Column(name = "location")
        private String location;

        @NotNull(message = "Number of positions cannot be null")
        @Min(value = 1, message = "Number of positions must be at least 1")
        @Column(name = "no_of_position")
        private Integer noOfPosition;

        @NotBlank(message = "Initiator cannot be blank")
        @Column(name = "initiator")
        private String initiator;

        @NotNull(message = "SAP ID cannot be null")
        @Positive(message = "SAP ID must be a positive number")
        @Column(name = "sap_id")
        private Long sapId;

        @NotBlank(message = "Initiator email ID cannot be blank")
        @Email(message = "Initiator email ID must be a valid email address")
        @Column(name = "initiator_email_id")
        private String initiatorEmailId;
}