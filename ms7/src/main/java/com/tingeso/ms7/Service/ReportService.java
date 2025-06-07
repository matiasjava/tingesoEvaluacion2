package com.tingeso.ms7.Service;


import com.tingeso.ms7.DTOs.ReserveDTO;
import com.tingeso.ms7.DTOs.ReserveDetailsDTO;
import com.tingeso.ms7.Entity.ReserveEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String reservasUrl = "http://ms5/api/reservas";

    public Map<String, Map<String, Double>> getReporteIngresosPorVueltasOTiempo(LocalDate fechaInicio, LocalDate fechaFin) {
        ResponseEntity<ReserveDTO[]> response = restTemplate.getForEntity(reservasUrl, ReserveDTO[].class);
        List<ReserveDTO> reservas = Arrays.asList(response.getBody());


        System.out.println("Reservas totales: " + reservas);

        Map<String, Map<String, Double>> reporte = new LinkedHashMap<>();

        List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<String> categorias = Arrays.asList("10 vueltas o máx 10 min", "15 vueltas o máx 15 min", "20 vueltas o máx 20 min");

        for (String categoria : categorias) {
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            for (String mes : meses) {
                ingresosPorMes.put(mes, 0.0);
            }
            ingresosPorMes.put("TOTAL", 0.0);
            reporte.put(categoria, ingresosPorMes);
        }

        // Filtrar reservas entre las fechas dadas
        List<ReserveDTO> reservasFiltradas = reservas.stream()
                .filter(reserva -> reserva.getFecha_reserva() != null)
                .filter(reserva -> !reserva.getFecha_reserva().isBefore(fechaInicio) &&
                        !reserva.getFecha_reserva().isAfter(fechaFin))
                .collect(Collectors.toList());

        System.out.println("Reservas filtradas: " + reservasFiltradas);

        for (ReserveDTO reserva : reservasFiltradas) {
            String categoria = switch (reserva.getVueltas_o_tiempo()) {
                case "30.0", "10 minutos" -> "10 vueltas o máx 10 min";
                case "35.0", "15 minutos" -> "15 vueltas o máx 15 min";
                case "40.0", "20 minutos" -> "20 vueltas o máx 20 min";
                default -> "Otros";
            };

            String mes = meses.get(reserva.getFecha_reserva().getMonthValue() - 1); // nombre del mes
            double monto = Double.parseDouble(reserva.getMonto_final());

            // Sumar ingresos al mes correspondiente
            Map<String, Double> ingresosPorMes = reporte.get(categoria);
            ingresosPorMes.put(mes, ingresosPorMes.get(mes) + monto);
            ingresosPorMes.put("TOTAL", ingresosPorMes.get("TOTAL") + monto);
        }

        Map<String, Double> totalPorMes = new LinkedHashMap<>();
        for (String mes : meses) {
            double totalMes = reporte.values().stream()
                    .mapToDouble(ingresosPorMes -> ingresosPorMes.get(mes))
                    .sum();
            totalPorMes.put(mes, totalMes);
        }
        totalPorMes.put("TOTAL", totalPorMes.values().stream().mapToDouble(Double::doubleValue).sum());
        reporte.put("TOTAL", totalPorMes);

        return reporte;
    }

    public Map<String, Map<String, Double>> getReporteIngresosPorCantidadDePersonas(LocalDate fechaInicio, LocalDate fechaFin) {
        ResponseEntity<ReserveDTO[]> response = restTemplate.getForEntity(reservasUrl, ReserveDTO[].class);
        List<ReserveDTO> reservas = Arrays.asList(response.getBody());

        Map<String, Map<String, Double>> reporte = new LinkedHashMap<>();

        List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<String> rangos = Arrays.asList("1-2 personas", "3-5 personas", "6-10 personas", "11-15 personas");

        for (String rango : rangos) {
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            for (String mes : meses) {
                ingresosPorMes.put(mes, 0.0);
            }
            ingresosPorMes.put("TOTAL", 0.0);
            reporte.put(rango, ingresosPorMes);
        }

        List<ReserveDTO> reservasFiltradas = reservas.stream()
                .filter(reserva -> reserva.getFecha_reserva() != null)
                .filter(reserva -> !reserva.getFecha_reserva().isBefore(fechaInicio) &&
                        !reserva.getFecha_reserva().isAfter(fechaFin))
                .collect(Collectors.toList());

        for (ReserveDTO reserva : reservasFiltradas) {
            int cantidadPersonas = reserva.getDetalles().size();
            String rango = getRangoPorCantidadDePersonas(cantidadPersonas);
            if (rango == null) continue;

            String mes = meses.get(reserva.getFecha_reserva().getMonthValue() - 1);
            double monto = reserva.getDetalles().stream()
                    .mapToDouble(detalle -> Double.parseDouble(detalle.getMonto_final()))
                    .sum();

            Map<String, Double> ingresosPorMes = reporte.get(rango);
            ingresosPorMes.put(mes, ingresosPorMes.get(mes) + monto);
            ingresosPorMes.put("TOTAL", ingresosPorMes.get("TOTAL") + monto);
        }

        // Totales por mes
        Map<String, Double> totalPorMes = new LinkedHashMap<>();
        for (String mes : meses) {
            double totalMes = reporte.values().stream()
                    .mapToDouble(ingresosPorMes -> ingresosPorMes.get(mes))
                    .sum();
            totalPorMes.put(mes, totalMes);
        }
        totalPorMes.put("TOTAL", totalPorMes.values().stream().mapToDouble(Double::doubleValue).sum());
        reporte.put("TOTAL", totalPorMes);

        return reporte;
    }


    public String getRangoPorCantidadDePersonas(int cantidadPersonas) {
        if (cantidadPersonas >= 1 && cantidadPersonas <= 2) {
            return "1-2 personas";
        } else if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            return "3-5 personas";
        } else if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            return "6-10 personas";
        } else if (cantidadPersonas >= 11 && cantidadPersonas <= 15) {
            return "11-15 personas";
        }
        return null;
    }
}