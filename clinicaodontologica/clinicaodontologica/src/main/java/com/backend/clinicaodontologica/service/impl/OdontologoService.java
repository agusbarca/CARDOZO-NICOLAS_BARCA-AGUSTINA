package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.OdontologoRepository;
import com.backend.clinicaodontologica.service.IOdontologoService;
import com.backend.clinicaodontologica.utils.JsonPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);

    private OdontologoRepository odontologoRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public OdontologoService(OdontologoRepository odontologoRepository, ObjectMapper objectMapper) {
        this.odontologoRepository = odontologoRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<OdontologoDto> listarOdontologos() {
        List<OdontologoDto> odontologos = odontologoRepository.findAll().stream()
                .map(o -> objectMapper.convertValue(o, OdontologoDto.class)).toList();

        LOGGER.info("Lista de todos los odontologos: {}", JsonPrinter.toString(odontologos));
        return odontologos;
    }

    @Override
    public OdontologoDto buscarOdontologoPorId(Long id){
        Odontologo odontologoBuscado = odontologoRepository.findById(id).orElse(null);
        OdontologoDto odontologoDto = null;
        if (odontologoBuscado != null){
            odontologoDto = objectMapper.convertValue(odontologoBuscado, OdontologoDto.class);

            LOGGER.info("Odontologo encontrado: {}",  JsonPrinter.toString(odontologoDto));

        } else {
            LOGGER.info("El odontologo con el id " + id + " no se encuentra registrado en la base de datos");

        }

        return odontologoDto;

    }

    @Override
    public OdontologoDto guardarOdontolgo(Odontologo odontologo) {
        Odontologo odontologoNuevo = odontologoRepository.save(odontologo);

        OdontologoDto odontologoDtoNuevo = objectMapper.convertValue(odontologoNuevo, OdontologoDto.class);

        LOGGER.info("Nuevo odontologo registrado con exito: {}", JsonPrinter.toString(odontologoDtoNuevo));

        return odontologoDtoNuevo;
    }

    @Override
    public OdontologoDto actualizarOdontologo(Odontologo odontologo) throws ResourceNotFoundException {
        Odontologo odontologoAActualizar = odontologoRepository.findById(odontologo.getId()).orElse(null);

        OdontologoDto odontologoDtoActualizado = null;

        if (odontologoAActualizar != null) {
            odontologoAActualizar = odontologo;
            odontologoRepository.save(odontologoAActualizar);

            odontologoDtoActualizado = objectMapper.convertValue(odontologoAActualizar, OdontologoDto.class);

            LOGGER.warn("Odontologo actualizado con exito: {}", JsonPrinter.toString(odontologoDtoActualizado));
        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el odontologo no se encuentra registrado");
            throw new ResourceNotFoundException("No fue posible actualizar los datos ya que el odontologo no se encuentra registrado");
        }
        return odontologoDtoActualizado;
    }

    @Override
    public void eliminarOdontologo( Long id ) throws ResourceNotFoundException {
        if (buscarOdontologoPorId(id) != null){
            odontologoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el odontologo con id {}", id);
        }else {
            LOGGER.error("No se ha encontrado el odontologo con id " + id);
            throw new ResourceNotFoundException("No se ha encontrado el odontologo con id " + id);
        }

    }
}
