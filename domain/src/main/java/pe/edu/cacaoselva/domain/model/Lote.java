package pe.edu.cacaoselva.domain.model;

import java.math.BigDecimal;

public record Lote(Integer id, String socio, BigDecimal pesoKg, EstadoLote estado) {
}
