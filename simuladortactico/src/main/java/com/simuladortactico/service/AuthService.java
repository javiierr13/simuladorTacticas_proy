package com.simuladortactico.service;

import com.simuladortactico.model.Entrenador;
import com.simuladortactico.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EntrenadorRepository entrenadorRepository;

    public Entrenador registrar(Entrenador entrenador) {
        if (entrenadorRepository.existsByCorreo(entrenador.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        return entrenadorRepository.save(entrenador);
    }

    public Optional<Entrenador> login(String correo, String contrasena) {
        Optional<Entrenador> entrenador = entrenadorRepository.findByCorreo(correo);
        if (entrenador.isPresent() && entrenador.get().getContrasena().equals(contrasena)) {
            return entrenador;
        }
        return Optional.empty();
    }

    public Optional<Entrenador> findById(Long id) {
        return entrenadorRepository.findById(id);
    }
}
