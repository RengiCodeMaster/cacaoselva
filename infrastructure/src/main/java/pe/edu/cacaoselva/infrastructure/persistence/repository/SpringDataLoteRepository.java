package pe.edu.cacaoselva.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cacaoselva.infrastructure.persistence.entity.LoteEntity;

public interface SpringDataLoteRepository extends JpaRepository<LoteEntity, Integer> {

    List<LoteEntity> findAllByOrderByIdAsc();
}
