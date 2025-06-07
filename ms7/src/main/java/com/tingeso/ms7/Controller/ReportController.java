package com.tingeso.ms7.Controller;

import com.tingeso.ms7.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/ingresos-por-vueltas-tiempo")
    public Map<String, Map<String, Double>> getIngresosPorVueltas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return reportService.getReporteIngresosPorVueltasOTiempo(fechaInicio, fechaFin);
    }

    @GetMapping("/ingresos-por-cantidad-personas")
    public Map<String, Map<String, Double>> getIngresosPorCantidadDePersonas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return reportService.getReporteIngresosPorCantidadDePersonas(fechaInicio, fechaFin);
    }


}

