package pe.edu.cacaoselva.infrastructure.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.application.exception.ApiNoDisponibleException;
import pe.edu.cacaoselva.application.port.LoteCommandPort;

public class HttpLoteCommandAdapter implements LoteCommandPort {

    private static final String RECURSO_LOTES = "/lotes";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final Duration timeout;

    public HttpLoteCommandAdapter(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LoteDto crear(DatosLote datos) {
        HttpRequest request = requestBuilder(RECURSO_LOTES)
                .POST(HttpRequest.BodyPublishers.ofString(escribir(datos)))
                .build();
        return ejecutarConRespuesta(request, 201);
    }

    @Override
    public LoteDto actualizar(Integer id, DatosLote datos) {
        HttpRequest request = requestBuilder(RECURSO_LOTES + "/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(escribir(datos)))
                .build();
        return ejecutarConRespuesta(request, 200);
    }

    @Override
    public void eliminar(Integer id) {
        HttpRequest request = requestBuilder(RECURSO_LOTES + "/" + id)
                .DELETE()
                .build();
        HttpResponse<String> response = enviar(request);
        validarEstado(response, 204);
    }

    private HttpRequest.Builder requestBuilder(String recurso) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + recurso))
                .timeout(timeout)
                .header("Content-Type", "application/json");
    }

    private LoteDto ejecutarConRespuesta(HttpRequest request, int estadoEsperado) {
        HttpResponse<String> response = enviar(request);
        validarEstado(response, estadoEsperado);
        try {
            return objectMapper.readValue(response.body(), LoteDto.class);
        } catch (JsonProcessingException e) {
            throw new ApiNoDisponibleException("La respuesta de la API no tiene el formato esperado", e);
        }
    }

    private String escribir(DatosLote datos) {
        try {
            return objectMapper.writeValueAsString(datos);
        } catch (JsonProcessingException e) {
            throw new ApiNoDisponibleException("No se pudieron preparar los datos del lote", e);
        }
    }

    private HttpResponse<String> enviar(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ApiNoDisponibleException("No se pudo conectar con la API", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiNoDisponibleException("La operacion con la API fue interrumpida", e);
        }
    }

    private void validarEstado(HttpResponse<String> response, int estadoEsperado) {
        if (response.statusCode() == estadoEsperado) {
            return;
        }
        throw new ApiNoDisponibleException(leerMensajeError(response));
    }

    private String leerMensajeError(HttpResponse<String> response) {
        try {
            JsonNode cuerpo = objectMapper.readTree(response.body());
            String mensaje = cuerpo.path("message").asText();
            if (!mensaje.isBlank()) {
                return mensaje;
            }
        } catch (JsonProcessingException ignored) {
            // Se conserva el estado HTTP cuando la API no devuelve el error esperado.
        }
        return "La API respondio con estado " + response.statusCode();
    }
}
