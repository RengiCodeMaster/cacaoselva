package pe.edu.cacaoselva.application.usecase;

import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.application.validation.LoteValidator;
import pe.edu.cacaoselva.domain.model.Lote;

public class BuscarLotePorIdUseCase {

    private final LoteRepository loteRepository;

    public BuscarLotePorIdUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public Lote buscar(Integer id) {
        LoteValidator.validarId(id);
        return loteRepository.findById(id)
                .orElseThrow(() -> new LoteNoEncontradoException(id));
    }
}
