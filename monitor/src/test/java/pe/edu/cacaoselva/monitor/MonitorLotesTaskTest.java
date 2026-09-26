package pe.edu.cacaoselva.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.application.exception.ApiNoDisponibleException;
import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.domain.model.EstadoLote;

class MonitorLotesTaskTest {

    @Test
    void debeInformarSoloCuandoCambiaElResumen() {
        AtomicReference<List<LoteDto>> respuesta = new AtomicReference<>(List.of(
                lote(1, EstadoLote.PENDIENTE), lote(2, EstadoLote.LIQUIDADO)));
        List<String> mensajes = new ArrayList<>();
        MonitorLotesTask task = new MonitorLotesTask(respuesta::get, mensajes::add, mensaje -> { });

        task.run();
        task.run();
        respuesta.set(List.of(lote(1, EstadoLote.LIQUIDADO), lote(2, EstadoLote.LIQUIDADO)));
        task.run();

        assertEquals(2, mensajes.size());
        assertTrue(mensajes.get(0).contains("pendientes=1"));
        assertTrue(mensajes.get(1).contains("pendientes=0"));
    }

    @Test
    void debeReportarUnaFallaYLaRecuperacion() {
        AtomicBoolean disponible = new AtomicBoolean(false);
        LoteQueryPort puerto = () -> {
            if (!disponible.get()) {
                throw new ApiNoDisponibleException("sin conexion");
            }
            return List.of(lote(1, EstadoLote.PENDIENTE));
        };
        List<String> info = new ArrayList<>();
        List<String> avisos = new ArrayList<>();
        MonitorLotesTask task = new MonitorLotesTask(puerto, info::add, avisos::add);

        task.run();
        task.run();
        disponible.set(true);
        task.run();

        assertEquals(1, avisos.size());
        assertEquals(2, info.size());
        assertTrue(info.get(0).contains("restablecida"));
        assertTrue(info.get(1).contains("total=1"));
    }

    private LoteDto lote(int id, EstadoLote estado) {
        return new LoteDto(id, "Socio " + id, new BigDecimal("10.00"), estado);
    }
}
