package com.bemba.goalsapi.repository;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bemba.goalsapi.entities.Goal;

@RepositoryRestResource
@NamedQueries({
		@NamedQuery(name = "GoalRepository.findByPersonId", query = "SELECT goal FROM Goal entry WHERE goal.person.id = :personId") })
public interface GoalRepository extends PagingAndSortingRepository<Goal, Long> {

	List<Goal> findByPersonId(@Param("personId") Long personId);

//	Page<Goal> findByPersonId(@Param("personId") Long personId, Pageable pageable);

}
