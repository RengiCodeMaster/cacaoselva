package pe.edu.cacaoselva.application.usecase;

import java.util.List;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.Lote;

public class ListarLotesUseCase {

    private final LoteRepository loteRepository;

    public ListarLotesUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public List<Lote> listar() {
        return loteRepository.findAll();
    }
}
