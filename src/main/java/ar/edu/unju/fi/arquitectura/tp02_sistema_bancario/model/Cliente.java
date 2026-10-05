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
     * Define si el cliente es el dueño de la cuenta o un familiar vinculado.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolCliente rol;

    /**
     * Relación recursiva: Si este cliente es un ADHERENTE, aquí guardamos
     * quién es su TITULAR principal. Si es titular, esto queda en null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titular_id")
    private Cliente titular;

    /**
     * Relación recursiva: Si este cliente es TITULAR, aquí se guarda la lista
     * de sus familiares adherentes vinculados.
     */
    @OneToMany(mappedBy = "titular", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cliente> adherentes = new ArrayList<>();

    /**
     * Estado operativo del cliente en el sistema.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCliente estado;

    /**
     * Token único generado automáticamente para el enlace de validación por correo.
     */
    @Column(name = "token_activacion", unique = true)
    private String tokenActivacion;

    /**
     * Fecha y hora máxima en la que el token es válido (24 horas desde la creación).
     */
    @Column(name = "fecha_expiracion_token")
    private LocalDateTime fechaExpiracionToken;


    /**
     * Colección de cuentas bancarias asociadas al cliente.
     * Mapeo bidireccional con eliminación en cascada y de huérfanos.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CuentaBancaria> cuentas = new ArrayList<>();







}
