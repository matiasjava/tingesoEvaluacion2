package com.tingeso.ms1.CargaDatos;

import com.tingeso.ms1.Entity.TariffEntity;
import com.tingeso.ms1.Repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TariffRepository tariffRepository;

    @Override
    public void run(String... args) throws Exception {
        if (tariffRepository.count() == 0) {
            tariffRepository.save(new TariffEntity("Vueltas", "10 vueltas o máx 10 min", 15000, 30));
            tariffRepository.save(new TariffEntity("Vueltas", "15 vueltas o máx 15 min", 20000, 35));
            tariffRepository.save(new TariffEntity("Vueltas", "20 vueltas o máx 20 min", 25000, 40));
        }
    }
}

