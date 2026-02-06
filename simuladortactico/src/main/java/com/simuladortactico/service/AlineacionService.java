package com.simuladortactico.service;

import com.simuladortactico.model.*;
import com.simuladortactico.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlineacionService {
    private final AlineacionRepository alineacionRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final JugadorRepository jugadorRepository;
    private final ParticipaRepository participaRepository;
    private final JugadorService jugadorService;

    public List<Alineacion> obtenerAlineacionesPorEntrenador(Long entrenadorId) {
        return alineacionRepository.findByEntrenadorId(entrenadorId);
    }

    @Transactional
    public Alineacion crearAlineacion(Long entrenadorId, Alineacion alineacion, List<ParticipacionDTO> participaciones) {
        Entrenador entrenador = entrenadorRepository.findById(entrenadorId)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
        alineacion.setEntrenador(entrenador);
        Alineacion saved = alineacionRepository.save(alineacion);

        for (ParticipacionDTO p : participaciones) {
            Participa participa = new Participa();
            participa.setAlineacion(saved);
            participa.setPosX(p.getPosX());
            participa.setPosY(p.getPosY());
            participa.setEsEquipoContrario(p.getEsEquipoContrario());
            
            if (!p.getEsEquipoContrario()) {
                Jugador jugador = jugadorRepository.findById(p.getJugadorId())
                        .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
                participa.setJugador(jugador);
            } else {
                // Crear jugador ficticio para equipo contrario
                Jugador jugadorContrario = new Jugador();
                jugadorContrario.setNombre(p.getNombreJugador());
                jugadorContrario.setDorsal(p.getDorsal());
                jugadorContrario.setPosicion(p.getPosicion());
                jugadorContrario.setPiernaDominante(Jugador.PiernaDominante.AMBAS);
                jugadorContrario.setEntrenador(entrenador);
                Jugador savedJugador = jugadorRepository.save(jugadorContrario);
                participa.setJugador(savedJugador);
            }
            
            participaRepository.save(participa);
        }

        return saved;
    }

    @Transactional
    public Alineacion actualizarAlineacion(Long id, Alineacion alineacionActualizada, List<ParticipacionDTO> participaciones) {
        Alineacion alineacion = alineacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alineación no encontrada"));
        
        alineacion.setTipoFormacion(alineacionActualizada.getTipoFormacion());
        
        // Eliminar participaciones existentes
        participaRepository.deleteByAlineacionId(id);
        
        // Crear nuevas participaciones
        for (ParticipacionDTO p : participaciones) {
            Participa participa = new Participa();
            participa.setAlineacion(alineacion);
            participa.setPosX(p.getPosX());
            participa.setPosY(p.getPosY());
            participa.setEsEquipoContrario(p.getEsEquipoContrario());
            
            if (!p.getEsEquipoContrario()) {
                Jugador jugador = jugadorRepository.findById(p.getJugadorId())
                        .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
                participa.setJugador(jugador);
            } else {
                Jugador jugadorContrario = new Jugador();
                jugadorContrario.setNombre(p.getNombreJugador());
                jugadorContrario.setDorsal(p.getDorsal());
                jugadorContrario.setPosicion(p.getPosicion());
                jugadorContrario.setPiernaDominante(Jugador.PiernaDominante.AMBAS);
                jugadorContrario.setEntrenador(alineacion.getEntrenador());
                Jugador savedJugador = jugadorRepository.save(jugadorContrario);
                participa.setJugador(savedJugador);
            }
            
            participaRepository.save(participa);
        }

        return alineacionRepository.save(alineacion);
    }

    @Transactional
    public void eliminarAlineacion(Long id) {
        participaRepository.deleteByAlineacionId(id);
        alineacionRepository.deleteById(id);
    }

    public Alineacion obtenerAlineacionConParticipaciones(Long id) {
        return alineacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alineación no encontrada"));
    }

    public List<Participa> obtenerParticipacionesPorAlineacion(Long alineacionId) {
        return participaRepository.findByAlineacionId(alineacionId);
    }

    public boolean tieneJugadoresSuficientes(Long entrenadorId, String tipoFormacion) {
        int jugadoresNecesarios = tipoFormacion.equals("FUTBOL_7") ? 7 : 11;
        int jugadoresDisponibles = jugadorService.contarJugadoresPorEntrenador(entrenadorId);
        return jugadoresDisponibles >= jugadoresNecesarios;
    }

    public List<ParticipacionDTO> generarFormacionContraria(String tipoFormacion) {
        List<ParticipacionDTO> formacion = new ArrayList<>();
        
        if (tipoFormacion.equals("FUTBOL_7")) {
            // Formación 1-2-3-1 (primera opción fútbol 7)
            formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 30, 30, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 70, 30, true));
            formacion.add(new ParticipacionDTO(null, "Centro 1", 4, "Centrocampista", 20, 50, true));
            formacion.add(new ParticipacionDTO(null, "Centro 2", 5, "Centrocampista", 50, 50, true));
            formacion.add(new ParticipacionDTO(null, "Centro 3", 6, "Centrocampista", 80, 50, true));
            formacion.add(new ParticipacionDTO(null, "Delantero", 7, "Delantero", 50, 80, true));
        } else if (tipoFormacion.equals("FUTBOL_11")) {
            // Formación 1-4-4-2 (primera opción fútbol 11)
            formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 20, 25, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 40, 25, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 3", 4, "Defensa", 60, 25, true));
            formacion.add(new ParticipacionDTO(null, "Defensa 4", 5, "Defensa", 80, 25, true));
            formacion.add(new ParticipacionDTO(null, "Centro 1", 6, "Centrocampista", 25, 50, true));
            formacion.add(new ParticipacionDTO(null, "Centro 2", 7, "Centrocampista", 50, 50, true));
            formacion.add(new ParticipacionDTO(null, "Centro 3", 8, "Centrocampista", 75, 50, true));
            formacion.add(new ParticipacionDTO(null, "Centro 4", 9, "Centrocampista", 50, 65, true));
            formacion.add(new ParticipacionDTO(null, "Delantero 1", 10, "Delantero", 35, 85, true));
            formacion.add(new ParticipacionDTO(null, "Delantero 2", 11, "Delantero", 65, 85, true));
        }
        
        return formacion;
    }

    public List<ParticipacionDTO> generarFormacionContrariaAlternativa(String tipoFormacion, int opcion) {
        List<ParticipacionDTO> formacion = new ArrayList<>();
        
        if (tipoFormacion.equals("FUTBOL_7")) {
            if (opcion == 1) {
                // Formación 1-2-3-1
                formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 30, 30, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 70, 30, true));
                formacion.add(new ParticipacionDTO(null, "Centro 1", 4, "Centrocampista", 20, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 2", 5, "Centrocampista", 50, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 3", 6, "Centrocampista", 80, 50, true));
                formacion.add(new ParticipacionDTO(null, "Delantero", 7, "Delantero", 50, 80, true));
            } else {
                // Formación 1-3-1-2
                formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 25, 30, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 50, 30, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 3", 4, "Defensa", 75, 30, true));
                formacion.add(new ParticipacionDTO(null, "Centro", 5, "Centrocampista", 50, 50, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 1", 6, "Delantero", 35, 80, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 2", 7, "Delantero", 65, 80, true));
            }
        } else if (tipoFormacion.equals("FUTBOL_11")) {
            if (opcion == 1) {
                // Formación 1-4-4-2
                formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 20, 25, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 40, 25, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 3", 4, "Defensa", 60, 25, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 4", 5, "Defensa", 80, 25, true));
                formacion.add(new ParticipacionDTO(null, "Centro 1", 6, "Centrocampista", 25, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 2", 7, "Centrocampista", 50, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 3", 8, "Centrocampista", 75, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 4", 9, "Centrocampista", 50, 65, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 1", 10, "Delantero", 35, 85, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 2", 11, "Delantero", 65, 85, true));
            } else {
                // Formación 1-3-5-2
                formacion.add(new ParticipacionDTO(null, "Portero", 1, "Portero", 50, 10, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 1", 2, "Defensa", 25, 25, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 2", 3, "Defensa", 50, 25, true));
                formacion.add(new ParticipacionDTO(null, "Defensa 3", 4, "Defensa", 75, 25, true));
                formacion.add(new ParticipacionDTO(null, "Centro 1", 5, "Centrocampista", 15, 45, true));
                formacion.add(new ParticipacionDTO(null, "Centro 2", 6, "Centrocampista", 35, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 3", 7, "Centrocampista", 50, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 4", 8, "Centrocampista", 65, 50, true));
                formacion.add(new ParticipacionDTO(null, "Centro 5", 9, "Centrocampista", 85, 45, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 1", 10, "Delantero", 35, 85, true));
                formacion.add(new ParticipacionDTO(null, "Delantero 2", 11, "Delantero", 65, 85, true));
            }
        }
        
        return formacion;
    }

    public static class ParticipacionDTO {
        private Long jugadorId;
        private String nombreJugador;
        private Integer dorsal;
        private String posicion;
        private Integer posX;
        private Integer posY;
        private Boolean esEquipoContrario;

        public ParticipacionDTO() {}

        public ParticipacionDTO(Long jugadorId, String nombreJugador, Integer dorsal, String posicion, 
                               Integer posX, Integer posY, Boolean esEquipoContrario) {
            this.jugadorId = jugadorId;
            this.nombreJugador = nombreJugador;
            this.dorsal = dorsal;
            this.posicion = posicion;
            this.posX = posX;
            this.posY = posY;
            this.esEquipoContrario = esEquipoContrario;
        }

        // Getters y Setters
        public Long getJugadorId() { return jugadorId; }
        public void setJugadorId(Long jugadorId) { this.jugadorId = jugadorId; }
        public String getNombreJugador() { return nombreJugador; }
        public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }
        public Integer getDorsal() { return dorsal; }
        public void setDorsal(Integer dorsal) { this.dorsal = dorsal; }
        public String getPosicion() { return posicion; }
        public void setPosicion(String posicion) { this.posicion = posicion; }
        public Integer getPosX() { return posX; }
        public void setPosX(Integer posX) { this.posX = posX; }
        public Integer getPosY() { return posY; }
        public void setPosY(Integer posY) { this.posY = posY; }
        public Boolean getEsEquipoContrario() { return esEquipoContrario; }
        public void setEsEquipoContrario(Boolean esEquipoContrario) { this.esEquipoContrario = esEquipoContrario; }
    }
}
