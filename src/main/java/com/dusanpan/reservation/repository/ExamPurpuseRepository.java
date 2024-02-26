package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.purpose.ExamPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPurpuseRepository extends JpaRepository<ExamPurpose, Long> {
}
