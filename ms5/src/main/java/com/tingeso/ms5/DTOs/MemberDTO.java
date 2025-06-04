package com.tingeso.ms5.DTOs;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDTO {
    private String nombre;
    private LocalDate fechaNacimiento;
    private Long userId;
}