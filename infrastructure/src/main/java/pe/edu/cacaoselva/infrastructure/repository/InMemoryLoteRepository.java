package pe.edu.cacaoselva.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

public class InMemoryLoteRepository implements LoteRepository {

    private final List<Lote> lotes;

    public InMemoryLoteRepository() {
        this.lotes = new ArrayList<>(List.of(
                new Lote(1, "Ana", new BigDecimal("120.5"), EstadoLote.PENDIENTE),
                new Lote(2, "Luis", new BigDecimal("80"), EstadoLote.LIQUIDADO),
                new Lote(3, "Rosa", new BigDecimal("95.25"), EstadoLote.PENDIENTE)));
    }

    @Override
    public List<Lote> findAll() {
        return List.copyOf(lotes);
    }

    @Override
    public Optional<Lote> findById(Integer id) {
        return lotes.stream()
                .filter(lote -> lote.id().equals(id))
                .findFirst();
    }

    @Override
    public Lote save(Lote lote) {
        Integer id = lote.id() != null
                ? lote.id()
                : lotes.stream().mapToInt(Lote::id).max().orElse(0) + 1;
        Lote guardado = new Lote(id, lote.socio(), lote.pesoKg(), lote.estado());
        lotes.removeIf(existente -> existente.id().equals(id));
        lotes.add(guardado);
        return guardado;
    }

    @Override
    public void deleteById(Integer id) {
        lotes.removeIf(lote -> lote.id().equals(id));
    }

    @Override
    public long count() {
        return lotes.size();
    }
}
