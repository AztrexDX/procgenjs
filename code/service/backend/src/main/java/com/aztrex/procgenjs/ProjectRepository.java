package com.aztrex.procgenjs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom query example (Spring generates SQL automatically)
    Project findByName(String name);
}