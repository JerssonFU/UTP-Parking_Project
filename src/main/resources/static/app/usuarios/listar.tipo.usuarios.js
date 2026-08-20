
$(function() {

	$(document).ready( () => {

		inicializarTipoDocumentos();	

	});

});


function inicializarTipoDocumentos(){	

	this.configurarTblTipoUsuarios();	

	this.editarTipoUsuario();
}


function configurarTblTipoUsuarios() {	
	
	var baseURL = $("meta[name='ctx']").attr("content");

	var tablaOficinas = $('#tblTipoUsuarios').dataTable({
		//dom: "Bfrtip",
		"processing": true,
		"serverSide": false,
		"pageLength": 10,
		"bDestroy": true,
		"responsive": true,

		"select": true,		
		"language": {
			"url": baseURL + "/assets/plugins/datatables/ln/Spanish.json"
		},

		"ajax": {
			"url" : baseURL + '/tipousuario/filtro-tipo-usuarios/',
			"type": "GET",				
			"dataSrc": ""
		},

		"columnDefs": [
			{ "width": "5%", "targets": 0 },
			{ "width": "20%", "targets": 1 },
			{ "width": "20%", "targets": 2 },
			{ "width": "5%", "targets": 3 }
			],
			"columns": [
				{"data": "cTusuCodigo",sClass :"ocultar"}, 				
				{"data": "nTusuDescripcion"}, 								
				{"data": "cTusuDetalle"},				
				{
					"data": null,
					"bSortable": false,
					sClass : "center",
					"mRender": function (o) {						
						return '<button type="button" class="btn waves-effect waves-light btn-rounded btn-sm btn-info editarTipoUsuario"><i class="fas fa-edit"/></button>';
					}
				}

				],					

				"drawCallback": function(settings) {

				},
				"destroy" : true 
	});	

}


function editarTipoUsuario(){

	$('#tblTipoUsuarios tbody').on( 'click', '.editarTipoUsuario', function () {

		var tablaTipUsu = $('#tblTipoUsuarios').DataTable();
		var row = tablaTipUsu.row( $(this).parents('tr')).data();
		if(row==null) 
			row=tablaTipUsu.row( $(this)).data();	       
       	
		
		mostrarModalTipoUsuario(row.cTusuCodigo);

	} );
}

function mostrarModalTipoUsuario(codigo){

	//$('#modalSelectorOficina .modal-title').html("Seleccionar Oficina");
	$('#modalNuevoTipoUsuario .modal-body').html("Procesando...");
	$("#modalNuevoTipoUsuario").modal({backdrop: false});
	$('#modalNuevoTipoUsuario').modal('show');

	var baseURL = $("meta[name='ctx']").attr("content");		

	var url = baseURL + '/tipousuario/contenedorTipoUsuario?codigo='+ codigo;


	$.get(url,function(respuesta){		
		$('#modalNuevoTipoUsuario .modal-body').html(respuesta);
	});
}


function recargarTblTipoUsuarios(){
	configurarTblTipoUsuarios();
}







