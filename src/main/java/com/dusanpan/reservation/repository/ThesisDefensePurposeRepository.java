package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.purpose.ThesisDefensePurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisDefensePurposeRepository extends JpaRepository<ThesisDefensePurpose, Long> {
}
