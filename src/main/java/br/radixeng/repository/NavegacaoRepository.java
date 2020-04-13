package br.radixeng.repository;

import br.radixeng.model.NavegacaoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NavegacaoRepository extends JpaRepository<NavegacaoInfo, Long> {
    public Optional<NavegacaoInfo> findById(Long id);
}
