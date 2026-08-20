package UTP.Parking.UTP.Parking.service;


import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private String nombres;
    
    private String apellidoPaterno;
    
    private String apellidoMaterno;
    
    private String codigoUniversitario;
    
    private String descripcionCarrera;

    private String username;   
    
    private String email_universitario;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;   
    
    

    public UserPrincipal(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String codigoUniversitario,
			String descripcionCarrera, String username, String password,String email_universitario, Role role,
			Collection<? extends GrantedAuthority> authorities) {		
		this.id = id;
		this.nombres = nombres;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.codigoUniversitario = codigoUniversitario;
		this.descripcionCarrera = descripcionCarrera;
		this.username = username;
		this.password = password;
		this.email_universitario  = email_universitario;
        this.role= role;
         this.authorities = authorities;

	}	

    public static UserPrincipal build(Usuario user) {
        List<GrantedAuthority> authorities = user.getRoles() == null
                ? java.util.Collections.emptyList()
                : user.getRoles().stream()
                    .filter(role -> role != null && role.getAlias() != null && !role.getAlias().trim().isEmpty())
                    .map(role -> {
                        String alias = role.getAlias().trim().toUpperCase();
                        return new SimpleGrantedAuthority(alias.startsWith("ROLE_") ? alias : "ROLE_" + alias);
                    })
                    .collect(Collectors.toList());

        return new UserPrincipal(
                user.getIdUsuario(),
                user.getNombres(),
                user.getApellidoPaterno(),
                user.getApellidoMaterno(),
                user.getCodigoUniversitario(),
                user.getDescripcionCarrera(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail_universitario(),
                user.getRole(),
                authorities
        );
    }

    public Long getId() {
        return id;
    } 

    public String getNombres() {
		return nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(id, user.id);
    }
}