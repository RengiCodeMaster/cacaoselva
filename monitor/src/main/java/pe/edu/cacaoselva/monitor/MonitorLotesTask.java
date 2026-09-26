package pe.edu.cacaoselva.monitor;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.edu.cacaoselva.application.exception.ApiNoDisponibleException;
import pe.edu.cacaoselva.application.port.LoteQueryPort;

public class MonitorLotesTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(MonitorLotesTask.class);

    private final LoteQueryPort loteQueryPort;
    private final Consumer<String> infoLogger;
    private final Consumer<String> warnLogger;

    private ResumenLotes ultimoResumen;
    private Boolean apiDisponible;

    public MonitorLotesTask(LoteQueryPort loteQueryPort) {
        this(loteQueryPort, log::info, log::warn);
    }

    MonitorLotesTask(LoteQueryPort loteQueryPort,
                     Consumer<String> infoLogger,
                     Consumer<String> warnLogger) {
        this.loteQueryPort = loteQueryPort;
        this.infoLogger = infoLogger;
        this.warnLogger = warnLogger;
    }

    @Override
    public void run() {
        try {
            ResumenLotes resumen = ResumenLotes.desde(loteQueryPort.obtenerLotes());
            boolean recuperada = Boolean.FALSE.equals(apiDisponible);
            apiDisponible = true;
            if (recuperada) {
                infoLogger.accept("Conexion con la API restablecida");
            }
            if (!resumen.equals(ultimoResumen)) {
                infoLogger.accept("Lotes: total=%d, pendientes=%d, liquidados=%d".formatted(
                        resumen.total(), resumen.pendientes(), resumen.liquidados()));
                ultimoResumen = resumen;
            }
        } catch (ApiNoDisponibleException excepcion) {
            if (!Boolean.FALSE.equals(apiDisponible)) {
                warnLogger.accept("No se pudo consultar la API: " + excepcion.getMessage());
            }
            apiDisponible = false;
        } catch (RuntimeException excepcion) {
            warnLogger.accept("Falla inesperada del monitor: " + excepcion.getMessage());
        }
    }
}
