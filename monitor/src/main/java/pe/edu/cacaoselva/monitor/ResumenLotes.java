package pe.edu.cacaoselva.monitor;

import java.util.List;

import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.domain.model.EstadoLote;

public record ResumenLotes(int total, long pendientes, long liquidados) {

    public static ResumenLotes desde(List<LoteDto> lotes) {
        long pendientes = lotes.stream()
                .filter(lote -> lote.estado() == EstadoLote.PENDIENTE)
                .count();
        return new ResumenLotes(lotes.size(), pendientes, lotes.size() - pendientes);
    }
}
