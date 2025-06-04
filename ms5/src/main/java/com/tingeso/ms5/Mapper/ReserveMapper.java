package com.tingeso.ms5.Mapper;

import com.tingeso.ms5.DTOs.MemberDTO;
import com.tingeso.ms5.DTOs.ReserveRequestDTO;
import com.tingeso.ms5.Entity.ReserveEntity;
import com.tingeso.ms5.Entity.ReserveDetailsEntity;

import java.util.ArrayList;
import java.util.List;

public class ReserveMapper {

    public static ReserveRequestDTO toRequestDTO(ReserveEntity reserva) {
        ReserveRequestDTO dto = new ReserveRequestDTO();
        dto.setFechaUso(reserva.getFechaUso());
        dto.setHoraInicio(reserva.getHoraInicio());
        dto.setHoraFin(reserva.getHoraFin());
        dto.setClienteId(reserva.getClienteId());
        dto.setVueltasOTiempo(reserva.getVueltasOTiempo());

        List<MemberDTO> miembros = new ArrayList<>();
        for (ReserveDetailsEntity detalle : reserva.getDetalles()) {
            MemberDTO miembro = new MemberDTO();
            miembro.setNombre(detalle.getMemberName());
            miembro.setFechaNacimiento(detalle.getDateBirthday());
            miembro.setUserId(detalle.getUserId());
            miembros.add(miembro);
        }
        dto.setMiembros(miembros);

        return dto;
    }
}
