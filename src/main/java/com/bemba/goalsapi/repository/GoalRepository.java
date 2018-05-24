package com.bemba.goalsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bemba.goalsapi.entities.Goal;

@RepositoryRestResource
public interface GoalRepository extends JpaRepository<Goal, Long>{

}
