package com.bemba.goalsapi.repository;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.bemba.goalsapi.entities.Goal;

@RepositoryRestResource
@NamedQueries({
		@NamedQuery(name = "GoalRepository.findByPersonId", query = "SELECT goal FROM Goal entry WHERE goal.person.id = :personId") })
public interface GoalRepository extends JpaRepository<Goal, Long> {

	List<Goal> findByPersonId(@Param("personId") Long personId);

	@RestResource(exported = false)
	Page<Goal> findByPersonId(@Param("personId") Long personId, Pageable pageable);

}
