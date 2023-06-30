package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.DomicilioDto;
import com.backend.clinicaodontologica.dto.PacienteDto;
import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.PacienteRepository;
import com.backend.clinicaodontologica.service.IPacienteService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PacienteService implements IPacienteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);
    private PacienteRepository pacienteRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, ObjectMapper objectMapper) {
        this.pacienteRepository = pacienteRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<PacienteDto> listarPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();

        List<PacienteDto> pacienteDtos = pacientes.stream()
                .map(paciente -> {
                    Domicilio dom = paciente.getDomicilio();
                    DomicilioDto domicilioDto = objectMapper.convertValue(dom, DomicilioDto.class);
                    return new PacienteDto(paciente.getId(), paciente.getNombre(), paciente.getApellido(), paciente.getDni(), paciente.getFechaIngreso(), domicilioDto);
                }).toList();

        LOGGER.info("Lista de todos los pacientes: {}", JsonPrinter.toString(pacienteDtos));
        return pacienteDtos;
    }

    @Override
    public PacienteDto buscarPacientePorId(Long id){
        Paciente pacienteBuscado = pacienteRepository.findById(id).orElse(null);
        PacienteDto pacienteDto = null;
        if (pacienteBuscado != null) {

            DomicilioDto domicilioDto = objectMapper.convertValue(pacienteBuscado.getDomicilio(), DomicilioDto.class);
            pacienteDto = objectMapper.convertValue(pacienteBuscado, PacienteDto.class);
            pacienteDto.setDomicilio(domicilioDto);

            LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(pacienteDto));

        } else {
            LOGGER.info("El paciente con el id " + id + " no se encuentra registrado en la base de datos");
        }

        return pacienteDto;
    }

    @Override
    public PacienteDto guardarPaciente(Paciente paciente) {
        Paciente pacienteNuevo = pacienteRepository.save(paciente);

        DomicilioDto domicilioDto = objectMapper.convertValue(pacienteNuevo.getDomicilio(), DomicilioDto.class);
        PacienteDto pacienteDtoNuevo = objectMapper.convertValue(pacienteNuevo, PacienteDto.class);
        pacienteDtoNuevo.setDomicilio(domicilioDto);

        LOGGER.info("Nuevo paciente registrado con exito: {}", JsonPrinter.toString(pacienteDtoNuevo));

        return pacienteDtoNuevo;
    }

    @Override
    public PacienteDto actualizarPaciente(Paciente paciente) throws ResourceNotFoundException {
        Paciente pacienteAActualizar = pacienteRepository.findById(paciente.getId()).orElse(null);
        PacienteDto pacienteDtoActualizado = null;

        if (pacienteAActualizar != null) {
            pacienteAActualizar = paciente;
            pacienteRepository.save(pacienteAActualizar);

            DomicilioDto domicilioDto = objectMapper.convertValue(pacienteAActualizar.getDomicilio(), DomicilioDto.class);
            pacienteDtoActualizado = objectMapper.convertValue(pacienteAActualizar, PacienteDto.class);
            pacienteDtoActualizado.setDomicilio(domicilioDto);

            LOGGER.warn("Paciente actualizado con exito: {}", JsonPrinter.toString(pacienteDtoActualizado));
        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el paciente no se encuentra registrado");
            throw new ResourceNotFoundException("No fue posible actualizar los datos ya que el paciente no se encuentra registrado");
        }
        return pacienteDtoActualizado;
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException {
        if (buscarPacientePorId(id) != null) {
            pacienteRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el paciente con id {}", id);
        } else {
            LOGGER.error("No se ha encontrado el paciente con id {}" + id);
            throw new ResourceNotFoundException("No se ha encontrado el paciente con id " + id); // throw quivalente a un return
        }
    }

    public boolean compararPacientes(Paciente paciente, PacienteDto pacienteDto) {
        Paciente paciente2 = objectMapper.convertValue(pacienteDto, Paciente.class);
        return (paciente.getId().equals(paciente2.getId()) &&
                paciente.getNombre().equals(paciente2.getNombre()) &&
                paciente.getApellido().equals(paciente2.getApellido()) &&
                paciente.getDni().equals(paciente2.getDni()) &&
                paciente.getFechaIngreso().equals(paciente2.getFechaIngreso()) &&
                paciente.getDomicilio().getId().equals(paciente2.getDomicilio().getId()) &&
                paciente.getDomicilio().getCalle().equals(paciente2.getDomicilio().getCalle()) &&
                Objects.equals(paciente.getDomicilio().getNumero(), paciente2.getDomicilio().getNumero()) &&
                paciente.getDomicilio().getLocalidad().equals(paciente2.getDomicilio().getLocalidad()) &&
                paciente.getDomicilio().getProvincia().equals(paciente2.getDomicilio().getProvincia()));
    }
}
