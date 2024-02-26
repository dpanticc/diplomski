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
public class ClassPurpose extends Purpose {
    private String typeOfClass;
    private String semester;
    private String studyLevel;
}
