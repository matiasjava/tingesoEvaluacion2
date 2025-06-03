package com.tingeso.ms5.Controller;

import com.tingeso.ms5.DTOs.ReserveRequestDTO;
import com.tingeso.ms5.Entity.ReserveEntity;
import com.tingeso.ms5.Service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin("*")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @PostMapping("/crear")
    public ReserveEntity crearReserva(@RequestBody ReserveRequestDTO dto) {
        return reserveService.processReservation(dto);
    }
}