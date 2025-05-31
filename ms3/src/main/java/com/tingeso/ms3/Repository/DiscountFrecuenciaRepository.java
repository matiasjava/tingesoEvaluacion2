package com.tingeso.ms3.Repository;

import com.tingeso.ms3.Entity.DiscountFrecuenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountFrecuenciaRepository extends JpaRepository<DiscountFrecuenciaEntity, Long> {
}
