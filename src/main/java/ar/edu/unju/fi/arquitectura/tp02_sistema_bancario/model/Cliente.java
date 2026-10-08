package ar.edu.unju.fi.arquitectura.tp02_sistema_bancario.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dell
 * @since 22/09/2026
 */
@Entity
@Table(name="clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends AuditableEntity {
    /**
     * Identificador único autogenerado del cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nombre y apellido o Razon Social completo del cliente.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Clave Única de Identificación Laboral / Tributaria del cliente.
     */
    @Column(nullable = false, unique = true, length = 11)
    private String cuil;

    /**
     * Correo electrónico de contacto del cliente.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String direccion;

    @Column(nullable = false, unique = true, length = 20)
    private String telefono;


    /**
     * Colección de cuentas bancarias asociadas al cliente.
     * Mapeo bidireccional con eliminación en cascada y de huérfanos.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CuentaBancaria> cuentas = new ArrayList<>();

    private String tokenActivacion;

    private LocalDateTime tokenActivacionExpira;

    @Enumerated(EnumType.STRING)
    private EstadoCliente estado;



}
