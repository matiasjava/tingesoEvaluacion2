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
    private UserRepository userRepository;

    @Autowired
    private ReserveDetailsRepository detailsRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;


    public ReserveEntity processReservation(ReserveRequestDTO dto) {
        ReserveEntity reserve = new ReserveEntity();
        reserve.setFechaUso(dto.getFechaUso());
        reserve.setHoraInicio(dto.getHoraInicio());
        reserve.setHoraFin(dto.getHoraFin());
        UserEntity user = userRepository.findById(dto.getClienteId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // System.out.println(user.getRut());
        reserve.setCliente(user);
        reserve.setVueltasOTiempo(dto.getVueltasOTiempo());
        reserve.setCantidadPersonas(dto.getMiembros().size());

        List<ReserveDetailsEntity> detalles = new ArrayList<>();
        double montoFinalTotal = 0;
        int cumpleanosAplicados = 0;
        int maxCumpleanos = calcularMaxCumpleanos(dto.getMiembros().size());

        for (MemberDTO miembro : dto.getMiembros()) {
            ReserveDetailsEntity detalle = new ReserveDetailsEntity();
            detalle.setMemberName(miembro.getNombre());
            detalle.setDateBirthday(miembro.getFechaNacimiento());
            detalle.setUserId(miembro.getUserId());
            detalle.setReserve(reserve);


            double montoBase = obtenerTarifa(dto.getVueltasOTiempo());


            double descuentoCantidad = obtenerDescuentoPorCantidad(dto.getMiembros().size());


            double descuentoFrecuencia = obtenerDescuentoPorCategoria(miembro.getRut());


            double descuentoCumple = 0;
            if (cumpleanosAplicados < maxCumpleanos &&
                    miembro.getFechaNacimiento().equals(dto.getFechaUso())) {
                descuentoCumple = 0.50;
                cumpleanosAplicados++;
            }


            double descuentoFinal = Math.max(descuentoCumple,
                    Math.max(descuentoFrecuencia, descuentoCantidad));


            double montoConDescuento = montoBase * (1 - descuentoFinal);

            detalle.setDiscount(descuentoFinal);
            detalle.setMontoFinal(montoConDescuento);

            detalles.add(detalle);
            montoFinalTotal += montoConDescuento;

            System.out.println("Monto base: " + montoBase);
            System.out.println("Descuento cantidad: " + descuentoCantidad);
            System.out.println("Descuento frecuencia: " + descuentoFrecuencia);
            System.out.println("Descuento cumple: " + descuentoCumple);
            System.out.println("Descuento final aplicado: " + descuentoFinal);
            System.out.println("Monto final con descuento: " + montoConDescuento);
        }



        reserve.setMontoFinal(montoFinalTotal);
        reserve.setDetalles(detalles);



        return reserveRepository.save(reserve);
    }


    public int obtenerTarifa(int duracionMinutos) {
        String url = "http://localhost:8001/api/tarifas/duracion/" + duracionMinutos;
        TariffDTO tarifa = restTemplate.getForObject(url, TariffDTO.class);
        if (tarifa == null) {
            throw new RuntimeException("No se encontró una tarifa para la duración: " + duracionMinutos);
        }
        return tarifa.getPrecio();
    }

    public double obtenerDescuentoPorCantidad(int cantidadPersonas) {
        String url = "http://localhost:8002/api/descuentoscantidad/" + cantidadPersonas;
        DescuentoCantidadResponse response = restTemplate.getForObject(url, DescuentoCantidadResponse.class);
        Double descuento = response.getDescuento();

        return descuento;

    }


    public double obtenerDescuentoPorCategoria(String rut) {
        System.out.println(rut);
        int visitas = userService.getUserByRut(rut).getNumberVisits();
        String url = "http://localhost:8003/api/frecuencia/" + visitas;
        DiscountFrecuenciaEntity response = restTemplate.getForObject(url, DiscountFrecuenciaEntity.class);

        if (response == null || response.getDescuento() == null) {
            throw new RuntimeException("No se pudo obtener el descuento para el usuario con ID: " + userService.getUserByRut(rut).getId());
        }

        return response.getDescuento();
    }


    public int calcularMaxCumpleanos(int cantidadPersonas) {
        if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            return 2;
        } else if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            return 1;
        } else {
            return 0;
        }
    }




}