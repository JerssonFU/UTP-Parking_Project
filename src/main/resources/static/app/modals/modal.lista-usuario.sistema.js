
var codUsuario ="";

$(function() {

	$(document).ready( () => {

		inicializar();	

	});

});


function inicializar(){
	
	codUsuario = $('#codUsuario').val();

	this.configurarTblUsuSistema();		

}


function configurarTblUsuSistema(){	
	
	var baseURL = $("meta[name='ctx']").attr("content");

	var tablaUsuSistema = $('#tblUsuSistema').dataTable({
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
			"url" : baseURL + '/usuario/filtrarUsuarioSistema?codUsuario='+ codUsuario,
			"type": "GET",				
			"dataSrc": ""
		},

		"columnDefs": [
			{ "width": "5%", "targets": 0 },
			{ "width": "30%", "targets": 1 },
			{ "width": "5%", "targets": 2 }
			
			],
			"columns": [
				{"data": "cUsCodigo",sClass :"ocultar"}, 				
				{"data": "tipoUsuario"}, 								
				{"data": "sistema"}
				],					

				"drawCallback": function(settings) {

				},
				"destroy" : true 
	});	

}













