package pe.edu.cacaoselva.application.port;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.dto.LoteDto;

public interface LoteCommandPort {

    LoteDto crear(DatosLote datos);

    LoteDto actualizar(Integer id, DatosLote datos);

    void eliminar(Integer id);
}
