package com.backend.clinicaodontologica.controller;

import com.backend.clinicaodontologica.dto.OdontologoDto;
import com.backend.clinicaodontologica.dto.TurnoDto;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.service.ITurnoService;
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
@RequestMapping("/turnos")
@CrossOrigin
public class TurnoController {
    private ITurnoService turnoService;

    @Autowired
    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }


    //GET ALL
    @Operation(summary = "Listado de todos los turnos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de turnos obtenido correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping()
    public List<TurnoDto> listarTurnos(){
        return turnoService.listarTurnos();
    }


    //GET
    @Operation(summary = "Búsqueda de un turno por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno obtenido correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<TurnoDto> buscarTurnoPorId(@PathVariable Long id) {
        TurnoDto turnoDto = turnoService.buscarTurnoPorId(id);
        if (turnoDto != null){
            return new ResponseEntity<>(turnoDto, null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }


    //POST
    @Operation(summary = "Registro de un nuevo turno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno guardado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/registrar")
    public ResponseEntity<TurnoDto> guardarTurno(@Valid @RequestBody Turno turno) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.guardarTurno(turno), null, HttpStatus.CREATED);
    }


    //PUT
    @Operation(summary = "Actualización de un turno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OdontologoDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PutMapping("/actualizar")
    public ResponseEntity<TurnoDto> actualizarTurno (@Valid @RequestBody Turno turno) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.actualizarTurno(turno), null, HttpStatus.OK);
    }


    //DELETE
    @Operation(summary = "Eliminación de un turno por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Turno eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Id inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarTurno (@PathVariable Long id) throws ResourceNotFoundException {
        turnoService.eliminarTurno(id);
        return ResponseEntity.ok("Turno eliminado");
    }
}
