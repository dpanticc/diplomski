package com.dusanpan.reservation.domain.purpose;

import com.dusanpan.reservation.domain.Purpose;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StudentOrgProjectPurpose extends Purpose {
    private String studentOrganization;
    private String projectName;
    private String projectDescription;
}
