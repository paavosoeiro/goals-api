package com.bemba.goalsapi.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Person implements Serializable {

	private static final long serialVersionUID = 2316922936433646399L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "person")
	private List<Goal> goals;

	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Reward.class)
	private List<Reward> rewards;

}
