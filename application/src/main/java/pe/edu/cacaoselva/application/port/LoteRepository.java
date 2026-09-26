package pe.edu.cacaoselva.application.port;

import java.util.List;
import java.util.Optional;

import pe.edu.cacaoselva.domain.model.Lote;

public interface LoteRepository {

    List<Lote> findAll();

    Optional<Lote> findById(Integer id);

    Lote save(Lote lote);

    void deleteById(Integer id);

    long count();
}
