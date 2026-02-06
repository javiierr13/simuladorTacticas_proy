package com.simuladortactico.repository;

import com.simuladortactico.model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    Optional<Entrenador> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
