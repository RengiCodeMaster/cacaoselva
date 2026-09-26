package pe.edu.cacaoselva.desktop.config;

import pe.edu.cacaoselva.application.port.LoteCommandPort;
import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.infrastructure.config.ApiHttpConfig;
import pe.edu.cacaoselva.infrastructure.http.HttpLoteCommandAdapter;
import pe.edu.cacaoselva.infrastructure.http.HttpLoteQueryAdapter;

public final class DesktopConfig {

    private DesktopConfig() {
    }

    public static LoteQueryPort loteQueryPort() {
        return new HttpLoteQueryAdapter(ApiHttpConfig.BASE_URL, ApiHttpConfig.TIMEOUT);
    }

    public static LoteCommandPort loteCommandPort() {
        return new HttpLoteCommandAdapter(ApiHttpConfig.BASE_URL, ApiHttpConfig.TIMEOUT);
    }
}
