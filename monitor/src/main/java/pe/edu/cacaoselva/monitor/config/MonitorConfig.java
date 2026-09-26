package pe.edu.cacaoselva.monitor.config;

import java.time.Duration;

import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.infrastructure.config.ApiHttpConfig;
import pe.edu.cacaoselva.infrastructure.http.HttpLoteQueryAdapter;

public final class MonitorConfig {

    private static final long INTERVALO_PREDETERMINADO_SEGUNDOS = 10;
    private static final String PROPIEDAD_INTERVALO = "cacaoselva.monitor.interval.seconds";
    private static final String VARIABLE_INTERVALO = "CACAOSELVA_MONITOR_INTERVAL_SECONDS";

    private MonitorConfig() {
    }

    public static LoteQueryPort loteQueryPort() {
        return new HttpLoteQueryAdapter(ApiHttpConfig.BASE_URL, ApiHttpConfig.TIMEOUT);
    }

    public static Duration intervalo() {
        String valor = System.getProperty(PROPIEDAD_INTERVALO);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(VARIABLE_INTERVALO);
        }
        return parsearIntervalo(valor);
    }

    static Duration parsearIntervalo(String valor) {
        if (valor == null || valor.isBlank()) {
            return Duration.ofSeconds(INTERVALO_PREDETERMINADO_SEGUNDOS);
        }
        try {
            long segundos = Long.parseLong(valor);
            return segundos > 0
                    ? Duration.ofSeconds(segundos)
                    : Duration.ofSeconds(INTERVALO_PREDETERMINADO_SEGUNDOS);
        } catch (NumberFormatException excepcion) {
            return Duration.ofSeconds(INTERVALO_PREDETERMINADO_SEGUNDOS);
        }
    }
}
