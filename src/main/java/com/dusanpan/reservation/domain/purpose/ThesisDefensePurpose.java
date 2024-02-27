package com.dusanpan.reservation.domain.purpose;

import com.dusanpan.reservation.domain.Purpose;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ThesisDefensePurpose extends Purpose {
    private String thesisLevel;
    private String theme;
    private String supervisor;
    private String committeeMembers;

}
