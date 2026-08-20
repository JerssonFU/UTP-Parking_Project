package UTP.Parking.UTP.Parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "\"C_PERL_CODIGO\"")
    private Long idUsuario;

    @Column(name = "\"N_MST_LOGIN\"")
    private String username;

    @Column(name = "\"N_MST_NOMBRE\"")
    private String nombres;

    @Column(name = "\"N_MST_APEPATERNO\"")
    private String apellidoPaterno;

    @Column(name = "\"N_MST_APEMATERNO\"")
    private String apellidoMaterno;

    @Column(name = "\"N_MST_EMAIL\"")
    private String email_universitario;

    @Column(name = "\"C_SIT_CODIGO\"")
    private String estadoDb;

    @Column(name = "\"N_MST_PASSWORD\"")
    private String password;

    @Column(name = "\"C_UNO_CODIGO_OFICINA\"")
    private String codigoUniversitario;

    @Column(name = "\"N_UNO_DESCRIPCION\"")
    private String descripcionCarrera;

    @Column(name = "\"ID_ROL\"")
    private Long idRol;

    @Transient
    private List<RolesUsuario> roles;

    @Transient
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    private List<Vehiculo> vehiculos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    private List<Registro> registrosUsuario;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioSeguridad", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuarioSeguridad"})
    private List<Registro> registrosSeguridad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"})
    private List<Solicitud> solicitudes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioSae", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuarioSAE"})
    private List<Solicitud> solicitudesSae;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Integer getEstado() {
        if (estadoDb == null || estadoDb.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(estadoDb.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setEstado(Integer estado) {
        this.estadoDb = estado == null ? null : String.valueOf(estado);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigoUniversitario() {
        return codigoUniversitario;
    }

    public void setCodigoUniversitario(String codigoUniversitario) {
        this.codigoUniversitario = codigoUniversitario;
    }

    public String getDescripcionCarrera() {
        return descripcionCarrera;
    }

    public void setDescripcionCarrera(String descripcionCarrera) {
        this.descripcionCarrera = descripcionCarrera;
    }

    public String getEmail_universitario() {
        return email_universitario;
    }

    public void setEmail_universitario(String email_universitario) {
        this.email_universitario = email_universitario;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public List<RolesUsuario> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesUsuario> roles) {
        this.roles = roles;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public List<Registro> getRegistrosUsuario() {
        return registrosUsuario;
    }

    public void setRegistrosUsuario(List<Registro> registrosUsuario) {
        this.registrosUsuario = registrosUsuario;
    }

    public List<Registro> getRegistrosSeguridad() {
        return registrosSeguridad;
    }

    public void setRegistrosSeguridad(List<Registro> registrosSeguridad) {
        this.registrosSeguridad = registrosSeguridad;
    }

    public List<Solicitud> getSolicitudesSae() {
        return solicitudesSae;
    }

    public void setSolicitudesSae(List<Solicitud> solicitudesSae) {
        this.solicitudesSae = solicitudesSae;
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
}
