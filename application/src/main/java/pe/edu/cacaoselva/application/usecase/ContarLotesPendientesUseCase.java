package pe.edu.cacaoselva.application.usecase;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

public class ContarLotesPendientesUseCase {

    private final LoteRepository loteRepository;

    public ContarLotesPendientesUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public long contar() {
        return loteRepository.findAll().stream()
                .filter(this::estaPendiente)
                .count();
    }

    private boolean estaPendiente(Lote lote) {
        return lote.estado() == EstadoLote.PENDIENTE;
    }
}
