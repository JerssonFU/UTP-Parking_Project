package UTP.Parking.UTP.Parking.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -5029676471020699828L;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHAREGISTRO")
	private Date fechaRegistro;
	
	@Column(name="USUARIOREGISTRO")
	private Long usuarioRegistro;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHAMODIFICA")	
	private Date fechaModifica;

	@Column(name="USUARIOMODIFICA")
	private Long usuarioModifica;
	
	@Column(name="IPREGISTRO")
	private String ipRegistro;
	
	@Column(name="IPMODIFICA")
	private String ipModifica;
	
	@Column(name="OPERACION")
	private Integer operacion;
	
		

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Long getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(Long usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public Long getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(Long usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getIpRegistro() {
		return ipRegistro;
	}

	public void setIpRegistro(String ipRegistro) {
		this.ipRegistro = ipRegistro;
	}

	public String getIpModifica() {
		return ipModifica;
	}

	public void setIpModifica(String ipModifica) {
		this.ipModifica = ipModifica;
	}

	public Integer getOperacion() {
		return operacion;
	}

	public void setOperacion(Integer operacion) {
		this.operacion = operacion;
	}
	
	
	
	

	

	
	
}
