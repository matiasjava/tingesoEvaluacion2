package com.tingeso.ms4.DataLoader;

import com.tingeso.ms4.Entity.SpecialTariffEntity;
import com.tingeso.ms4.Repository.SpecialTariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SpecialTariffRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.save(new SpecialTariffEntity("SABADO", 0.15, 0, 0, 0, 0.0));
            repository.save(new SpecialTariffEntity("DOMINGO", 0.20, 0, 0, 0, 0.0));
            repository.save(new SpecialTariffEntity("FERIADO", 0.25, 0, 0, 0, 0.0));
            repository.save(new SpecialTariffEntity("CUMPLEAÑOS", 0.0, 3, 5, 1, 0.5));
            repository.save(new SpecialTariffEntity("CUMPLEAÑOS", 0.0, 6, 10, 2, 0.5));
        }
    }
}
