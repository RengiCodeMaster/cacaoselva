package pe.edu.cacaoselva.application.exception;

public class ApiNoDisponibleException extends RuntimeException {

    public ApiNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    public ApiNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
