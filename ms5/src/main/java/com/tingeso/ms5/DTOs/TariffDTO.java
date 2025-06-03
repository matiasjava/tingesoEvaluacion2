package com.tingeso.ms5.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TariffDTO {
    private Long id;
    private String tipo;
    private String descripcion;
    private int precio;
    private int duracionMinutos;
}
