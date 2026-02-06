package com.simuladortactico.persistence.entities;

import com.simuladortactico.persistence.entities.enums.PiernaDominante;
import com.simuladortactico.persistence.entities.enums.Posicion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jugador")
@Getter
@Setter
@NoArgsConstructor
public class Jugador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 50, nullable = false)
	private String nombre;

	@Column(nullable = false)
	private int dorsal;

	@Column(length = 20, nullable = false)
	private Posicion posicion;

	@Column(length = 15, nullable = false)
	private PiernaDominante piernaDominante;
}
