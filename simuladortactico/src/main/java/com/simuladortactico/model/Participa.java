package com.simuladortactico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alineacion", nullable = false)
    private Alineacion alineacion;

    @Column(nullable = false)
    private Integer posX;

    @Column(nullable = false)
    private Integer posY;

    @Column(name = "es_equipo_contrario", nullable = false)
    private Boolean esEquipoContrario = false;
}
