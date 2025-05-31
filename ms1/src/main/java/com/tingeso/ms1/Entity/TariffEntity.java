package com.tingeso.ms1.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "tariff")
@Data
@NoArgsConstructor
@Getter
@Setter
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String descripcion;
    private int precio;
    private int duracionMinutos;

    public TariffEntity(String tipo, String descripcion, int precio, int duracionMinutos) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }
}
