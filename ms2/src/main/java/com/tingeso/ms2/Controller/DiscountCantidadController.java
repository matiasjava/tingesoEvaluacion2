package com.tingeso.ms2.Controller;

import com.tingeso.ms2.Entity.DiscountCantidadEntity;
import com.tingeso.ms2.Repository.DiscountCantidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{cantidad}")
    public ResponseEntity<DiscountCantidadEntity> obtenerDescuento(@PathVariable int cantidad) {
        DiscountCantidadEntity descuento = repository.findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(cantidad, cantidad);
        if (descuento != null) {
            return ResponseEntity.ok(descuento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
