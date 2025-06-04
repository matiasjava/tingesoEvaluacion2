package com.tingeso.ms5.DTOs;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReserveRequestDTO {
    private LocalDate fechaUso;
    private String horaInicio;
    private String horaFin;
    private Long clienteId;
    private int vueltasOTiempo;
    private List<MemberDTO> miembros;
}