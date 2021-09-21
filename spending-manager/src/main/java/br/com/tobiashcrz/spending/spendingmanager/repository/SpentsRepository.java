package br.com.tobiashcrz.spending.spendingmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tobiashcrz.spending.spendingmanager.entity.SpentsEntity;

@Repository
public interface SpentsRepository extends JpaRepository<SpentsEntity, Integer> {
    @EntityGraph(attributePaths = "tags")
    List<SpentsEntity> findAll();

    @EntityGraph(attributePaths = "tags")
    Optional<SpentsEntity> findById(Integer id);
}
