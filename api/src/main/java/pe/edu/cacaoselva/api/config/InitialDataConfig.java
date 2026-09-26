package pe.edu.cacaoselva.api.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

@Configuration
public class InitialDataConfig {

    @Bean
    public ApplicationRunner cargarDatosIniciales(LoteRepository loteRepository) {
        return arguments -> {
            if (loteRepository.count() > 0) {
                return;
            }

            datosIniciales().forEach(loteRepository::save);
        };
    }

    private List<Lote> datosIniciales() {
        return List.of(
                lote("Ana Torres", "120.50", EstadoLote.PENDIENTE),
                lote("Luis Mendoza", "80.00", EstadoLote.LIQUIDADO),
                lote("Rosa Quispe", "95.25", EstadoLote.PENDIENTE),
                lote("Carlos Rojas", "143.75", EstadoLote.PENDIENTE),
                lote("Mariela Sanchez", "67.40", EstadoLote.LIQUIDADO),
                lote("Jose Paredes", "110.20", EstadoLote.PENDIENTE),
                lote("Elena Huaman", "89.60", EstadoLote.LIQUIDADO),
                lote("Miguel Salazar", "156.00", EstadoLote.PENDIENTE),
                lote("Patricia Flores", "72.35", EstadoLote.PENDIENTE),
                lote("Victor Castillo", "101.90", EstadoLote.LIQUIDADO));
    }

    private Lote lote(String socio, String pesoKg, EstadoLote estado) {
        return new Lote(null, socio, new BigDecimal(pesoKg), estado);
    }
}
