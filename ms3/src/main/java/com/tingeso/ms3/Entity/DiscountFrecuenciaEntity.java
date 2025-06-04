package com.tingeso.ms3.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "discount_frecuencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountFrecuenciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;
    private int minVisitas;
    private int maxVisitas;
    private double descuento;
}