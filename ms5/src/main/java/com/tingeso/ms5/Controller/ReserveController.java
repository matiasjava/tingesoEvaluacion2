package com.tingeso.ms5.Controller;

import com.tingeso.ms5.DTOs.MemberDTO;
import com.tingeso.ms5.DTOs.ReserveRequestDTO;
import com.tingeso.ms5.Entity.ReserveEntity;
import com.tingeso.ms5.Service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
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

    @GetMapping
    public List<Map<String, Object>> getAllReserves() {
        return reserveService.getAllReserves().stream().map(reserve -> {
            Map<String, Object> formattedReserve = new HashMap<>();
            formattedReserve.put("fecha_reserva", reserve.getFechaUso().toString());
            formattedReserve.put("hora_inicio", reserve.getHoraInicio());
            formattedReserve.put("hora_fin", reserve.getHoraFin());
            formattedReserve.put("codigo_reserva", reserve.getCodigoReserva());
            formattedReserve.put("monto_final", String.valueOf(reserve.getMontoFinal()));
            formattedReserve.put("vueltas_o_tiempo", String.valueOf(reserve.getVueltasOTiempo()));

            List<Map<String, Object>> detallesList = reserve.getDetalles().stream().map(detalle -> {
                Map<String, Object> detalleMap = new HashMap<>();
                detalleMap.put("monto_final", detalle.getMontoFinal());
                return detalleMap;
            }).collect(Collectors.toList());

            formattedReserve.put("detalles", detallesList);

            return formattedReserve;
        }).collect(Collectors.toList());
    }



} //formattedReserve.put("cantidadpersonas", String.valueOf(reserve.getCantidadPersonas()));