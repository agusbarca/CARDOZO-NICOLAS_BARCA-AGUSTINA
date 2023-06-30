
function closeModal() {
    $('#modal-container').css('display', 'none');
    $('#modal-overlay').css('display', 'none');
  }

$(document).ready(function() {
    var baseUrl = "http://localhost:8080/odontologos";
    function openModal() {
        $('#modal-container').css('display', 'block');
        $('#modal-overlay').css('display', 'block');
      }
    
    // Función para obtener la lista de odontólogos
    function obtenerOdontologos() {
      $.ajax({
        url: baseUrl,
        method: 'GET',
        success: function(data) {
          // Limpiar la tabla de odontólogos
          $('#tabla-odontologos tbody').empty();
          
          // Agregar cada odontólogo a la tabla
          data.forEach(function(odontologo) {
            var row = '<tr>';
            row += '<td>' + odontologo.id + '</td>';
            row += '<td>' + odontologo.matricula + '</td>';
            row += '<td>' + odontologo.nombre + '</td>';
            row += '<td>' + odontologo.apellido + '</td>';
            row += '<td><button class="btn-editar" data-id="' + odontologo.id + '">Editar</button></td>';
            row += '<td><button class="btn-eliminar" data-id="' + odontologo.id + '">Eliminar</button></td>';
            row += '</tr>';
            
            $('#tabla-odontologos tbody').append(row);
          });
        },
        error: function(error) {
          console.log(error);
        }
      });
    }
    
    // Cargar la lista de odontólogos al cargar la página
    obtenerOdontologos();
    
    // Evento para crear un odontólogo
    $('#form-crear').submit(function(event) {
      event.preventDefault();
      
      var matricula = $('#matricula').val();
      var nombre = $('#nombre').val();
      var apellido = $('#apellido').val();
      
      var data = {
        matricula: matricula,
        nombre: nombre,
        apellido: apellido
      };
    
      $.ajax({
        url: baseUrl + "/registrar",
        method: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(data) {
          obtenerOdontologos();
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
          $('#editar-id').val(data.id);
          $('#editar-matricula').val(data.matricula);
          $('#editar-nombre').val(data.nombre);
          $('#editar-apellido').val(data.apellido);
          
          $('#editar-id').parent().addClass("is-dirty");
          $('#editar-matricula').parent().addClass("is-dirty");
          $('#editar-nombre').parent().addClass("is-dirty");
          $('#editar-apellido').parent().addClass("is-dirty");

          openModal();
        },
        error: function(error) {
          console.log(error);
        }
      });
    });

    
      // Evento para actualizar un odontólogo
  $('#btn-editar').click(function() {
    var id = $('#editar-id').val();
    var matricula = $('#editar-matricula').val();
    var nombre = $('#editar-nombre').val();
    var apellido = $('#editar-apellido').val();
    
    var data = {
      id: id,  
      matricula: matricula,
      nombre: nombre,
      apellido: apellido
    };
    
    $.ajax({
      url: baseUrl + '/actualizar/',
      method: 'PUT',
      data: JSON.stringify(data),
      contentType: 'application/json',
      success: function(data) {
        obtenerOdontologos();
        closeModal();
      },
      error: function(error) {
        console.log(error);
      }
    });
  });

    // Evento para eliminar un odontologo
    $(document).on('click', '.btn-eliminar', function() {
    var id = $(this).data('id');
    
    $.ajax({
        url: baseUrl + '/eliminar/' + id,
        method: 'DELETE',
        success: function(data) {
        obtenerOdontologos();
        },
        error: function(error) {
        console.log(error);
        }
    });
    });

});
