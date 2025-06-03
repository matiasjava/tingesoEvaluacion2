package com.tingeso.ms5.Service;

import com.tingeso.ms5.Config.RestTemplateConfig;
import com.tingeso.ms5.DTOs.*;
import com.tingeso.ms5.Entity.*;
import com.tingeso.ms5.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
public class ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private ReserveDetailsRepository detailsRepository;

    @Autowired
    private RestTemplate restTemplate;


    public ReserveEntity processReservation(ReserveRequestDTO dto) {
        ReserveEntity reserve = new ReserveEntity();
        reserve.setFechaUso(dto.getFechaUso());
        reserve.setHoraInicio(dto.getHoraInicio());
        reserve.setHoraFin(dto.getHoraFin());
        reserve.setClienteId(dto.getClienteId());
        reserve.setVueltasOTiempo(dto.getVueltasOTiempo());
        reserve.setCantidadPersonas(dto.getMiembros().size());

        List<ReserveDetailsEntity> detalles = new ArrayList<>();
        double montoFinalTotal = 0;

        for (MemberDTO miembro : dto.getMiembros()) {
            ReserveDetailsEntity detalle = new ReserveDetailsEntity();
            detalle.setMemberName(miembro.getNombre());
            detalle.setDateBirthday(miembro.getFechaNacimiento());
            detalle.setUserId(miembro.getUserId());

            double montoBase = 0;
            double descuentoCantidad = 0;
            double descuentoFrecuencia = 0;
            double descuentoCumple = 0;

            double montoConDescuento = montoBase * (1 - descuentoCantidad - descuentoFrecuencia - descuentoCumple);
            detalle.setMontoFinal(montoConDescuento);
            detalle.setDiscount(descuentoCantidad + descuentoFrecuencia + descuentoCumple);
            detalle.setReserve(reserve);

            detalles.add(detalle);
            montoFinalTotal += montoConDescuento;
        }

        reserve.setMontoFinal(montoFinalTotal);
        reserve.setDetalles(detalles);

        return reserveRepository.save(reserve);
    }

    public int obtenerTarifa(int duracionMinutos) {
        String url = "http://ms1/api/tarifas/duracion/" + duracionMinutos;
        TariffDTO tarifa = restTemplate.getForObject(url, TariffDTO.class);
        if (tarifa == null) {
            throw new RuntimeException("No se encontró una tarifa para la duración: " + duracionMinutos);
        }
        return tarifa.getPrecio();
    }




}