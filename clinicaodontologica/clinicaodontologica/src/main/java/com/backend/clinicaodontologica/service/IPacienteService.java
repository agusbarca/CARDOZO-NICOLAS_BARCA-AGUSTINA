package com.backend.clinicaodontologica.service;

import com.backend.clinicaodontologica.dto.PacienteDto;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;

import java.util.List;

// VAN A RETORNAR DTO
public interface IPacienteService{
    List<PacienteDto> listarPacientes();
    PacienteDto buscarPacientePorId(Long id);

    PacienteDto guardarPaciente(Paciente paciente);
    PacienteDto actualizarPaciente (Paciente paciente) throws ResourceNotFoundException;
    void eliminarPaciente(Long id) throws ResourceNotFoundException;


}
