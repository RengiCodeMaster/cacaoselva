package pe.edu.cacaoselva.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.application.usecase.ActualizarLoteUseCase;
import pe.edu.cacaoselva.application.usecase.BuscarLotePorIdUseCase;
import pe.edu.cacaoselva.application.usecase.ContarLotesPendientesUseCase;
import pe.edu.cacaoselva.application.usecase.CrearLoteUseCase;
import pe.edu.cacaoselva.application.usecase.EliminarLoteUseCase;
import pe.edu.cacaoselva.application.usecase.ListarLotesUseCase;

@Configuration
public class ApplicationConfig {

    @Bean
    public ListarLotesUseCase listarLotesUseCase(LoteRepository loteRepository) {
        return new ListarLotesUseCase(loteRepository);
    }

    @Bean
    public BuscarLotePorIdUseCase buscarLotePorIdUseCase(LoteRepository loteRepository) {
        return new BuscarLotePorIdUseCase(loteRepository);
    }

    @Bean
    public ContarLotesPendientesUseCase contarLotesPendientesUseCase(LoteRepository loteRepository) {
        return new ContarLotesPendientesUseCase(loteRepository);
    }

    @Bean
    public CrearLoteUseCase crearLoteUseCase(LoteRepository loteRepository) {
        return new CrearLoteUseCase(loteRepository);
    }

    @Bean
    public ActualizarLoteUseCase actualizarLoteUseCase(LoteRepository loteRepository) {
        return new ActualizarLoteUseCase(loteRepository);
    }

    @Bean
    public EliminarLoteUseCase eliminarLoteUseCase(LoteRepository loteRepository) {
        return new EliminarLoteUseCase(loteRepository);
    }
}
