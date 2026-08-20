
var flag ="";

$(function() {

	$(document).ready( () => {

		inicializar();	

	});

});


function inicializar(){
	
	flag = $('#flag').val();

	this.configurarTblOficinas();	

	this.seleccionarOficina();
	
	

}


function configurarTblOficinas(){	
	
	var baseURL = $("meta[name='ctx']").attr("content");

	var tablaOficinas = $('#tblOficinas').dataTable({
		//dom: "Bfrtip",
		"processing": true,
		"serverSide": false,
		"pageLength": 5,
		"bDestroy": true,
		"responsive": true,

		"select": true,		
		"language": {
			"url": baseURL + "/assets/plugins/datatables/ln/Spanish.json"
		},

		"ajax": {
			"url" : baseURL + '/usuario/filtrarOficinas?flag='+ flag,
			"type": "GET",				
			"dataSrc": ""
		},

		"columnDefs": [
			{ "width": "5%", "targets": 0 },
			{ "width": "30%", "targets": 1 },
			{ "width": "5%", "targets": 2 },
			{ "width": "5%", "targets": 3 }
			],
			"columns": [
				{"data": "cUnoCodigo",sClass :"ocultar"}, 				
				{"data": "nUnoDescripcion"}, 								
				{"data": "nUnoSigla"},				
				{
					"data": null,
					"bSortable": false,
					sClass : "center",
					"mRender": function (o) {						
						return '<button type="button" class="btn waves-effect waves-light btn-rounded btn-sm btn-success seleccionarOficina">Seleccionar</button>';
					}
				}

				],					

				"drawCallback": function(settings) {

				},
				"destroy" : true 
	});	

}


function seleccionarOficina(){

	$('#tblOficinas tbody').on( 'click', '.seleccionarOficina', function () {

		var tablaOficinas = $('#tblOficinas').DataTable();
		var row = tablaOficinas.row( $(this).parents('tr')).data();
		if(row==null) 
			row=tablaOficinas.row( $(this)).data();	

        console.debug("row.cUnoCodigo==="+row.cUnoCodigo);
        console.debug("row.nUnoDescripcion==="+row.nUnoDescripcion);
        
        if(flag == '1'){
        	$('#codOficDestaque').val(row.cUnoCodigo);
    		$('#oficinaDestaque').val(row.nUnoDescripcion);
        }else  if(flag == '2'){
        	$('#codOficina').val(row.cUnoCodigo);
    		$('#oficina').val(row.nUnoDescripcion);
        }
        
		$('#modalSelectorOficina').modal('hide');

	} );
}










