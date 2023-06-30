package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.dto.PacienteDto;
import com.backend.clinicaodontologica.dto.TurnoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.TurnoRepository;
import com.backend.clinicaodontologica.service.ITurnoService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    @Override
    public List<TurnoDto> listarTurnos() {
        List<Turno> turnos = turnoRepository.findAll();

        List<TurnoDto> turnoDtos = turnos.stream().map(TurnoDto::fromTurno).toList();

        LOGGER.info("Lista de todos los turnos: {}", JsonPrinter.toString(turnoDtos));
        return turnoDtos;
    }

    @Override
    public TurnoDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoDto turnoDto = null;

        if (turnoBuscado != null) {
            turnoDto = TurnoDto.fromTurno(turnoBuscado);

            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoDto));
        } else {
            LOGGER.info("El id no se encuentra registrado en la base de datos");
        }

        return turnoDto;
    }

    @Override
    public TurnoDto guardarTurno(Turno turno) throws BadRequestException{
        TurnoDto turnoDto = null;
        PacienteDto paciente = null;
        OdontologoDto odontologo = null;

        if(turno.getPaciente() != null) {
            paciente = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
        }
        if (turno.getOdontologo() != null){
            odontologo = odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId()); // capturamos el odontologo
        }

        Paciente pacienteTurno = turno.getPaciente();
        Odontologo odontologoTurno = turno.getOdontologo();

        if (paciente == null || odontologo == null) {
            if (paciente == null && odontologo == null) {
                LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                throw new BadRequestException("El paciente y el odontologo no se encuentran en nuestra base de datos");
            } else if (paciente == null) {
                LOGGER.error("El paciente no se encuentra en nuestra base de datos");
                throw new BadRequestException("El paciente no se encuentra en nuestra base de datos");
            } else {
                LOGGER.error("El odontologo no se encuentra en nuestra base de datos");
                throw new BadRequestException("El odontologo no se encuentra en nuestra base de datos");
            }
        } else if (!pacienteService.compararPacientes(pacienteTurno, paciente) || !odontologoService.compararOdontologos(odontologoTurno, odontologo)){
            if (!pacienteService.compararPacientes(pacienteTurno, paciente) && !odontologoService.compararOdontologos(odontologoTurno, odontologo)) {
                LOGGER.error("Alguno de los datos del odontologo y del paciente no coinciden con los registrados en la base de datos");
                throw new BadRequestException("Alguno de los datos del odontologo y del paciente no coinciden con los registrados en la base de datos");
            } else if (!odontologoService.compararOdontologos(odontologoTurno, odontologo)) {
                LOGGER.error("Alguno de los datos del odontologo no coinciden con los registrados en la base de datos");
                throw new BadRequestException("Alguno de los datos del odontologo no coinciden con los registrados en la base de datos");
            } else {
                LOGGER.error("Alguno de los datos del paciente no coinciden con los registrados en la base de datos");
                throw new BadRequestException("Alguno de los datos del paciente no coinciden con los registrados en la base de datos");
            }
        }else {
            turnoDto = TurnoDto.fromTurno(turnoRepository.save(turno));
            LOGGER.info("Nuevo turno registrado con exito: {}", JsonPrinter.toString(turnoDto));
        }

        return turnoDto;
    }

    @Override
    public TurnoDto actualizarTurno(Turno turno) throws BadRequestException, ResourceNotFoundException {
        Turno turnoAActualizar = turnoRepository.findById(turno.getId()).orElse(null);
        TurnoDto turnoDtoActualizado = null;

        PacienteDto paciente = null;
        OdontologoDto odontologo = null;

        if(turno.getPaciente() != null) {
            paciente = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
        }
        if (turno.getOdontologo() != null){
            odontologo = odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId());
        }

        if (turnoAActualizar != null) {
            if (paciente == null || odontologo == null) {
                if (paciente == null && odontologo == null) {
                    LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                    throw new BadRequestException("El paciente no se encuentra en nuestra base de datos");
                } else if (paciente == null) {
                    LOGGER.error("El paciente no se encuentra en nuestra base de datos");
                    throw new BadRequestException("El paciente no se encuentra en nuestra base de datos");
                } else {
                    LOGGER.error("El odontologo no se encuentra en nuestra base de datos");
                    throw new BadRequestException("El odontologo no se encuentra en nuestra base de datos");
                }

            } else {
                turnoAActualizar = turno;

                turnoRepository.save(turnoAActualizar);

                turnoDtoActualizado = TurnoDto.fromTurno(turnoAActualizar);

                LOGGER.warn("Turno actualizado con exito: {}", JsonPrinter.toString(turnoDtoActualizado));
            }
        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
            throw new ResourceNotFoundException("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
        }

        return turnoDtoActualizado;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (buscarTurnoPorId(id) != null) {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else {
            LOGGER.error("No se ha encontrado el turno con id " + id);
            throw new ResourceNotFoundException("No se ha encontrado el turno con id " + id);
        }
    }


}
