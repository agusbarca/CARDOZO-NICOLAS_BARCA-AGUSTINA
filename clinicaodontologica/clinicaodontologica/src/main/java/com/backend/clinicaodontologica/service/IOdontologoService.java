package com.backend.clinicaodontologica.service;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;

import java.util.List;


public interface IOdontologoService {
    List<OdontologoDto> listarOdontologos();
    OdontologoDto buscarOdontologoPorId(Long id);

    OdontologoDto guardarOdontolgo(Odontologo odontologo);
    OdontologoDto actualizarOdontologo(Odontologo odontologo) throws ResourceNotFoundException;
    void eliminarOdontologo(Long id) throws ResourceNotFoundException;
}
