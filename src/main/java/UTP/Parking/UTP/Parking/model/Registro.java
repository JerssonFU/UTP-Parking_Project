package UTP.Parking.UTP.Parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_registro;

    @Column(nullable = false)
    private LocalDateTime fecha_ingreso;

    private LocalDateTime fecha_salida;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario", "C_PERL_CODIGO"})
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_seguridad")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario", "C_PERL_CODIGO"})
    private Usuario usuarioSeguridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "vehiculos", "id_vehiculo"})
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estacionamiento")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "estacionamientos", "id_estacionamiento"})
    private Estacionamiento estacionamiento;
}
