package pe.edu.cacaoselva.desktop.adapter;

import java.util.List;

import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.desktop.dto.LoteVista;

public final class LoteVistaAdapter {

    private LoteVistaAdapter() {
    }

    public static List<LoteVista> toVista(List<LoteDto> lotes) {
        return lotes.stream()
                .map(LoteVistaAdapter::toVista)
                .toList();
    }

    public static LoteVista toVista(LoteDto lote) {
        return new LoteVista(
                lote.id(),
                lote.socio(),
                lote.pesoKg().toPlainString(),
                lote.estado().name());
    }
}
