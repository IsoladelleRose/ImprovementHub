package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    // Find ideas by email
    List<Idea> findByEmail(String email);

    // Find ideas that want help
    List<Idea> findByWantsHelp(Boolean wantsHelp);

    // Find ideas by core concept (case insensitive)
    List<Idea> findByCoreConceptContainingIgnoreCase(String keyword);
}