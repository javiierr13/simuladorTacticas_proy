package com.simuladortactico.repository;

import com.simuladortactico.model.Alineacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlineacionRepository extends JpaRepository<Alineacion, Long> {
    List<Alineacion> findByEntrenadorId(Long entrenadorId);
}
