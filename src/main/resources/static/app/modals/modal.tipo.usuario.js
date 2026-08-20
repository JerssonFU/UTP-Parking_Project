
$(function() {

	$(document).ready( () => {

		inicializarTipoUsuario();	

	});

});

function inicializarTipoUsuario(){	

	$('[data-toggle="tooltip"]').tooltip();

	//GUARDAR DATOS DEL FORMULARIO

	$("#btnGuardarTipoUsuario").click( (event) => {

		event.preventDefault();

		this.guardarDatosTipoUsuario();

	});	

}

function validarFormulario(){		

	if ($("#nTusuDescripcion").val() == null || $("#nTusuDescripcion").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar descripcion");	
		$('#nTusuDescripcion').focus();
		return false;			
	}

	if ($("#fTusuEstado").val() == null || $("#fTusuEstado").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione un estado");	
		$('#fTusuEstado').focus();
		return false;			
	}	
	
	if ($("#cTusuDetalle").val() == null || $("#cTusuDetalle").val() == ''){		
		mensaje("warning","Advertencia","Por favor el alias");	
		$('#cTusuDetalle').focus();
		return false;			
	}	
	
	

	return true;

}


function guardarDatosTipoUsuario() {

	if (validarFormulario()) {	

		var baseURL = $("meta[name='ctx']").attr("content");		

		var url = baseURL + '/tipousuario/guardarDatosTipoUsuario';

		var form = $('#frmTipoUsuario')[0];

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
					recargarTblTipoUsuarios();//recargamos la lista
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






