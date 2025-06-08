package com.tingeso.ms7.Service;

import com.tingeso.ms7.DTOs.ReserveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private RestTemplate restTemplate;

    private final String reservasUrl = "http://ms5/api/reservas";

    public Map<String, Map<String, Double>> getReporteIngresosPorVueltasOTiempo(LocalDate fechaInicio, LocalDate fechaFin) {
        ResponseEntity<ReserveDTO[]> response =
            restTemplate.getForEntity(reservasUrl, ReserveDTO[].class);
        List<ReserveDTO> reservas = Arrays.asList(Objects.requireNonNull(response.getBody()));

        Map<String, Map<String, Double>> reporte = new LinkedHashMap<>();

        List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<String> categorias = Arrays.asList("10 vueltas o máx 10 min", "15 vueltas o máx 15 min", "20 vueltas o máx 20 min");

        for (String categoria : categorias) {
            Map<String, Double> ingresosPorMes = new LinkedHashMap<>();
            for (String mes : meses) {
                ingresosPorMes.put(mes, 0.0);
            }
            ingresosPorMes.put("TOTAL", 0.0);
            reporte.put(categoria, ingresosPorMes);
        }

        List<ReserveDTO> reservasFiltradas = reservas.stream()
                .filter(reserva -> reserva.getFecha_reserva() != null)
                .filter(reserva -> !reserva.getFecha_reserva().isBefore(fechaInicio) &&
                        !reserva.getFecha_reserva().isAfter(fechaFin))
                .collect(Collectors.toList());

        for (ReserveDTO reserva : reservasFiltradas) {
            String categoria = switch (reserva.getVueltas_o_tiempo()) {
                case "30.0" -> "10 vueltas o máx 10 min";
                case "35.0" -> "15 vueltas o máx 15 min";
                case "40.0" -> "20 vueltas o máx 20 min";
                default -> "Otros";
            };

            if (!reporte.containsKey(categoria)) continue;

            String mes = meses.get(reserva.getFecha_reserva().getMonthValue() - 1);
            double monto = Double.parseDouble(reserva.getMonto_final());

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

    public Map<String, Map<String, Double>> getReporteIngresosPorCantidadDePersonas(
            LocalDate fechaInicio, LocalDate fechaFin) {
        ResponseEntity<ReserveDTO[]> resp =
            restTemplate.getForEntity(reservasUrl, ReserveDTO[].class);
        List<ReserveDTO> reservas = Arrays.asList(Objects.requireNonNull(resp.getBody()));

        Map<String, Map<String, Double>> reporte = new LinkedHashMap<>();
        List<String> meses = List.of("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        List<String> rangos = List.of("1-2 personas","3-5 personas",
                                      "6-10 personas","11-15 personas");
        // inicializar el mapa
        for (String rango : rangos) {
            Map<String, Double> ing = new LinkedHashMap<>();
            for (String mes : meses) ing.put(mes, 0.0);
            ing.put("TOTAL", 0.0);
            reporte.put(rango, ing);
        }

        // filtrar y acumular
        reservas.stream()
            .filter(r -> r.getFecha_reserva() != null)
            .filter(r -> !r.getFecha_reserva().isBefore(fechaInicio) &&
                         !r.getFecha_reserva().isAfter(fechaFin))
            .forEach(r -> {
                int c = r.getDetalles().size();
                String rango = getRangoPorCantidadDePersonas(c);
                String mes = meses.get(r.getFecha_reserva().getMonthValue() - 1);
                double monto = Double.parseDouble(r.getMonto_final());
                Map<String, Double> ing = reporte.get(rango);
                ing.put(mes, ing.get(mes) + monto);
                ing.put("TOTAL", ing.get("TOTAL") + monto);
            });

        Map<String, Double> totalPorMes = new LinkedHashMap<>();
        for (String mes : meses) {
            double suma = reporte.values()
                .stream()
                .mapToDouble(m -> m.get(mes))
                .sum();
            totalPorMes.put(mes, suma);
        }
        totalPorMes.put("TOTAL",
                totalPorMes.values().stream().mapToDouble(Double::doubleValue).sum());
        reporte.put("TOTAL", totalPorMes);

        return reporte;   // ← aquí devolvemos el mapa construido
    }


    public String getRangoPorCantidadDePersonas(int cantidadPersonas) {
        if (cantidadPersonas >= 1 && cantidadPersonas <= 2) return "1-2 personas";
        else if (cantidadPersonas <= 5) return "3-5 personas";
        else if (cantidadPersonas <= 10) return "6-10 personas";
        else if (cantidadPersonas <= 15) return "11-15 personas";
        return null;
    }
}
