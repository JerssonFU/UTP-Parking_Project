
$(function() {

	$(document).ready( () => {

		inicializarUsuario();	

	});

});

function inicializarUsuario(){	

	$('[data-toggle="tooltip"]').tooltip(); 	

	this.configFechas();	

	this.cargarProvinciasByDepto();

	this.cargarDistritosByDeptoProv();	

	this.validarCamposNumericos();

	//cotejo reniec
	$("#btnValidarPersona").click( (event) => {

		event.preventDefault();

		this.validarPersona();

	});


	//GUARDAR DATOS DEL FORMULARIO

	$("#btnGuardarUsuario").click( (event) => {

		event.preventDefault();

		this.guardarDatosUsuario();

	});	

}


function configFechas(){	

	$("#fecNacimiento").datetimepicker({
		format: 'DD/MM/YYYY',
		locale: 'es',
		icons: {
			time: "fa fa-clock-o",
			date: "fa fa-calendar",
			up: "fa fa-arrow-up",
			down: "fa fa-arrow-down"
		},
		//minDate:new Date()
	});

	$("#fecIngresoMin").datetimepicker({
		format: 'DD/MM/YYYY',
		locale: 'es',
		icons: {
			time: "fa fa-clock-o",
			date: "fa fa-calendar",
			up: "fa fa-arrow-up",
			down: "fa fa-arrow-down"
		},
		//minDate:new Date()
	});
}


function cargarProvinciasByDepto(){

	//Llenar combo provincias por marca departamento

	var baseURL = $("meta[name='ctx']").attr("content");	

	var url = baseURL+"/usuario/provinciasByDepto/";	

	$('#cMstDomicilioDep').change(function() {	

		$("#cMstDomicilioProv").load(url, $("#cMstDomicilioDep").serialize());

	});
}


function cargarDistritosByDeptoProv(){

	//Llenar combo distritos por marca departamento

	var baseURL = $("meta[name='ctx']").attr("content");	

	var url = baseURL+"/usuario/distritosByDeptoProv/";	

	$('#cMstDomicilioProv').change(function() {	

		$("#cMstDomicilioDis").load(url, $("#cMstDomicilioDep").serialize()+'&'+$("#cMstDomicilioProv").serialize());

	});

}


function validarPersona() {

	var dni = $("#nMstDni").val();	

	if(dni == null || dni == '') {
		mensaje("warning","Advertencia","Por favor debe ingresar el Dni de la persona");
		$('#nMstDni').focus();
	}else if(dni.length!=8){
		mensaje("warning","Advertencia","Por favor debe ingresar un Dni valido");
		$('#nMstDni').focus();
	}else{

		var baseURL = $("meta[name='ctx']").attr("content");	

		$.ajax({
			type: 'GET',
			url:  baseURL + '/rest/servicio/obtenerDatosPersona/'+dni,

			beforeSend : (request) => {	

				//this.bloquearPantalla();//bloqueo la pantalla
				this.bloqueo();
			},
			success: (data) => {	

				//this.desbloquearPantalla();//desbloqueamos la pantalla		
				this.desbloqueo();

				if(data!=null && data!='') {					

					this.llenarDatosPersona(data);			

				}else{
					mensaje("warning","Advertencia","Número de documento no encontrado");
					$('#nMstDni').focus();
				}					
			},
			complete: () => {
				//this.desbloquearPantalla();//desbloqueamos la pantalla
				this.desbloqueo();
			},
			error :(request, status, error) => { 
				mensaje("error","Error","Oucurrió un error al consultar dni");
			}
		});		
	}
}


function llenarDatosPersona(datos){

	if(datos.mensaje!=null & datos.mensaje!=''){
		mensaje("warning","Advertencia",datos.mensaje);	
	}

	$('#nMstApepaterno').val(datos.apPaterno);
	$('#nMstApematerno').val(datos.apMaterno);
	$('#nMstNombre').val(datos.nombres);

	console.debug("foto de la persona "+datos.foto);
}



function validarFormulario(){		

	if ($("#nMstDni").val() == null || $("#nMstDni").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar Dni");	
		$('#nMstDni').focus();
		return false;			
	}

	if ($("#nMstNombre").val() == null || $("#nMstNombre").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar nombres");	
		$('#nMstNombre').focus();
		return false;			
	}	

	if ($("#nMstApepaterno").val() == null || $("#nMstApepaterno").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar apellido paterno");	
		$('#nMstApepaterno').focus();
		return false;			
	}

	if ($("#nMstApematerno").val() == null || $("#nMstApematerno").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar apellido materno");	
		$('#nMstApematerno').focus();
		return false;			
	}	

	if ($("#nMstSexo").val() == null || $("#nMstSexo").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccionar Sexo");	
		$('#nMstSexo').focus();
		return false;			
	}

	if ($("#fechaNacimiento").val() == null || $("#fechaNacimiento").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingrese la fecha de nacimiento");	
		$('#fechaNacimiento').focus();
		return false;			
	}

	if ($("#nMstEmail").val() == null || $("#nMstEmail").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingresar un email");	
		$('#nMstEmail').focus();
		return false;			
	}

	if (!this.validarEmail($("#nMstEmail").val())){		
		mensaje("warning","Advertencia","Por favor ingresar un email valido");	
		$('#nMstEmail').focus();
		return false;			
	}

	if ($("#cEstcCodigo").val() == null || $("#cEstcCodigo").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccionar estado civil");	
		$('#cEstcCodigo').focus();
		return false;			
	}


	if ($("#cMstDomicilioDep").val() == null || $("#cMstDomicilioDep").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione departamento del domicilio");	
		$('#cMstDomicilioDep').focus();
		return false;			
	}

	if ($("#cMstDomicilioProv").val() == null || $("#cMstDomicilioProv").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione provincia del domicilio");	
		$('#cMstDomicilioProv').focus();
		return false;			
	}

	if ($("#cMstDomicilioDis").val() == null || $("#cMstDomicilioDis").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione distrito del domicilio");	
		$('#cMstDomicilioDis').focus();
		return false;			
	}

	if ($("#nMstDomicilio").val() == null || $("#nMstDomicilio").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingrese direccion");	
		$('#nMstDomicilio').focus();
		return false;			
	}	

	if ($("#cSitCodigo").val() == null || $("#cSitCodigo").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione situacion");	
		$('#cSitCodigo').focus();
		return false;			
	}

	if ($("#cTingCodigo").val() == null || $("#cTingCodigo").val() == ''){		
		mensaje("warning","Advertencia","Por favor seleccione el tipo de regimen");	
		$('#cTingCodigo').focus();
		return false;			
	}

	if ($("#codOficDestaque").val() == null || $("#codOficDestaque").val() == 0){		
		mensaje("warning","Advertencia","Por favor ingrese la oficina de destaque");	
		$('#codOficDestaque').focus();
		return false;			
	}

	if ($("#codOficina").val() == null || $("#codOficina").val() == 0){		
		mensaje("warning","Advertencia","Por favor ingrese la oficina");	
		$('#codOficina').focus();
		return false;			
	}

	if ($("#fechaInMin").val() == null || $("#fechaInMin").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingrese la fecha de ingreso al ministerio");	
		$('#fechaInMin').focus();
		return false;			
	}

	if ($("#nMstLogin").val() == null || $("#nMstLogin").val() == ''){		
		mensaje("warning","Advertencia","Por favor ingrese su user name");	
		$('#nMstLogin').focus();
		return false;			
	}
	
	var id = $("#cPerlCodigo").val();	
	
	if(id==null || id ==0){  
		
		//solo valido cuando es nuevo registro
		if ($("#password").val() == null || $("#password").val() == ''){		
			mensaje("warning","Advertencia","Por favor ingrese su password");	
			$('#password').focus();
			return false;			
		}
		
	}	

	return true;

}


function guardarDatosUsuario() {

	if (validarFormulario()) {	

		var baseURL = $("meta[name='ctx']").attr("content");		

		var url = baseURL + '/usuario/guardarDatosUsuario';

		var form = $('#frmUsuario')[0];

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

function mostrarModalOficinas(flag){

	//$('#modalSelectorOficina .modal-title').html("Seleccionar Oficina");
	$('#modalSelectorOficina .modal-body').html("Procesando...");
	$('#modalSelectorOficina').modal('show');

	var baseURL = $("meta[name='ctx']").attr("content");		

	var url = baseURL + '/usuario/contenedorOficinas?flag='+ flag;


	$.get(url,function(respuesta){		
		$('#modalSelectorOficina .modal-body').html(respuesta);
	});
}




function validarCamposNumericos(){	
	/*$(".input_numeric").keydown(function(event){		
		if((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105) && 
				event.keyCode !==190  && event.keyCode !==110 && event.keyCode !==8 && event.keyCode !==9  ){
			return false;
		}
	});*/

	$('.input_numeric').on('input', function () { 
		this.value = this.value.replace(/[^0-9]/g,'');
	});	
}

