package com.backend.clinicaodontologica.controller;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.service.impl.OdontologoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/odontologos")
@CrossOrigin
public class OdontologoController {
    OdontologoService odontologoService;

    @Autowired
    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }


    //GET ALL
    @Operation(summary = "Listado de todos los odontologos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de odontologos obtenido correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping()
    public List<OdontologoDto> listarOdontologos(){
        return odontologoService.listarOdontologos();
    }


    //GET
    @Operation(summary = "Búsqueda de un odontologo por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo obtenido correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDto> buscarOdontologoPorId(@PathVariable Long id) {
        OdontologoDto odontologoDto = odontologoService.buscarOdontologoPorId(id);
        if (odontologoDto != null){
            return new ResponseEntity<>(odontologoDto, null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }

    //POST
    @Operation(summary = "Registro de un nuevo odontologo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Odontologo guardado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/registrar")
    public ResponseEntity<OdontologoDto> guardarOdontologo(@Valid @RequestBody Odontologo odontologo){
        return new ResponseEntity<>(odontologoService.guardarOdontolgo(odontologo), null, HttpStatus.CREATED);
    }

    //PUT
    @Operation(summary = "Actualización de un odontologo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Odontologo actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PutMapping("/actualizar")
    public ResponseEntity<OdontologoDto> actualizarOdontologo(@Valid @RequestBody Odontologo odontologo) throws ResourceNotFoundException {
        return new ResponseEntity<>(odontologoService.actualizarOdontologo(odontologo), null, HttpStatus.OK);
    }


    //DELETE
    @Operation(summary = "Eliminación de un odontologo por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Odontologo eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Id inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Odontologo no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(id);
        return ResponseEntity.ok("Odontologo eliminado");
    }
}
