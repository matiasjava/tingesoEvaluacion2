package com.tingeso.ms2.Repository;

import com.tingeso.ms2.Entity.DiscountCantidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCantidadRepository extends JpaRepository<DiscountCantidadEntity, Long> {
}
