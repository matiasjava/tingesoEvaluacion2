package com.tingeso.ms2.Controller;

import com.tingeso.ms2.Entity.DiscountCantidadEntity;
import com.tingeso.ms2.Repository.DiscountCantidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/descuentoscantidad")
public class DiscountCantidadController {

    @Autowired
    private DiscountCantidadRepository repository;

    @GetMapping
    public List<DiscountCantidadEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{cantidad}")
    @ResponseBody
    public double obtenerDescuento(@PathVariable int cantidad) {
        DiscountCantidadEntity descuentoEntity = repository.findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(cantidad, cantidad);

        if (descuentoEntity != null) {
            return descuentoEntity.getDescuento() / 100.0;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Descuento no encontrado");
        }
    }
}
