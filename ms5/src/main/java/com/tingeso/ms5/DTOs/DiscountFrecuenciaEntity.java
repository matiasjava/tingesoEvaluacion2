package com.tingeso.ms5.DTOs;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DiscountFrecuenciaEntity {
    private Long id;
    private Integer minVisitas;
    private Integer maxVisitas;
    private Double descuento;


}
