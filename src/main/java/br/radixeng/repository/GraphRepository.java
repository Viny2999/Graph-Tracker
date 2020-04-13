package br.radixeng.repository;

import br.radixeng.model.GraphArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GraphRepository extends JpaRepository<GraphArray, Long> {
    public Optional<GraphArray> findById(Long id);
}
