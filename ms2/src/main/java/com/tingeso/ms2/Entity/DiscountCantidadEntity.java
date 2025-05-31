package com.tingeso.ms2.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "discount_cantidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCantidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int minPersonas;
    private int maxPersonas;
    private int descuento;
}
