
$(function() {

	$(document).ready( () => {

		inicializarUsuarioSistema();	

	});

});

function inicializarUsuarioSistema(){	

	$('[data-toggle="tooltip"]').tooltip();

	//GUARDAR DATOS DEL FORMULARIO

	$("#btnGuardarUsuarioSis").click( (event) => {

		event.preventDefault();

		this.guardarDatosUsuarioSistema();

	});	

}

function validarFormulario(){		

	if ($("#codTipoUsuario").val() == null || $("#codTipoUsuario").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione un tipo de usuario");	
		$('#codTipoUsuario').focus();
		return false;			
	}

	if ($("#codSistema").val() == null || $("#codSistema").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione un sistema");	
		$('#codSistema').focus();
		return false;			
	}	

	return true;

}


function guardarDatosUsuarioSistema() {

	if (validarFormulario()) {	

		var baseURL = $("meta[name='ctx']").attr("content");		

		var url = baseURL + '/usuario/guardarDatosUsuarioSistema';

		var form = $('#frmUsuarioSis')[0];

		var data = new FormData(form);

		$.ajax({	
			type: "POST",
			enctype: 'multipart/form-data',
			url: url,
			data: data,
			processData: false, //prevent jQuery from automatically transforming the data into a query string
			contentType: false,
			cache: false,
			beforeSend : (request) => {
				this.desbloquearPantalla();//desbloqueamos la pantalla	
			},
			success: (data) => {

				this.desbloquearPantalla();//desbloqueamos la pantalla		

				if(data.status=='Done'){
					mensaje("success","Success",data.data);						
				}else{	
					mensaje("warning","Advertencia", data.data);						
				}						
			},
			error: (e) => {		
				this.desbloquearPantalla();//desbloqueamos la pantalla		
				mensaje("error","Error", 'Ocurrió un problema, vuelva a intentarlo y si el problema persiste contáctese con el Administrador.');				
			}
		});		
	}
}






