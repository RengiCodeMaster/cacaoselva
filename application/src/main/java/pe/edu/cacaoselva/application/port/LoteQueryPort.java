package pe.edu.cacaoselva.application.port;

import java.util.List;

import pe.edu.cacaoselva.application.dto.LoteDto;

public interface LoteQueryPort {

    List<LoteDto> obtenerLotes();
}
