package com.backend.clinicaodontologica.service;

import com.backend.clinicaodontologica.dto.TurnoDto;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ITurnoService {
    List<TurnoDto> listarTurnos();
    TurnoDto buscarTurnoPorId(Long id); // throws ResourceNotFoundException no porque pisa el de eliminarTruno

    TurnoDto guardarTurno(Turno turno) throws BadRequestException;
    TurnoDto actualizarTurno (Turno turno) throws BadRequestException, ResourceNotFoundException;
    void eliminarTurno(Long id) throws ResourceNotFoundException;
}
