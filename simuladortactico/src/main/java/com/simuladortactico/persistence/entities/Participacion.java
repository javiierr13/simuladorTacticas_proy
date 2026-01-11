package com.simuladortactico.persistence.entities;

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
@Table(name = "participacion")
@Getter
@Setter
@NoArgsConstructor
public class Participacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "jugador_id")
	private Jugador jugador;

	@ManyToOne(optional = false)
	@JoinColumn(name = "alineacion_id")
	private Alineacion alineacion;

	@Column(nullable = false)
	private int posX;

	@Column(nullable = false)
	private int posY;
}