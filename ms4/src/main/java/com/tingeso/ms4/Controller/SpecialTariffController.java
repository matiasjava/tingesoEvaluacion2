package com.tingeso.ms4.Controller;

import com.tingeso.ms4.DTOs.FechaDescuentoDTO;
import com.tingeso.ms4.DTOs.FechaDescuentoRequest;


import com.tingeso.ms4.Entity.SpecialTariffEntity;
import com.tingeso.ms4.Repository.SpecialTariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fecha-descuento")
@CrossOrigin(origins = "*")
public class SpecialTariffController {

    @Autowired
    private SpecialTariffRepository repository;

    @GetMapping
    public List<SpecialTariffEntity> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public SpecialTariffEntity create(@RequestBody SpecialTariffEntity entity) {
        return repository.save(entity);
    }

    @PostMapping("/calcular")
    public ResponseEntity<FechaDescuentoDTO> calcularDescuento(@RequestBody FechaDescuentoRequest request) {
        LocalDate fecha = request.getFecha();
        int cantidad = request.getCantidad();
        List<LocalDate> fechasCumpleanos = request.getFechasCumpleanos();

        double descuentoCumple = 0;
        int cumpleCount = (int) fechasCumpleanos.stream().filter(f -> f.equals(fecha)).count();

        if (cantidad >= 3 && cantidad <= 5 && cumpleCount >= 1) {
            descuentoCumple = 50;
        } else if (cantidad >= 6 && cantidad <= 10 && cumpleCount >= 2) {
            descuentoCumple = 100;
        }

        boolean esFinde = fecha.getDayOfWeek().getValue() >= 6; // Sábado o domingo
        boolean esFeriado = List.of(
                LocalDate.of(2025, 9, 18),
                LocalDate.of(2025, 12, 25)
        ).contains(fecha);

        FechaDescuentoDTO dto = new FechaDescuentoDTO();
        dto.setDescuentoCumple(descuentoCumple);
        dto.setEsFinde(esFinde);
        dto.setEsFeriado(esFeriado);

        return ResponseEntity.ok(dto);
    }
}
