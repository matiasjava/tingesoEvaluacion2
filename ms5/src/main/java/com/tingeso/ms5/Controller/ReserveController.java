package com.tingeso.ms5.Controller;

import com.tingeso.ms5.DTOs.MemberDTO;
import com.tingeso.ms5.DTOs.ReserveRequestDTO;
import com.tingeso.ms5.Entity.ReserveEntity;
import com.tingeso.ms5.Service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin("*")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarReserva(@RequestBody ReserveRequestDTO dto) {
        try {
            if (dto.getMiembros() == null || dto.getMiembros().isEmpty()) {
                return ResponseEntity.badRequest().body("La reserva debe incluir al menos un miembro.");
            }

            for (MemberDTO miembro : dto.getMiembros()) {
                if (miembro.getUserId() == null) {
                    return ResponseEntity.badRequest().body("Cada miembro debe incluir un userId válido.");
                }
            }

            ReserveEntity savedReserva = reserveService.processReservation(dto);
            return ResponseEntity.ok(savedReserva);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al confirmar la reserva.");
        }
    }


}