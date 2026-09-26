package pe.edu.cacaoselva.infrastructure.config;

import java.time.Duration;

public final class ApiHttpConfig {

    public static final String BASE_URL = "http://localhost:5080";
    public static final Duration TIMEOUT = Duration.ofSeconds(5);

    private ApiHttpConfig() {
    }
}
