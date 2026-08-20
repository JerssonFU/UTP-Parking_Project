var tblUsuarios;
var baseURL = $("meta[name='ctx']").attr("content");
$.fn.dataTableExt.oApi.fnReloadAjax = function(oSettings, sNewSource) {
	this.fnClearTable(this);
	this.oApi._fnProcessingDisplay(oSettings, true);
	var that = this;

	$.getJSON(oSettings.sAjaxSource, null, function(json) {
		oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
		that.fnDraw(that);
		that.oApi._fnProcessingDisplay(oSettings, false);
	});
};

$(function() {			
	inicializar();		
});


function inicializar(){

	busquedaAvanzada = 0;

	configurarTablaUsuarios();

	//$('[data-toggle="tooltip"]').tooltip();		

	inicializarBusqAvanzada();
}



function configurarTablaUsuarios(){



	tblUsuarios = $('#tblUsuarios')
	.dataTable(					
			{
				"pageLength": 10,
				/*"responsive": {
					"details": {
						"display": $.fn.dataTable.Responsive.display.modal( {
							"header": function ( row ) {
								var data = row.data();
								return 'Detalle de '+data[3]+' '+data[1];
							}
						} ),
						"renderer": $.fn.dataTable.Responsive.renderer.tableAll( {
							"tableClass": 'table'
						} )
					}
				},*/								
				"responsive": true,						
				"bSort" : false,							
				"drawCallback" : function(settings) {							
					postRecarga();
				},	

				"columnDefs" : [ 
					{"width": "3%", "targets" : 0,	"orderable" : false},
					{"width": "3%", "targets" : 1,  "orderable" : false},
					{"width": "7%", "targets" : 2,  "orderable" : false},
					{"width": "7%", "targets" : 3,  "orderable" : false},
					{"width": "7%", "targets" : 4,	"orderable" : false},
					{"width": "3%", "targets" : 5,  "orderable" : false},
					{"width": "3%", "targets" : 6,  "orderable" : false},
					{"width": "3%", "targets" : 7,  "orderable" : false},
					{"width": "7%", "targets" : 8,	"orderable" : false},
					{"width": "3%", "targets" : 9,	"orderable" : false},
					{"width": "3%", "targets" : 10,	"orderable" : false},
					{"width": "3%", "targets" : 11,	"orderable" : false}
					],

					"bProcessing" : true,
					"bServerSide" : true,
					"language": {
						"url": baseURL +"/assets/plugins/datatables/ln/Spanish.json"
					},
					"sAjaxSource" : baseURL + "/usuario/filtro-usuarios",
					"fnServerData" : function(sSource, aoData, fnCallback) {

						aoData.push(
								{
									"name" : "exportaExcel",
									"value" : 0
								}
						);							

						configurarFiltro(aoData);

						$('#tblUsuarios').find('tbody').addClass('loading');							

						request = $.ajax({
							"dataType" : "json",
							"contentType" : "application/json; charset=UTF-8",
							"type" : "GET",
							"url" : sSource,
							"data" : aoData,
							"success" : fnCallback,
							"enctype" : "multipart/form-data"
						});

					},
					"bFilter" : false,
					"aoColumns" : [ {sClass : "dni"}					
					, {sClass : "apellidoPaterno"}	
					, {sClass : "apellidoMaterno"}
					, {sClass : "nombres"}	
					, {sClass : "fechaNacimiento"}						
					, {sClass : "oficina"}	
					, {sClass : "email"}	
					, {sClass : "userName"}
					, {sClass : "domicilio"}
					, {sClass : "sexo"}
					, {sClass : "estado"}
					, {sClass : "center btnSeleccionar"}
					]
			});
}


function configurarFiltro(aoData){

	if ($("#textoBusqueda").val() != null && $("#textoBusqueda").val() != ""){
		aoData.push(
				{
					"name" : "textoBusqueda",
					"value" :  encode($("#textoBusqueda").val())
				}
		);
	}	

	if ($("#dniBusq").val() != null && $("#dniBusq").val() != ""){
		aoData.push(
				{
					"name" : "dni",
					"value" :  encode($("#dniBusq").val())
				}
		);
	}

	if ($("#apPaternoBusq").val() != null && $("#apPaternoBusq").val() != ""){
		aoData.push(
				{
					"name" : "apellidoPaterno",
					"value" :  encode($("#apPaternoBusq").val())
				}
		);
	}

	if ($("#apMaternoBusq").val() != null && $("#apMaternoBusq").val() != ""){
		aoData.push(
				{
					"name" : "apellidoMaterno",
					"value" :  encode($("#apMaternoBusq").val())
				}
		);
	}
	
	if ($("#nombresBusq").val() != null && $("#nombresBusq").val() != ""){
		aoData.push(
				{
					"name" : "nombres",
					"value" :  encode($("#nombresBusq").val())
				}
		);
	}

	return aoData;
}

//Permite recargar la busqueda
function recargar(parBusquedaAvanzada) {
	
	busquedaAvanzada = parBusquedaAvanzada;

	if (parBusquedaAvanzada == 1){

		$('#buscarPersonalizado').button('loading');		
	}
	else {
		$('#buscarGeneral').button('loading');
	}

	tblUsuarios.fnReloadAjax();
}

function postRecarga() {

	$('[data-toggle="tooltip"]').tooltip();
	$('#buscarPersonalizado').button('reset');
	$('#buscarGeneral').button('reset');

	if (busquedaAvanzada == 1){
		$("#dlDropDownBAvanzada").dropdown("toggle");
		busquedaAvanzada == 0;
	}

	asegurarAvisoFiltroConReductor(0);
}

function asegurarAvisoFiltroConReductor(reductor) {
	var aoData = [];
	configurarFiltro(aoData);

	var tamanio = aoData.length - reductor; 

	if (tamanio > 0) {
		var mensaje = '';

		if (aoData.length == 1) {
			mensaje = tamanio + ' filtro aplicado';
		}
		else {
			mensaje = tamanio + ' filtros aplicados';
		}	 
		//$('#buscarGeneral').attr('title', mensaje).tooltip('fixTitle').tooltip('show');
	}
	else {
		//$('#buscarGeneral').tooltip('destroy');
	}	
}

function inicializarBusqAvanzada() {

	if ($('#dlDropDownBAvanzada') != null) {
		$('#dlDropDownBAvanzada').click(procesarAnchoDivDropDowMenu);
	}

	if ($('#textoBusqueda') != null) {
		$('#textoBusqueda').on('keyup', function (e) {
			if (e.keyCode == 13) {
				recargar(0);
			}
		});		
	}		
}


//Permite configurar el ancho del dropdown menu.

function procesarAnchoDivDropDowMenu() {
	$("#divDropDowMenu").css("width", $('#adv-search').width() - 42);
}


//Metodo para ir a registrar nuevo pase

function viewRegistrarUsuario(){

	var baseURL = $("meta[name='ctx']").attr("content");	

	console.debug("contexto =========="+baseURL);		

	var url = baseURL + '/usuario/crear-usuarios';	

	$("#fragDinamico").load(url);

	/*$.ajax({
		type : "GET",
		url : url,		
		success : function(data) {						
			$("#fragDinamico").html(data);
		},
	});	*/	
}

function editarUsuario(id){

	var baseURL = $("meta[name='ctx']").attr("content");	

	console.debug("contexto =========="+baseURL);		

	var url = baseURL + '/usuario/editar-usuario?codigoUsuario='+ id;	

	$("#fragDinamico").load(url);
}

function verUsuarioSistema(codigo){
	
	//$('#modalSelectorOficina .modal-title').html("Seleccionar Oficina");
	$('#modalUsuarioSistema .modal-body').html("Procesando...");
	$("#modalUsuarioSistema").modal({backdrop: false});
	$('#modalUsuarioSistema').modal('show');

	var baseURL = $("meta[name='ctx']").attr("content");		

	var url = baseURL + '/usuario/contenedorListUsuarioSistema?codUsuario='+ codigo;


	$.get(url,function(respuesta){		
		$('#modalUsuarioSistema .modal-body').html(respuesta);
	});
}

function registarUsuarioSistema(codigo){
	
	//$('#modalSelectorOficina .modal-title').html("Seleccionar Oficina");
	$('#modalUsuarioSistema .modal-body').html("Procesando...");
	$("#modalUsuarioSistema").modal({backdrop: false});
	$('#modalUsuarioSistema').modal('show');

	var baseURL = $("meta[name='ctx']").attr("content");		

	var url = baseURL + '/usuario/contenedorUsuarioSistema?codUsuario='+ codigo;


	$.get(url,function(respuesta){		
		$('#modalUsuarioSistema .modal-body').html(respuesta);
	});
	
	
	
}





