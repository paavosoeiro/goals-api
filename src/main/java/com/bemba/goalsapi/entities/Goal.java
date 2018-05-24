package com.bemba.goalsapi.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.bemba.goalsapi.enums.GoalStatusEnum;

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
	private Date startDate;

	@Column
	private Date endDate;

	@Column
	private Date deadline;

	@Enumerated(EnumType.STRING)
	@Column
	private GoalStatusEnum status;

	@OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Entry> entries;

	@PrePersist
	private void prePersist() {
		status = GoalStatusEnum.OPEN;
	}

}
