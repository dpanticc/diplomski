package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.purpose.StudentOrgProjectPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentOrgProjectPurposeRepository extends JpaRepository<StudentOrgProjectPurpose, Long> {
}
