  // Función para cerrar el modal de edición
  function closeModal() {
    $("#modal-edicion").removeClass("open");
  }

$(document).ready(function() {
    var pacientes = [];
    var odontologos = [];
    obtenerTurnos(); // Obtener los turnos existentes al cargar la página
    obtenerPacientes();
    obtenerOdontologos();
  
    // Manejar el evento de envío del formulario de creación
    $("#form-crear").submit(function(event) {
      event.preventDefault();
      crearTurno();
    });
  
    // Manejar el evento de envío del formulario de edición
    $("#form-editar").submit(function(event) {
      event.preventDefault();
      actualizarTurno();
    });
  });
  
  // Obtener la base de la URL
  var baseUrl = "http://localhost:8080/turnos";
  
 
  // Función para abrir el modal de edición
  function openModal(turno) {
    $("#edit-id").val(turno.id);
    $("#edit-paciente").val(turno.paciente);
    $("#edit-odontologo").val(turno.odontologo);
    $("#edit-fechaHora").val(turno.fechaYHora);
    $("#modal-edicion").addClass("open");
  }

    function obtenerPacientes() {
        var pacienteSelect = document.getElementById('paciente');

        // Hacer la solicitud HTTP al endpoint de pacientes
        fetch('http://localhost:8080/pacientes')
        .then(response => response.json())
        .then(data => {
            // Procesar los datos recibidos y agregar opciones al select de pacientes
            data.forEach(paciente => {
                var option = document.createElement('option');
                option.value = paciente.id;
                option.textContent = paciente.nombre + ' ' + paciente.apellido;
                pacienteSelect.appendChild(option);
            });
            pacientes = data;
        })
        .catch(error => {
            console.log(error);
        });
    }

    function obtenerOdontologos() {
        var odontologoSelect = document.getElementById('odontologo');

        // Hacer la solicitud HTTP al endpoint de odontólogos
        fetch('http://localhost:8080/odontologos')
          .then(response => response.json())
          .then(data => {
            // Procesar los datos recibidos y agregar opciones al select de odontólogos
            data.forEach(odontologo => {
              var option = document.createElement('option');
              option.value = odontologo.id;
              option.textContent = odontologo.nombre + ' ' + odontologo.apellido;
              odontologoSelect.appendChild(option);
            });
            odontologos = data;
          })
          .catch(error => {
            console.log(error);
          });
    }

  
  // Función para obtener la lista de turnos existentes
  function obtenerTurnos() {
    $.get(baseUrl, function(data) {
      mostrarTurnos(data);
    });
  }
  
  // Función para mostrar los turnos en la tabla
  function mostrarTurnos(turnos) {
    var tablaTurnos = $("#tabla-turnos tbody");
    tablaTurnos.empty();
  
    turnos.forEach(function(turno) {
      var fila =
        "<tr>" +
        "<td class='mdl-data-table__cell--non-numeric'>" +
        turno.id +
        "</td>" +
        "<td class='mdl-data-table__cell--non-numeric'>" +
        turno.paciente +
        "</td>" +
        "<td class='mdl-data-table__cell--non-numeric'>" +
        turno.odontologo +
        "</td>" +
        "<td class='mdl-data-table__cell--non-numeric'>" +
        turno.fechaYHora +
        "</td>" +
        "<td class='mdl-data-table__cell--non-numeric'>" +
        "<button class='mdl-button mdl-js-button mdl-button--raised mdl-button--colored' onclick='eliminarTurno(" +
        turno.id +
        ")'>Eliminar</button>" +
        "</td>" +
        "</tr>";
  
      tablaTurnos.append(fila);
    });
  }
  
  // Función para crear un nuevo turno
  function crearTurno() {
    var paciente = $("#paciente").val();
    var odontologo = $("#odontologo").val();
    var fechaHora = $("#fechaHora").val();
    
    var odontologoSeleccionado = odontologos.find(item => item.id === Number(odontologo));
    var pacienteSeleccionado = pacientes.find(item => item.id === Number(paciente));

    var nuevoTurno = {
      paciente: pacienteSeleccionado,
      odontologo: odontologoSeleccionado,
      fechaYHora: fechaHora
    };
  
    $.ajax({
      url: baseUrl + "/registrar",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(nuevoTurno),
      success: function(data) {
        obtenerTurnos(); // Actualizar la lista de turnos
        $("#form-crear")[0].reset(); // Limpiar el formulario de creación
      }
    });
  }
  
  
// Función para actualizar un turno existente
function actualizarTurno() {
    var id = $("#edit-id").val();
    var paciente = $("#edit-paciente").val();
    var odontologo = $("#edit-odontologo").val();
    var fechaHora = $("#edit-fechaHora").val();
  
    var turnoActualizado = {
      id: id,
      paciente: paciente,
      odontologo: odontologo,
      fechaYHora: fechaHora
    };
  
    $.ajax({
      url: baseUrl + "/actualizar",
      type: "PUT",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(turnoActualizado),
      success: function(data) {
        obtenerTurnos(); // Actualizar la lista de turnos
        closeModal(); // Cerrar el modal de edición
      }
    });
  }
  
  
  // Función para eliminar un turno
  function eliminarTurno(id) {
    $.ajax({
      url: baseUrl + "/eliminar/" + id,
      type: "DELETE",
      success: function(data) {
        obtenerTurnos(); // Actualizar la lista de turnos
      }
    });
  }
  