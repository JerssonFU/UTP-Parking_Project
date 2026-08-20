//agregado regresar al inicio de la pagina
$(document).ready(function(){

	$(window).scroll(function () {
		if ($(this).scrollTop() > 50) {
			$('#back-to-top').fadeIn();
		} else {
			$('#back-to-top').fadeOut();
		}
	});

	// scroll body to 0px on click
	$('#back-to-top').click(function () {
		$('#back-to-top').tooltip('hide');
		$('body,html').animate({
			scrollTop: 0
		}, 600);
		return false;
	});

	$('#back-to-top').tooltip('show');       
});


/*$(document).ready(function(){
   // $('body').append('<div id="back-to-top" class="btn btn-info"><span class="glyphicon glyphicon-chevron-up"></span> Back to Top</div>');
  	$(window).scroll(function () {
			if ($(this).scrollTop() != 0) {
				$('#back-to-top').fadeIn();
			} else {
				$('#back-to-top').fadeOut();
			}
		}); 
  $('#back-to-top').click(function(){
      $("html, body").animate({ scrollTop: 0 }, 600);
      return false;
  });
});*/



//Para Manejo de mensajes estilo toastar -notificacion

function mensaje(type,title,text){	
	// icon=type info,warning,success,error	
	$.toast({
		heading: title,
		text: text,
		position: 'top-right',
		loaderBg:'#ff6849',
		icon: type,
		hideAfter: 3000, 
		stack: 6
	});
}


function minavegador() {
	if((navigator.userAgent.indexOf("Opera") || navigator.userAgent.indexOf('OPR')) != -1 ) 
	{
		return 'opera';
	}
	else if(navigator.userAgent.indexOf("Chrome") != -1 )
	{
		return 'chrome'
	}
	else if(navigator.userAgent.indexOf("Safari") != -1)
	{
		return 'safari';
	}
	else if(navigator.userAgent.indexOf("Firefox") != -1 ) 
	{
		return firefox;
	}
	else if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) //IF IE > 10
	{
		return 'IE';
	}  
	else 
	{
		return 'unknown';
	}
}


//Metodo 1 para bloquear pantalla

function bloqueo(){
	$(".preloader").fadeIn();
}

function desbloqueo(){
	$(".preloader").fadeOut();
}

//Metodo 2 para bloquear pantalla

function bloquearPantalla(){
	$.LoadingOverlay("show", {
		image       : "",
		fontawesome : "fa fa-spinner fa-spin",
		maxSize     : 60
		// text        : "Loading..."
	});		
}

function desbloquearPantalla(){
	$.LoadingOverlay("hide");
}

//Metodo 3 para bloquear pantalla
function bloquearPantalla1(){	
	$.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		color: '#fff' 
	},message: 'Cargando...'
	} );
}

function desbloquearPantalla1(){
	$.unblockUI();
}




function encode(val){
	var eVal;

	if(!encodeURIComponent){
		eVal=escape(val);
		eVal=eVal.replace(/@/g,"%40");
		eVal=eVal.replace(/\//g,"%2F");
		eVal=eVal.replace(/\+/g,"%2B");
		eVal=eVal.replace(/'/g,"%60");
		eVal=eVal.replace(/"/g,"%22");
		eVal=eVal.replace(/`/g,"%27");
		eVal=eVal.replace(/&/g,"%26");
	}else{
		eVal=encodeURIComponent(val);
		eVal=eVal.replace(/~/g,"%7E");
		eVal=eVal.replace(/!/g,"%21");
		eVal=eVal.replace(/\(/g,"%28");
		eVal=eVal.replace(/\)/g,"%29");
		eVal=eVal.replace(/'/g,"%27");
		eVal=eVal.replace(/"/g,"%22");
		eVal=eVal.replace(/`/g,"%27");
		eVal=eVal.replace(/&/g,"%26");
	}

	return eVal.replace(/\%20/g,"+");
}


function obtenerextension(mimetype) {
	if (mimetype=='application/pdf') return '.pdf';
	if (mimetype=='application/msword') return '.doc';
	if (mimetype=='video/x-msvideo') return '.avi';
	if (mimetype=='application/x-bzip') return '.avi';
	if (mimetype=='text/csv') return '.csv';
	if (mimetype=='image/gif') return '.gif';
	if (mimetype=='text/html') return '.html';
	if (mimetype=='image/x-icon') return '.ico';
	if (mimetype=='image/jpeg') return '.jpg';
	if (mimetype=='video/mpeg') return '.mpeg';
	if (mimetype=='application/vnd.oasis.opendocument.presentation') return '.odp';
	if (mimetype=='application/vnd.oasis.opendocument.spreadsheet') return '.ods';
	if (mimetype=='application/vnd.oasis.opendocument.text') return '.odt';
	if (mimetype=='application/rt') return '.rtf';
	if (mimetype=='application/vnd.ms-excel') return '.xls';
	if (mimetype=='application/zip') return '.zip';
	if (mimetype=='application/vnd.openxmlformats-officedocument.wordprocessingml.document') return '.docx';
	if (mimetype=='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') return '.xlsx';
	if (mimetype=='application/vnd.openxmlformats-officedocument.presentationml.presentation') return '.pptx';
}


function esIE() {
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent) || navigator.userAgent.indexOf("Trident/") > -1 ){ 
		return true;
	}
	else return false;
}



function visualizar_file_modal(data,tipo,nombre_file){	

	var mimetipo=data.type;

	if (mimetipo!="application/pdf") {
		descargar_file(data,nombre_file); 
		return; 
	}	

	var blob = new Blob([data], {type: mimetipo });		

	if (blob.size >0) {

		if(esIE()){
			window.navigator.msSaveOrOpenBlob(blob);
		}else {
			var height=600;
			var width=800;
			var left=0;
			if (tipo==1) left = window.screen.width;
			else if(tipo==2) left = 600;
			var top = (window.screen.height / 2) - ((height / 2) + 50);

			var url = URL.createObjectURL(blob);
			// var printWindow = window.open(url, '', 'width='+1200,height=800');
			var printWindow=window.open(url,'',
					"status=no,height=" + height + ",width=" + width + ",resizable=yes,left="
					+ left + ",top=" + top + ",screenX=" + left + ",screenY="
					+ top + ",toolbar=no,menubar=no,scrollbars=yes,location=no,directories=no");
			//printWindow.print(); //function que permite mostrar primero la ventana de imprimir
		}
	}else{
		mensaje("error","Error", "ocurrio un error al visualizar el archivo");
	}
}


function descargarArchivo(data,nombre_file) {
	var mimetipo=data.type;
	var blob = new Blob([data], { type: mimetipo });
	if(blob.size>0) {
		var link = document.createElement('a');
		link.href = window.URL.createObjectURL(blob);

		var fecha = new Date();
		var horas = fecha.getHours();
		var minutos = fecha.getMinutes();
		var segundos = fecha.getSeconds();

		var nombrearchivo = nombre_file + horas + "" + minutos + "" + segundos;//+".doc";

		if(minavegador()!='IE'){

			link.download = nombrearchivo;//+".doc";
			link.click();  
		}
		else if(minavegador()=='IE'){
			window.navigator.msSaveOrOpenBlob(blob, nombrearchivo + obtenerextension(mimetipo));
		}
	}
	else {
		mensaje("warning","Información","No existe el archivo para ser descargado");
	}
}


function validarEmail(valor){
	var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(!regex.test(valor)) {
		return false;
	}else{
		return true;
	}
}


function reloadTable(table,url) {

	table.ajax.url(url).load();
}



