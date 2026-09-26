package pe.edu.cacaoselva.monitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.monitor.config.MonitorConfig;

public class MonitorApplication {

    private static final Logger log = LoggerFactory.getLogger(MonitorApplication.class);
    public static void main(String[] args) {
        LoteQueryPort loteQueryPort = MonitorConfig.loteQueryPort();
        long intervaloSegundos = MonitorConfig.intervalo().toSeconds();
        ScheduledExecutorService planificador = Executors.newSingleThreadScheduledExecutor();
        planificador.scheduleWithFixedDelay(
                new MonitorLotesTask(loteQueryPort),
                0,
                intervaloSegundos,
                TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> cerrar(planificador), "cacaoselva-monitor-shutdown"));
        log.info("Monitor iniciado. Intervalo: {} segundos", intervaloSegundos);
    }

    private static void cerrar(ScheduledExecutorService planificador) {
        log.info("Deteniendo monitor");
        planificador.shutdown();
        try {
            if (!planificador.awaitTermination(5, TimeUnit.SECONDS)) {
                planificador.shutdownNow();
            }
        } catch (InterruptedException excepcion) {
            planificador.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
