package com.tingeso.ms3.DataLoader;

import com.tingeso.ms3.Entity.DiscountFrecuenciaEntity;
import com.tingeso.ms3.Repository.DiscountFrecuenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DiscountFrecuenciaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.save(new DiscountFrecuenciaEntity(null, "Muy frecuente", 7, 10000, 30));
            repository.save(new DiscountFrecuenciaEntity(null, "Frecuente", 5, 6, 20));
            repository.save(new DiscountFrecuenciaEntity(null, "Regular", 2, 4, 10));
            repository.save(new DiscountFrecuenciaEntity(null, "No frecuente", 0, 1, 0));
        }
    }
}
