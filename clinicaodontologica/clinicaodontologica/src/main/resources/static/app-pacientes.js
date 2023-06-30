function closeModal() {
    $('#modal-container').css('display', 'none');
    $('#modal-overlay').css('display', 'none');
  }

    $(document).ready(function() {

    var baseUrl = "http://localhost:8080/pacientes";
    function openModal() {
        $('#modal-container').css('display', 'block');
        $('#modal-overlay').css('display', 'block');
      }
    

    // Función para obtener la lista de pacientes
    function obtenerPacientes() {
      $.ajax({  
        url: baseUrl,
        method: 'GET',
        success: function(data) {
          // Limpiar la tabla de pacientes
          $('#tabla-pacientes tbody').empty();
          
          // Agregar cada paciente a la tabla
          data.forEach(function(paciente) {
            var row = '<tr>';
            row += '<td>' + paciente.id + '</td>';
            row += '<td>' + paciente.nombre + '</td>';
            row += '<td>' + paciente.apellido + '</td>';
            row += '<td>' + paciente.dni + '</td>';
            row += '<td>' + paciente.fechaIngreso + '</td>';
            row += '<td><button class="btn-editar" data-id="' + paciente.id + '">Editar</button></td>';
            row += '<td><button class="btn-eliminar" data-id="' + paciente.id + '">Eliminar</button></td>';
            row += '</tr>';
            
            $('#tabla-pacientes tbody').append(row);
          });
        },
        error: function(error) {
          console.log(error);
        }
      });
    }
    
    // Cargar la lista de pacientes al cargar la página
    obtenerPacientes();
    
    // Evento para crear un paciente
    $('#form-crear').submit(function(event) {
      event.preventDefault();
      
      var nombre = $('#nombre').val();
      var apellido = $('#apellido').val();
      var dni = $('#dni').val();
      var fechaIngreso = $('#fecha-ingreso').val();
      var calle = $('#calle').val();
      var numero = $('#numero').val();
      var localidad = $('#localidad').val();
      var provincia = $('#provincia').val();
    
      var domicilio = {
        calle: calle,
        numero: numero,
        localidad: localidad,
        provincia: provincia
      };
    
      var data = {
        nombre: nombre,
        apellido: apellido,
        dni: dni,
        fechaIngreso: fechaIngreso,
        domicilio: domicilio
      };
    
      $.ajax({
        url: baseUrl + '/registrar',
        method: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(data) {
          obtenerPacientes();
          $('#form-crear')[0].reset();
        },
        error: function(error) {
          console.log(error);
        }
      });
    });
    
      $(document).on('click', '.btn-editar', function() {
        var id = $(this).data('id');
      
        $.ajax({
          url: baseUrl + '/' + id,
          method: 'GET',
          success: function(data) {
            $('#id-editar').val(data.id);
            $('#nombre-editar').val(data.nombre);
            $('#apellido-editar').val(data.apellido);
            $('#dni-editar').val(data.dni);
            $('#fecha-ingreso-editar').val(data.fechaIngreso);
            
            // Restablecer la validación de los campos si están vacíos
            if ($('#nombre-editar').val().trim() === '') {
                $('#nombre-editar')[0].setCustomValidity('Ingrese un nombre válido');
                $('#nombre-editar')[0].reportValidity();
            }
            if ($('#apellido-editar').val().trim() === '') {
                $('#apellido-editar')[0].setCustomValidity('Ingrese un apellido válido');
                $('#apellido-editar')[0].reportValidity();
            }
            if ($('#dni-editar').val().trim() === '') {
                $('#dni-editar')[0].setCustomValidity('Ingrese un DNI válido');
                $('#dni-editar')[0].reportValidity();
            }
            if ($('#fecha-ingreso-editar').val().trim() === '') {
                $('#fecha-ingreso-editar')[0].setCustomValidity('Ingrese una fecha de ingreso válida');
                $('#fecha-ingreso-editar')[0].reportValidity();
            }
            
            $('#id-editar').parent().addClass("is-dirty");
            $('#nombre-editar').parent().addClass("is-dirty");
            $('#apellido-editar').parent().addClass("is-dirty");
            $('#dni-editar').parent().addClass("is-dirty");
            $('#fecha-ingreso-editar').parent().addClass("is-dirty");

            openModal();
          },
          error: function(error) {
            console.log(error);
          }
        });
      });
      
      // Evento para actualizar un paciente
      $('#form-editar').submit(function(event) {
        event.preventDefault();
        
        var id = $('#id-editar').val();
        var nombre = $('#nombre-editar').val();
        var apellido = $('#apellido-editar').val();
        var dni = $('#dni-editar').val();
        var fechaIngreso = $('#fecha-ingreso-editar').val();
        
        var data = {
          id: id,  
          nombre: nombre,
          apellido: apellido,
          dni: dni,
          fechaIngreso: fechaIngreso
        };
        
        $.ajax({
          url: baseUrl + '/actualizar',
          method: 'PUT',
          data: JSON.stringify(data),
          contentType: 'application/json',
          success: function(data) {
            obtenerPacientes();
            closeModal();
          },
          error: function(error) {
            console.log(error);
          }
        });
      });
    
      // Evento para eliminar un paciente
      $(document).on('click', '.btn-eliminar', function() {
        var id = $(this).data('id');
        
        $.ajax({
          url: baseUrl + '/eliminar/' + id,
          method: 'DELETE',
          success: function(data) {
            obtenerPacientes();
          },
          error: function(error) {
            console.log(error);
          }
        });
      });
    });
    
  