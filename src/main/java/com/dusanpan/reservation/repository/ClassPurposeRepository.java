package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.purpose.ClassPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassPurposeRepository extends JpaRepository<ClassPurpose, Long> {
}
