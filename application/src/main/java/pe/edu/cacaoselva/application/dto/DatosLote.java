package pe.edu.cacaoselva.application.dto;

import java.math.BigDecimal;

import pe.edu.cacaoselva.domain.model.EstadoLote;

public record DatosLote(String socio, BigDecimal pesoKg, EstadoLote estado) {
}
