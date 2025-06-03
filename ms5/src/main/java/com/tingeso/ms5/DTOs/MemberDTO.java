package com.tingeso.ms5.DTOs;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private String nombre;
    private LocalDate fechaNacimiento;
    private Long userId;
}