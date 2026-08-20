var _ctx =$("meta[name='ctx']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");
$(function(){
console.log("Listar profesiones");
var url=_ctx+'/rest/profesiones/listarprofesiones';
var tblProfesiones=$('#tblProfesiones').DataTable({
	"dom": 'Bfrtip',
	"sAjaxDataProp": "",
	"sAjaxSource": url,
	"pageLength": 10,
	"pageLength": 10,
	"bScrollCollapse": true,
	"responsive": true,
	"order": [[ 0, "desc" ]],
   	"language": {
        "url": _ctx+"/assets/plugins/datatables/ln/Spanish.json"
      },
  	"buttons": [

			{
			 extend: 'csvHtml5',
			 exportOptions: {
			       columns: [ 0, 1, 2, 3, 4, 5 ]
			 				}
			},
			{
			 extend: 'excelHtml5',
			 exportOptions: {
			       columns: [ 0, 1, 2, 3, 4, 5 ]
			 				}
			},
			{
			extend: 'pdfHtml5',
			exportOptions: {
			       columns: [ 0, 1, 2, 3, 4, 5 ]
						}
			},
	 	],
     
 	 "columns": [		       
	       	{"data": "nprofnombre"}, 
	       	{"data": "dproffecharegistro"},
	       	{"data": "dproffechamodificacion"},
	       	{"data": "cprofusuarioregistro"},
	       	{"data": "cprofusuariomodificacion"},
	       	{"data": "fprofestado",  render : function (data, type, row) {
	       	
	            switch(data) {
	               case 0 : return 'INACTIVO'; break;
	               case 1 : return 'ACTIVO'; break;
	               default  : return 'N/A';
	            }
	          }
	      	},
	          {
					"data": null,
					"bSortable": false,
					"mRender": function (o) {
						return '<button title="Ver" class="btn btn-xs btn-outline-info"> <i class="fa fa-eye"></i> </button> <button title="Editar" class="btn btn-xs btn-outline-success btnEditarProfesiones"> <i class="fa fa-outdent"></i> </button>';
					}
				}
	     
	      	
	       ],
	       "drawCallback": function(settings) {
        	 	console.log(settings.json);
      	    	}
  
});

$('#tblProfesiones tbody').on( 'click', '.btnEditarProfesiones', function () {
		var obj = tblProfesiones.row($(this).parents('tr')).data();
		if(obj==null) obj = tblProfesiones.row($(this)).data();
		var tipoaccion="editar";
		generarModalProfesiones(tipoaccion,obj);
	});

});

function generarModalProfesiones(tipoaccion,obj){
	$.LoadingOverlay("show");
	var cprofcodigo=obj.cprofcodigo;

	
	$.ajax({
	    type:'POST',
	    url: _ctx+'/profesiones/generarmodalprofesion',
	    dataType: 'html',
	    data:{
	    cprofcodigo:cprofcodigo,
	    },
	    beforeSend: function(xhr) {
		 xhr.setRequestHeader(header,token);
		 } ,
	    success: function (data) {
	    console.log("Impreme data");
	    console.log(data);
	       	$('#modalshow').html(data);
	    	$('#modalprofesion').modal('show');
	    	$.LoadingOverlay('hide');
	    },
	    error: function (xhr,request, error) {
			  mensaje('error','Error','Oucrrió un error mientras se espera la respuesta del servidor');
			  
	    }
	});
	
}