package com.tingeso.ms5.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDTO {
    private Long id;
    private String tipo;
    private String descripcion;
    private int precio;
    private int duracionMinutos;
}
