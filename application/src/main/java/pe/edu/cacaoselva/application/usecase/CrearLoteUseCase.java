package pe.edu.cacaoselva.application.usecase;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.application.validation.LoteValidator;
import pe.edu.cacaoselva.domain.model.Lote;

public class CrearLoteUseCase {

    private final LoteRepository loteRepository;

    public CrearLoteUseCase(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public Lote crear(DatosLote datos) {
        LoteValidator.validarDatos(datos);
        Lote nuevo = new Lote(null, datos.socio().trim(), datos.pesoKg(), datos.estado());
        return loteRepository.save(nuevo);
    }
}
