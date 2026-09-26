package pe.edu.cacaoselva.api.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import pe.edu.cacaoselva.api.dto.ErrorResponse;
import pe.edu.cacaoselva.application.exception.DatosLoteInvalidosException;
import pe.edu.cacaoselva.application.exception.IdentificadorLoteInvalidoException;
import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(LoteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarLoteNoEncontrado(LoteNoEncontradoException excepcion) {
        return construirRespuesta(HttpStatus.NOT_FOUND, excepcion.getMessage());
    }

    @ExceptionHandler(IdentificadorLoteInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarIdentificadorInvalido(
            IdentificadorLoteInvalidoException excepcion) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, excepcion.getMessage());
    }

    @ExceptionHandler(DatosLoteInvalidosException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(DatosLoteInvalidosException excepcion) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, excepcion.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudIlegible(HttpMessageNotReadableException excepcion) {
        return construirRespuesta(HttpStatus.BAD_REQUEST,
                "La solicitud contiene datos ausentes o con formato invalido");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(MethodArgumentTypeMismatchException excepcion) {
        String mensaje = "El identificador del lote es invalido: " + excepcion.getValue();
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(HttpStatus estado, String mensaje) {
        ErrorResponse cuerpo = new ErrorResponse(estado.value(), mensaje, Instant.now());
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
