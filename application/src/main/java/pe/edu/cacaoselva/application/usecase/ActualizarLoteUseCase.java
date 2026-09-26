package pe.edu.cacaoselva.application.usecase;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.application.validation.LoteValidator;
import pe.edu.cacaoselva.domain.model.Lote;

public class ActualizarLoteUseCase {

    private final LoteRepository loteRepository;

    public ActualizarLoteUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public Lote actualizar(Integer id, DatosLote datos) {
        LoteValidator.validarId(id);
        LoteValidator.validarDatos(datos);
        loteRepository.findById(id)
                .orElseThrow(() -> new LoteNoEncontradoException(id));

        Lote actualizado = new Lote(id, datos.socio().trim(), datos.pesoKg(), datos.estado());
        return loteRepository.save(actualizado);
    }
}
