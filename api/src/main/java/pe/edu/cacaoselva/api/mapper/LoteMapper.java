package pe.edu.cacaoselva.api.mapper;

import pe.edu.cacaoselva.api.dto.LoteRequest;
import pe.edu.cacaoselva.api.dto.LoteResponse;
import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.domain.model.Lote;

public final class LoteMapper {

    private LoteMapper() {
    }

    public static LoteResponse toResponse(Lote lote) {
        return new LoteResponse(
                lote.id(),
                lote.socio(),
                lote.pesoKg(),
                lote.estado().name());
    }

    public static DatosLote toDatos(LoteRequest request) {
        if (request == null) {
            return null;
        }
        return new DatosLote(request.socio(), request.pesoKg(), request.estado());
    }
}
