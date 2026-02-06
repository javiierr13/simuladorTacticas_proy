package com.simuladortactico.controller;

import com.simuladortactico.model.Jugador;
import com.simuladortactico.service.JugadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class JugadorController {
    private final JugadorService jugadorService;

    @GetMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<List<Jugador>> obtenerJugadores(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(jugadorService.obtenerJugadoresPorEntrenador(entrenadorId));
    }

    @PostMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<Jugador> crearJugador(@PathVariable Long entrenadorId, @RequestBody Jugador jugador) {
        return ResponseEntity.ok(jugadorService.crearJugador(entrenadorId, jugador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizarJugador(@PathVariable Long id, @RequestBody Jugador jugador) {
        try {
            return ResponseEntity.ok(jugadorService.actualizarJugador(id, jugador));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJugador(@PathVariable Long id) {
        jugadorService.eliminarJugador(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entrenador/{entrenadorId}/count")
    public ResponseEntity<Integer> contarJugadores(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(jugadorService.contarJugadoresPorEntrenador(entrenadorId));
    }
}
