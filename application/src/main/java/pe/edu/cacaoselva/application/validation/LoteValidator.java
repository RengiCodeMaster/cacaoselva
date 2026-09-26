package pe.edu.cacaoselva.application.validation;

import java.math.BigDecimal;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.exception.DatosLoteInvalidosException;
import pe.edu.cacaoselva.application.exception.IdentificadorLoteInvalidoException;

public final class LoteValidator {

    private static final int LONGITUD_MAXIMA_SOCIO = 120;
    private static final BigDecimal PESO_MAXIMO = new BigDecimal("99999999.99");

    private LoteValidator() {
    }

    public static void validarId(Integer id) {
        if (id == null || id < 1) {
            throw new IdentificadorLoteInvalidoException(id);
        }
    }

    public static void validarDatos(DatosLote datos) {
        if (datos == null) {
            throw new DatosLoteInvalidosException("Los datos del lote son obligatorios");
        }
        if (datos.socio() == null || datos.socio().isBlank()) {
            throw new DatosLoteInvalidosException("El socio es obligatorio");
        }
        if (datos.socio().trim().length() > LONGITUD_MAXIMA_SOCIO) {
            throw new DatosLoteInvalidosException("El socio no puede superar 120 caracteres");
        }
        if (datos.pesoKg() == null || datos.pesoKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatosLoteInvalidosException("El peso debe ser mayor que cero");
        }
        if (datos.pesoKg().compareTo(PESO_MAXIMO) > 0 || datos.pesoKg().scale() > 2) {
            throw new DatosLoteInvalidosException("El peso admite hasta 8 enteros y 2 decimales");
        }
        if (datos.estado() == null) {
            throw new DatosLoteInvalidosException("El estado es obligatorio");
        }
    }
}
