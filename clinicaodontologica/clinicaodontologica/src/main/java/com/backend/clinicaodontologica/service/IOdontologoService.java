package com.backend.clinicaodontologica.service;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;

import java.util.List;


// VAN A RETORNAR DTO
public interface IOdontologoService {
    List<OdontologoDto> listarOdontologos();
    OdontologoDto buscarOdontologoPorId(Long id); // throws ResourceNotFoundException;

    OdontologoDto guardarOdontolgo(Odontologo odontologo); // throws BadRequestException;
    OdontologoDto actualizarOdontologo(Odontologo odontologo) throws ResourceNotFoundException;
    void eliminarOdontologo(Long id) throws ResourceNotFoundException;
}
