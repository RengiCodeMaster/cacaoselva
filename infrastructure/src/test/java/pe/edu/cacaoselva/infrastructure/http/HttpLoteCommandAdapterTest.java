package pe.edu.cacaoselva.infrastructure.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.domain.model.EstadoLote;

class HttpLoteCommandAdapterTest {

    private HttpServer server;
    private HttpLoteCommandAdapter adapter;
    private final AtomicBoolean eliminado = new AtomicBoolean();

    @BeforeEach
    void iniciarServidor() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/lotes", this::responder);
        server.start();
        String baseUrl = "http://localhost:" + server.getAddress().getPort();
        adapter = new HttpLoteCommandAdapter(baseUrl, Duration.ofSeconds(2));
    }

    @AfterEach
    void detenerServidor() {
        server.stop(0);
    }

    @Test
    void debeCrearActualizarYEliminarMedianteHttp() {
        DatosLote datos = new DatosLote("Socio HTTP", new java.math.BigDecimal("33.50"),
                EstadoLote.PENDIENTE);

        LoteDto creado = adapter.crear(datos);
        LoteDto actualizado = adapter.actualizar(creado.id(),
                new DatosLote("Socio actualizado", new java.math.BigDecimal("40.00"),
                        EstadoLote.LIQUIDADO));
        adapter.eliminar(creado.id());

        assertEquals(11, creado.id());
        assertEquals("Socio actualizado", actualizado.socio());
        assertEquals(EstadoLote.LIQUIDADO, actualizado.estado());
        assertTrue(eliminado.get());
    }

    private void responder(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String cuerpo;
        int estado;
        if ("POST".equals(metodo)) {
            estado = 201;
            cuerpo = "{\"id\":11,\"socio\":\"Socio HTTP\",\"pesoKg\":33.50,\"estado\":\"PENDIENTE\"}";
        } else if ("PUT".equals(metodo)) {
            estado = 200;
            cuerpo = "{\"id\":11,\"socio\":\"Socio actualizado\",\"pesoKg\":40.00,\"estado\":\"LIQUIDADO\"}";
        } else {
            estado = 204;
            cuerpo = "";
            eliminado.set(true);
        }
        byte[] respuesta = cuerpo.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(estado, estado == 204 ? -1 : respuesta.length);
        if (respuesta.length > 0) {
            exchange.getResponseBody().write(respuesta);
        }
        exchange.close();
    }
}
