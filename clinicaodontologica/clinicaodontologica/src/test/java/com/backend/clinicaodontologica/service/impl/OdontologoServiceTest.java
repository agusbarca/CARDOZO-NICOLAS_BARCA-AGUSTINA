package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;


    @Test
    @Order(1)
    void deberiaInsertarUnOdontolgo() {
        // NO VA EL OBJECT MAPER
        Odontologo odontologoAInsertar = new Odontologo("TF-3634523453453345", "Concepcion", "Suarez");
        OdontologoDto odontologoDto = odontologoService.guardarOdontolgo(odontologoAInsertar);

        //Assertions.assertEquals("Suarez", odontologoDto.getApellido());
        Assertions.assertNotNull(odontologoDto); // pq lo creo
        Assertions.assertNotNull(odontologoDto.getId()); // pq si no cumplio con la tarea no va a crear nada
    }

    @Test
    @Order(2)
    void cuandoNoSeCumplaElFormatoDeMatricula_noDeberiaInsertarUnOdontologo(){
        Odontologo odontologoAInsertar = new Odontologo("234-123234", "Concepcion", "Suarez");
        Assertions.assertThrows(ConstraintViolationException.class, () -> odontologoService.guardarOdontolgo(odontologoAInsertar));
    }

    @Test
    @Order(3)
    void deberiaListarUnSoloOdontologo(){
        List<OdontologoDto> odontologos = odontologoService.listarOdontologos();

        Assertions.assertFalse(odontologos.isEmpty());
        Assertions.assertEquals(1, odontologos.size());
    }

    //@Test
    //void deberiaEncontrarUnOdontologoPorId() {
    //    OdontologoDto odontologoEncontrado = odontologoService.buscarOdontologoPorId(1L);

    //    Assertions.assertEquals("TF-3634523453453345", odontologoService.buscarOdontologoPorId(1L).getMatricula());
    //    Assertions.assertNotEquals(odontologoEncontrado, null);
    //}

    //@Test
    //void deberiaActualizarUnOdontologo(){
        //ObjectMapper objectMapper = new ObjectMapper();
        //OdontologoDto odontologoDto = new OdontologoDto(1L, "FR-453455464566", "Perejil", "Suarez");
        //Odontologo odontologoActualizacion = objectMapper.convertValue(odontologoDto, Odontologo.class);

        //Assertions.assertEquals(odontologoActualizacion.getNombre(), odontologoService.buscarOdontologoPorId(1L).getNombre());
        //Assertions.assertNotEquals("TF-3634523453453345", odontologoService.buscarOdontologoPorId(1L).getMatricula());

        // mocks
    //}


    @Test
    @Order(4)
    void deberiaEliminarElOdontologoConId1() throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(1L);

        //Assertions.assertNotEquals("No se ha encontrado el odontologo con id 1", odontologoService.eliminarOdontologo(1L));
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> odontologoService.eliminarOdontologo(1L));
        // Assertions.assertTrue(odontologoService.listarOdontologos().isEmpty());  no pq usa otro metodo
        // Assertions.assertEquals(0, odontologoService.listarOdontologos().size()); no pq usa otro metodo
    }


    @Test
    @Order(5)
    void deberiaNoEncontrarElOdontologoConId1(){
        odontologoService.buscarOdontologoPorId(1L);
        Assertions.assertEquals(null, odontologoService.buscarOdontologoPorId(1L));
    }


    @Test
    @Order(6)
    void cuandoSePasaUnIdNoRegistrado_deberiaLanzarResourceNotFoundException() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> odontologoService.eliminarOdontologo(50L));
    }
}