package pe.edu.cacaoselva.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.Lote;
import pe.edu.cacaoselva.infrastructure.persistence.entity.LoteEntity;

@Primary
@Repository
@Transactional(readOnly = true)
public class JpaLoteRepositoryAdapter implements LoteRepository {

    private final SpringDataLoteRepository repository;

    public JpaLoteRepositoryAdapter(SpringDataLoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Lote> findAll() {
        return repository.findAllByOrderByIdAsc().stream()
                .map(JpaLoteRepositoryAdapter::toDomain)
                .toList();
    }

    @Override
    public Optional<Lote> findById(Integer id) {
        return repository.findById(id).map(JpaLoteRepositoryAdapter::toDomain);
    }

    @Override
    @Transactional
    public Lote save(Lote lote) {
        LoteEntity entity = new LoteEntity(lote.id(), lote.socio(), lote.pesoKg(), lote.estado());
        return toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    private static Lote toDomain(LoteEntity entity) {
        return new Lote(entity.getId(), entity.getSocio(), entity.getPesoKg(), entity.getEstado());
    }
}
