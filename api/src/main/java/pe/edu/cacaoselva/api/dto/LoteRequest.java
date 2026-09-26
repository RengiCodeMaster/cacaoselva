package pe.edu.cacaoselva.api.dto;

import java.math.BigDecimal;

import pe.edu.cacaoselva.domain.model.EstadoLote;

public record LoteRequest(String socio, BigDecimal pesoKg, EstadoLote estado) {
}
