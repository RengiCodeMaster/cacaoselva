package pe.edu.cacaoselva.application.exception;

public class IdentificadorLoteInvalidoException extends RuntimeException {

    private static final String MENSAJE = "El identificador del lote es invalido: %s";

    public IdentificadorLoteInvalidoException(Object identificador) {
        super(MENSAJE.formatted(identificador));
    }
}
