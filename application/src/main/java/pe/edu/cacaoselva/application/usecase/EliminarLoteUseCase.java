package pe.edu.cacaoselva.application.usecase;

import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.application.validation.LoteValidator;

public class EliminarLoteUseCase {

    private final LoteRepository loteRepository;

    public EliminarLoteUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public void eliminar(Integer id) {
        LoteValidator.validarId(id);
        loteRepository.findById(id)
                .orElseThrow(() -> new LoteNoEncontradoException(id));
        loteRepository.deleteById(id);
    }
}
