package br.radixeng.repository;

import br.radixeng.model.GraphList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GraphRepository extends JpaRepository<GraphList, Long> {
    public Optional<GraphList> findById(Long id);
}
