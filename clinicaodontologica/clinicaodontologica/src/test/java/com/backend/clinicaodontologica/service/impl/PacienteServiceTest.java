package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.PacienteDto;
import com.backend.clinicaodontologica.entity.Domicilio;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;



@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacienteServiceTest {
    @Autowired
    private PacienteService pacienteService;

    @Test
    @Order(1)
    void deberiaRegistrarUnPaciente(){
        Paciente paciente = new Paciente("Perejil", "Merito", "23653987",LocalDate.of(2023,07,25),(new Domicilio("Montiel",23,"Ituzaingo","Provincia de Buenos Aires")));
        PacienteDto pacienteDto = pacienteService.guardarPaciente(paciente);

        Assertions.assertNotNull(pacienteDto);
        Assertions.assertNotNull(pacienteDto.getId());
    }

    @Test
    @Order(2)
    void deberiaEncontrarElPacienteConId1() {
        PacienteDto pacienteEncontrado = pacienteService.buscarPacientePorId(1L);

        Assertions.assertEquals("23653987", pacienteEncontrado.getDni());
        Assertions.assertNotEquals(pacienteEncontrado, null);
    }

    @Test
    @Order(3)
    void deberiaEliminarElPacienteConId1() throws ResourceNotFoundException {
        pacienteService.eliminarPaciente(1L);

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> pacienteService.eliminarPaciente(1L));
    }

    @Test
    @Order(4)
    void deberiaRetornarUnaListaDePacientesVacia() {
        List<PacienteDto> pacientes = pacienteService.listarPacientes();

        Assertions.assertTrue(pacientes.isEmpty());
    }

    @Test
    void deberiaLanzarResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> pacienteService.eliminarPaciente(10L));
    }


}