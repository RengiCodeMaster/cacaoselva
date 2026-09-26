package pe.edu.cacaoselva.infrastructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.edu.cacaoselva.domain.model.EstadoLote;

@Entity
@Table(name = "lotes")
public class LoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String socio;

    @Column(name = "peso_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoLote estado;

    protected LoteEntity() {
    }

    public LoteEntity(Integer id, String socio, BigDecimal pesoKg, EstadoLote estado) {
        this.id = id;
        this.socio = socio;
        this.pesoKg = pesoKg;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public String getSocio() {
        return socio;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public EstadoLote getEstado() {
        return estado;
    }
}
