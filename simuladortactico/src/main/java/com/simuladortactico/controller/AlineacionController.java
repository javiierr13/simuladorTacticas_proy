package com.simuladortactico.controller;

import com.simuladortactico.model.Alineacion;
import com.simuladortactico.model.Participa;
import com.simuladortactico.service.AlineacionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alineaciones")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AlineacionController {
    private final AlineacionService alineacionService;

    @GetMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<List<Alineacion>> obtenerAlineaciones(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(alineacionService.obtenerAlineacionesPorEntrenador(entrenadorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alineacion> obtenerAlineacion(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(alineacionService.obtenerAlineacionConParticipaciones(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/participaciones")
    public ResponseEntity<List<Participa>> obtenerParticipaciones(@PathVariable Long id) {
        return ResponseEntity.ok(alineacionService.obtenerParticipacionesPorAlineacion(id));
    }

    @PostMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<?> crearAlineacion(@PathVariable Long entrenadorId, @RequestBody AlineacionRequest request) {
        try {
            Alineacion alineacion = new Alineacion();
            alineacion.setTipoFormacion(request.getTipoFormacion());
            Alineacion creada = alineacionService.crearAlineacion(entrenadorId, alineacion, request.getParticipaciones());
            return ResponseEntity.ok(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAlineacion(@PathVariable Long id, @RequestBody AlineacionRequest request) {
        try {
            Alineacion alineacion = new Alineacion();
            alineacion.setTipoFormacion(request.getTipoFormacion());
            Alineacion actualizada = alineacionService.actualizarAlineacion(id, alineacion, request.getParticipaciones());
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlineacion(@PathVariable Long id) {
        alineacionService.eliminarAlineacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entrenador/{entrenadorId}/validar/{tipoFormacion}")
    public ResponseEntity<Map<String, Object>> validarJugadores(@PathVariable Long entrenadorId, @PathVariable String tipoFormacion) {
        boolean tieneSuficientes = alineacionService.tieneJugadoresSuficientes(entrenadorId, tipoFormacion);
        Map<String, Object> response = new HashMap<>();
        response.put("tieneSuficientes", tieneSuficientes);
        if (!tieneSuficientes) {
            int necesarios = tipoFormacion.equals("FUTBOL_7") ? 7 : 11;
            response.put("mensaje", "No tiene jugadores suficientes. Se necesitan al menos " + necesarios + " jugadores.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/formacion-contraria/{tipoFormacion}")
    public ResponseEntity<List<AlineacionService.ParticipacionDTO>> obtenerFormacionContraria(@PathVariable String tipoFormacion) {
        return ResponseEntity.ok(alineacionService.generarFormacionContraria(tipoFormacion));
    }

    @GetMapping("/formacion-contraria/{tipoFormacion}/{opcion}")
    public ResponseEntity<List<AlineacionService.ParticipacionDTO>> obtenerFormacionContrariaAlternativa(
            @PathVariable String tipoFormacion, @PathVariable int opcion) {
        return ResponseEntity.ok(alineacionService.generarFormacionContrariaAlternativa(tipoFormacion, opcion));
    }

    @Data
    static class AlineacionRequest {
        private String tipoFormacion;
        private List<AlineacionService.ParticipacionDTO> participaciones;
    }
}
