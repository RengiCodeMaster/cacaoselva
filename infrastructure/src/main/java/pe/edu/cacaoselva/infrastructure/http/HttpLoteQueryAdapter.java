package pe.edu.cacaoselva.infrastructure.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.application.exception.ApiNoDisponibleException;
import pe.edu.cacaoselva.application.port.LoteQueryPort;

public class HttpLoteQueryAdapter implements LoteQueryPort {

    private static final Logger log = LoggerFactory.getLogger(HttpLoteQueryAdapter.class);
    private static final String RECURSO_LOTES = "/lotes";
    private static final int CODIGO_OK = 200;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final Duration timeout;

    public HttpLoteQueryAdapter(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<LoteDto> obtenerLotes() {
        HttpResponse<String> response = enviar(peticion());
        validarRespuesta(response);
        return leerLotes(response.body());
    }

    private HttpRequest peticion() {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + RECURSO_LOTES))
                .timeout(timeout)
                .GET()
                .build();
    }

    private HttpResponse<String> enviar(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ApiNoDisponibleException("No se pudo conectar con la API", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiNoDisponibleException("La consulta a la API fue interrumpida", e);
        }
    }

    private void validarRespuesta(HttpResponse<String> response) {
        if (response.statusCode() != CODIGO_OK) {
            log.warn("La API respondio con estado {}", response.statusCode());
            throw new ApiNoDisponibleException(
                    "La API respondio con estado " + response.statusCode());
        }
    }

    private List<LoteDto> leerLotes(String cuerpo) {
        try {
            LoteDto[] lotes = objectMapper.readValue(cuerpo, LoteDto[].class);
            return List.of(lotes);
        } catch (IOException e) {
            throw new ApiNoDisponibleException("La respuesta de la API no tiene el formato esperado", e);
        }
    }
}
