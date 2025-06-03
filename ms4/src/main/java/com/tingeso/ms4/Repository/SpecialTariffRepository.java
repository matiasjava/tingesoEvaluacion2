package com.tingeso.ms4.Repository;

import com.tingeso.ms4.Entity.SpecialTariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialTariffRepository extends JpaRepository<SpecialTariffEntity, Long> {
}
