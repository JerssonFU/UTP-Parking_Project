var _ctx =$("meta[name='ctx']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var token = $("meta[name='_csrf']").attr("content");
$(function(){
console.log("Listar profesiones");
var url=_ctx+'/rest/listavalor/';
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
});