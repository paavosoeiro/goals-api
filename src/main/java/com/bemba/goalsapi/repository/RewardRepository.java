package com.bemba.goalsapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bemba.goalsapi.entities.Reward;

@RepositoryRestResource
public interface RewardRepository extends PagingAndSortingRepository<Reward, Long> {

}
