package com.tingeso.ms7.DTOs;

import java.time.LocalDate;
import java.util.List;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReserveDTO {
    private LocalDate fecha_reserva;
    private String hora_inicio;
    private String hora_fin;
    private String codigo_reserva;
    private String monto_final;
    private String vueltas_o_tiempo;
    private List<ReserveDetailsDTO> detalles;
}
