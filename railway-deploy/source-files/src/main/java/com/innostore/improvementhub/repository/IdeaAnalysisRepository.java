package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.IdeaAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdeaAnalysisRepository extends JpaRepository<IdeaAnalysis, Long> {

    // Find analysis by idea ID
    Optional<IdeaAnalysis> findByIdeaId(Long ideaId);

    // Find all analyses for ideas (useful for partner matching)
    List<IdeaAnalysis> findAll();
}
