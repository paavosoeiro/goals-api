package com.bemba.goalsapi.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Person implements Serializable {

	private static final long serialVersionUID = 2316922936433646399L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "person")
	@JsonBackReference
	@Singular
	private List<Goal> goals;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Reward.class)
	@Singular
	private List<Reward> rewards;

//	public List<Reward> getRewards() {
//		if (this.rewards == null) {
//			this.rewards = new ArrayList<>();
//		}
//
//		return this.rewards;
//	}

}
