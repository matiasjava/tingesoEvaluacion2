package com.tingeso.ms5.Repository;

import com.tingeso.ms5.Entity.ReserveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<ReserveEntity, Long> {
}