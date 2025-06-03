package com.tingeso.ms5.DTOs;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReserveRequestDTO {
    private LocalDate fechaUso;
    private String horaInicio;
    private String horaFin;
    private Long clienteId;
    private String vueltasOTiempo;
    private List<MemberDTO> miembros;
}