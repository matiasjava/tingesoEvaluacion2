package com.tingeso.ms5.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reserves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigoReserva;

    private LocalDate fechaUso;
    private String horaInicio;
    private String horaFin;
    private Long clienteId;
    private int cantidadPersonas;
    private String vueltasOTiempo;
    private double montoFinal;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReserveDetailsEntity> detalles = new ArrayList<>();

    @PrePersist
    private void generateCodigoReserva() {
        if (this.codigoReserva == null || this.codigoReserva.isEmpty()) {
            this.codigoReserva = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}