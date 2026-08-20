package UTP.Parking.UTP.Parking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rolesusuario")
public class RolesUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "descripcion_rol")
    private String descripcion;

    @Column(name = "alias_rol")
    private String alias;

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
