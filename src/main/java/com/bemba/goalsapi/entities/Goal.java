package com.bemba.goalsapi.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.bemba.goalsapi.enums.GoalStatusEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "goal")
public class Goal implements Serializable {

	private static final long serialVersionUID = -4081110271284640050L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	@Column
	private String description;

	@Column
	private Long totalHours;

	@Column
	private LocalDate startDate;

	@Column
	private LocalDate endDate;

	@Column
	private LocalDate deadline;

	@Column
	private Long remainingHours;

	@Enumerated(EnumType.STRING)
	@Column
	private GoalStatusEnum status;

	@OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonBackReference
	private List<Entry> entries;

	@OneToOne(cascade = javax.persistence.CascadeType.PERSIST)
	@Cascade({ CascadeType.SAVE_UPDATE })
	private Reward reward;

	@ManyToOne
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonManagedReference
	private Person person;

	@PrePersist
	private void prePersist() {
		status = GoalStatusEnum.OPENED;
		startDate = LocalDate.now();
	}

	@JsonIgnore
	public Boolean isOverdue() {
		return LocalDate.now().isAfter(this.deadline);
	}

	@JsonIgnore
	public Boolean isFinished() {
		return remainingHours <= 0;
	}

}
