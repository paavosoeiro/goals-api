package com.bemba.goalsapi.repository;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bemba.goalsapi.entities.Entry;

@RepositoryRestResource
@NamedQueries({
		@NamedQuery(name = "EntryRepository.findByGoalId", query = "SELECT entry FROM Entry entry WHERE entry.goal.id = :goalId") })
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {

	List<Entry> findByGoalId(@Param("goalId") Long goalId);
	
	Page<Entry> findByGoalId(@Param("goalId") Long goalId, Pageable pageable);
}
