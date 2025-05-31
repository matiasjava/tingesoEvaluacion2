package com.tingeso.ms2.Controller;

import com.tingeso.ms2.Entity.DiscountCantidadEntity;
import com.tingeso.ms2.Repository.DiscountCantidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentoscantidad")
public class DiscountCantidadController {

    @Autowired
    private DiscountCantidadRepository repository;

    @GetMapping
    public List<DiscountCantidadEntity> getAll() {
        return repository.findAll();
    }
}
