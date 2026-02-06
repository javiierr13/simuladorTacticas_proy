package com.simuladortactico.service;

import com.simuladortactico.model.Entrenador;
import com.simuladortactico.model.Jugador;
import com.simuladortactico.repository.EntrenadorRepository;
import com.simuladortactico.repository.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JugadorService {
    private final JugadorRepository jugadorRepository;
    private final EntrenadorRepository entrenadorRepository;

    public List<Jugador> obtenerJugadoresPorEntrenador(Long entrenadorId) {
        return jugadorRepository.findByEntrenadorId(entrenadorId);
    }

    @Transactional
    public Jugador crearJugador(Long entrenadorId, Jugador jugador) {
        Entrenador entrenador = entrenadorRepository.findById(entrenadorId)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        jugador.setEntrenador(entrenador);
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public Jugador actualizarJugador(Long id, Jugador jugadorActualizado) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        jugador.setNombre(jugadorActualizado.getNombre());
        jugador.setDorsal(jugadorActualizado.getDorsal());
        jugador.setPosicion(jugadorActualizado.getPosicion());
        jugador.setPiernaDominante(jugadorActualizado.getPiernaDominante());
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public void eliminarJugador(Long id) {
        jugadorRepository.deleteById(id);
    }

    public int contarJugadoresPorEntrenador(Long entrenadorId) {
        return jugadorRepository.findByEntrenadorId(entrenadorId).size();
    }
}
