package com.tingeso.ms4.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "special_tariffs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialTariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoDia;
    private double porcentajeIncremento;
    private int cantidadMinima;
    private int cantidadMaxima;
    private int cantidadCumpleanerosAplicables;
    private double descuentoCumpleanero;


    public SpecialTariffEntity(String tipoDia, double porcentajeIncremento, int cantidadMinima, int cantidadMaxima, int cantidadCumpleanerosAplicables, double descuentoCumpleanero) {
        this.tipoDia = tipoDia;
        this.porcentajeIncremento = porcentajeIncremento;
        this.cantidadMinima = cantidadMinima;
        this.cantidadMaxima = cantidadMaxima;
        this.cantidadCumpleanerosAplicables = cantidadCumpleanerosAplicables;
        this.descuentoCumpleanero = descuentoCumpleanero;
    }
}
