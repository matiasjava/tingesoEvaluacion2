package com.tingeso.ms4.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FechaDescuentoRequest {
    private LocalDate fecha;
    private int cantidad;
    private List<LocalDate> fechasCumpleanos;
}
