package UTP.Parking.UTP.Parking.base.exception;


public class AppException extends Exception{	
	
	private static final long serialVersionUID = 1L;		

	private String codError;
	
	private String mensaje;

	private String tipo;
	

	public AppException(String codError,String tipo,String mensaje){	
		this.codError =codError;
		this.mensaje=mensaje;			
		if(tipo.equals(Tipo.SUCCESS.getValor())){
			this.tipo= Tipo.SUCCESS.getValor();
		}else if(tipo.equals(Tipo.ERROR.getValor())){
			this.tipo= Tipo.ERROR.getValor();	
		}else if(tipo.equals(Tipo.WARNING.getValor())){
			this.tipo= Tipo.WARNING.getValor();	
		}else if(tipo.equals(Tipo.INFO.getValor())){
			this.tipo= Tipo.INFO.getValor();		
		}	
	}	
	
	public AppException(String tipo,String mensaje){		
		this.mensaje=mensaje;	
		if(tipo.equals(Tipo.SUCCESS.getValor())){
			this.tipo= Tipo.SUCCESS.getValor();
		}else if(tipo.equals(Tipo.ERROR.getValor())){
			this.tipo= Tipo.ERROR.getValor();	
		}else if(tipo.equals(Tipo.WARNING.getValor())){
			this.tipo= Tipo.WARNING.getValor();	
		}else if(tipo.equals(Tipo.INFO.getValor())){
			this.tipo= Tipo.INFO.getValor();		
		}	
	}	
	
	public AppException(String mensaje){		
		this.mensaje=mensaje;	
		
	}	
	

	public String getMensaje() {	
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void setInfo(String mensaje) {
		this.mensaje = mensaje;
		this.tipo= Tipo.ERROR.getValor();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}		

	public String getCodError() {
		return codError;
	}


	public void setCodError(String codError) {
		this.codError = codError;
	}
	

	public enum Tipo {	

		INFO("info"),
		ERROR("error"),
		WARNING("warning"),
		SUCCESS("success");

		private final String codigo;


		Tipo(String codigo) {
			this.codigo = codigo;
		}

		public String getValor() {
			return codigo;
		}
	}

}
