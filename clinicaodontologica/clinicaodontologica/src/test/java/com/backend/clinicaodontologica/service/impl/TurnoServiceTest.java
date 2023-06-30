package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.TurnoDto;
import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TurnoServiceTest {
    private TurnoService turnoService;
    private PacienteService pacienteService;
    private OdontologoService odontologoService;
    private static Turno turno;
    private static Paciente paciente;
    private static Odontologo odontologo;

    @Autowired
    TurnoServiceTest(TurnoService turnoService, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoService = turnoService;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }


    @BeforeAll
    static void prepararPacienteOdontologoYTurno() {
        paciente = new Paciente("Lola", "Perez", "538723423", LocalDate.of(2023, 7, 29),  new Domicilio("Lacosta", 756, "Burlando", "Bueno Aires"));

        odontologo = new Odontologo("YU-56735346", "Juan", "Carlos");

        turno = new Turno(paciente, odontologo, LocalDateTime.of(2023, 7, 10, 10, 30));
    }

    @Test
    @Order(1)
    void deberiaInsertarUnTurnoUsandoElPacienteYOdontologoExistentesEnLaBaseDeDatos() throws BadRequestException {

        pacienteService.guardarPaciente(paciente);
        odontologoService.guardarOdontolgo(odontologo);

        TurnoDto turnoDto = turnoService.guardarTurno(turno);

        Assertions.assertEquals(1L, turnoDto.getId());
        Assertions.assertEquals(turnoDto.getFechaYHora(), turnoDto.getFechaYHora());
    }

    @Test
    @Order(2)
    void cuandoSeIntentaRegistrarUnTurnoSinPaciente_deberiaLanzarLaExepecionBadRequest() throws BadRequestException {
        Turno turnoAInsertar = new Turno(null, odontologo, LocalDateTime.of(2023,9,18,17,45));

        Assertions.assertThrows(BadRequestException.class, () -> turnoService.guardarTurno(turnoAInsertar));
    }

    @Test
    @Order(3)
    void deberiaEncontrarElTurnoConId1(){
        TurnoDto turnoEncontrado = turnoService.buscarTurnoPorId(1L);

        Assertions.assertEquals(paciente, turno.getPaciente());
        Assertions.assertNotEquals(turnoEncontrado, null);
    }

    @Test
    @Order(4)
    void deberiaListarUnSoloTurno(){
        List<TurnoDto> turnos = turnoService.listarTurnos();

        Assertions.assertFalse(turnos.isEmpty());
        Assertions.assertEquals(1, turnos.size());
    }

    @Test
    @Order(5)
    void deberiaEliminarElTurnoConId1() throws ResourceNotFoundException {
        turnoService.eliminarTurno(1L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> turnoService.eliminarTurno(1L));
    }

    @Test
    @Order(6)
    void deberiaRetornarUnaListaDeTurnosVacia(){
        List<TurnoDto> turnos = turnoService.listarTurnos();
        Assertions.assertTrue(turnos.isEmpty());
    }

}

