package com.tingeso.ms2.DataLoader;

import com.tingeso.ms2.Entity.DiscountCantidadEntity;
import com.tingeso.ms2.Repository.DiscountCantidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DiscountCantidadRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.save(new DiscountCantidadEntity(null, 1, 2, 0));
            repository.save(new DiscountCantidadEntity(null, 3, 5, 10));
            repository.save(new DiscountCantidadEntity(null, 6, 10, 20));
            repository.save(new DiscountCantidadEntity(null, 11, 15, 30));
        }
    }
}
