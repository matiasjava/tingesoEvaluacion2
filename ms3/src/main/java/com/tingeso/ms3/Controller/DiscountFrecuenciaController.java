package com.tingeso.ms3.Controller;

import com.tingeso.ms3.Entity.DiscountFrecuenciaEntity;
import com.tingeso.ms3.Repository.DiscountFrecuenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frecuencia")
@CrossOrigin(origins = "*")
public class DiscountFrecuenciaController {

    @Autowired
    private DiscountFrecuenciaRepository repository;

    @GetMapping
    public List<DiscountFrecuenciaEntity> getAll() {
        return repository.findAll();
    }


    @GetMapping("/{visitas}")
    public ResponseEntity<DiscountFrecuenciaEntity> obtenerDescuento(@PathVariable int visitas) {
        DiscountFrecuenciaEntity descuento = repository.findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(visitas, visitas);
        if (descuento != null) {
            return ResponseEntity.ok(descuento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

