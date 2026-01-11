package com.simuladortactico.persistence.entities;

import java.time.LocalDate;

import com.simuladortactico.persistence.entities.enums.TipoFutbol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alineacion")
@Getter
@Setter
@NoArgsConstructor
public class Alineacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 20, nullable = false)
	private TipoFutbol tipoFutbol;

	@Column(nullable = false)
	private LocalDate fechaCreacion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;
}