package com.tingeso.ms1.Controller;

import com.tingeso.ms1.Entity.TariffEntity;
import com.tingeso.ms1.Repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifas")
public class TariffController {

    @Autowired
    private TariffRepository tariffRepository;

    @GetMapping
    public List<TariffEntity> getAllTarifas() {
        return tariffRepository.findAll();
    }

    @GetMapping("/{id}")
    public TariffEntity getTarifaById(@PathVariable Long id) {
        Optional<TariffEntity> tarifa = tariffRepository.findById(id);
        return tarifa.orElse(null);
    }

    @PostMapping
    public TariffEntity createTarifa(@RequestBody TariffEntity tarifa) {
        return tariffRepository.save(tarifa);
    }

    @PutMapping("/{id}")
    public TariffEntity updateTarifa(@PathVariable Long id, @RequestBody TariffEntity nuevaTarifa) {
        return tariffRepository.findById(id).map(tarifa -> {
            tarifa.setTipo(nuevaTarifa.getTipo());
            tarifa.setDescripcion(nuevaTarifa.getDescripcion());
            tarifa.setPrecio(nuevaTarifa.getPrecio());
            tarifa.setDuracionMinutos(nuevaTarifa.getDuracionMinutos());
            return tariffRepository.save(tarifa);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteTarifa(@PathVariable Long id) {
        tariffRepository.deleteById(id);
    }

    @GetMapping("/duracion/{duracionMinutos}")
    public ResponseEntity<TariffEntity> getTariffByDuration(@PathVariable int duracionMinutos) {
        TariffEntity tarifa = tariffRepository.findByDuracionMinutos(duracionMinutos);
        if (tarifa != null) {
            return ResponseEntity.ok(tarifa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
