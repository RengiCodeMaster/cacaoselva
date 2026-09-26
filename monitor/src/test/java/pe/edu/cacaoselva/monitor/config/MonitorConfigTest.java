package pe.edu.cacaoselva.monitor.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class MonitorConfigTest {

    @Test
    void debeAceptarIntervaloPositivo() {
        assertEquals(Duration.ofSeconds(30), MonitorConfig.parsearIntervalo("30"));
    }

    @Test
    void debeUsarDiezSegundosCuandoElValorEsInvalido() {
        assertEquals(Duration.ofSeconds(10), MonitorConfig.parsearIntervalo("texto"));
        assertEquals(Duration.ofSeconds(10), MonitorConfig.parsearIntervalo("0"));
        assertEquals(Duration.ofSeconds(10), MonitorConfig.parsearIntervalo(null));
    }
}
