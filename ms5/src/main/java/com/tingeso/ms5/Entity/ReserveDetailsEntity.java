package com.tingeso.ms5.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reserve_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReserveDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;
    private LocalDate dateBirthday;
    private double montoFinal;
    private double discount;
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "reserve_id")
    private ReserveEntity reserve;
}