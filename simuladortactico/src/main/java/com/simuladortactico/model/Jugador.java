package com.simuladortactico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "jugador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Integer dorsal;

    @Column(nullable = false, length = 50)
    private String posicion;

    @Enumerated(EnumType.STRING)
    @Column(name = "pierna_dominante")
    private PiernaDominante piernaDominante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entrenador", nullable = false)
    private Entrenador entrenador;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participa> participaciones;

    public enum PiernaDominante {
        IZQUIERDA, DERECHA, AMBAS
    }
}
