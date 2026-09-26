package pe.edu.cacaoselva.application.exception;

public class LoteNoEncontradoException extends RuntimeException {

    private static final String MENSAJE = "No existe el lote con id %d";

    public LoteNoEncontradoException(Integer id) {
        super(MENSAJE.formatted(id));
    }
}
