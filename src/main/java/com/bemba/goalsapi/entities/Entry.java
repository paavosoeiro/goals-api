package com.bemba.goalsapi.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "entry")
public class Entry implements Serializable {

	private static final long serialVersionUID = 7025414693033020164L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Long hours;

	@Column
	private LocalDate date;

	@ManyToOne
	@JsonManagedReference
	@Cascade({ CascadeType.SAVE_UPDATE })
	private Goal goal;

	@PrePersist
	private void prePersist() {
		date = LocalDate.now();
	}

}
